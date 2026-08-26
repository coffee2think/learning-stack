package com.learningstack.application.learning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;
import com.learningstack.domain.learning.LearningStackEntry;
import com.learningstack.domain.learning.LearningStackEntryRepository;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

class LearningStackServiceTest {

	private static final Instant NOW = Instant.parse("2026-08-26T00:00:00Z");
	private final InMemorySessionRepository sessions = new InMemorySessionRepository();
	private final InMemoryConceptRepository concepts = new InMemoryConceptRepository();
	private final InMemoryStackRepository stack = new InMemoryStackRepository();
	private final LearningStackService service = new LearningStackService(sessions, concepts, stack);

	@BeforeEach
	void setUp() {
		sessions.items.clear();
		concepts.items.clear();
		stack.items.clear();
		stack.sequence = 0;
		sessions.save(new LearningSession(1L, "Session", "Goal", 10L, 10L,
				SessionStatus.ACTIVE, NOW, NOW));
		concepts.save(concept(10L, "A"));
		concepts.save(concept(20L, "B"));
		concepts.save(concept(30L, "C"));
	}

	@Test
	void returnsToActualEntryPathForAtoBtoC() {
		service.startConcept(1L, 20L);
		service.startConcept(1L, 30L);

		assertThat(service.getCurrentStack(1L))
				.extracting(LearningStackEntry::getConceptId)
				.containsExactly(10L, 20L, 30L);

		service.completeConcept(1L, 30L);
		assertThat(sessions.findById(1L).orElseThrow().getCurrentConceptId()).isEqualTo(20L);
		assertThat(concepts.findById(30L).orElseThrow().getStatus()).isEqualTo(ConceptStatus.COMPLETED);

		service.completeConcept(1L, 20L);
		assertThat(sessions.findById(1L).orElseThrow().getCurrentConceptId()).isEqualTo(10L);
		assertThat(service.getCurrentStack(1L))
				.extracting(LearningStackEntry::getConceptId)
				.containsExactly(10L);
	}

	@Test
	void rejectsCompletingConceptThatIsNotCurrent() {
		service.startConcept(1L, 20L);

		assertThatThrownBy(() -> service.completeConcept(1L, 10L))
				.isInstanceOf(LearningStackException.class)
				.hasMessageContaining("current concept");
	}

	@Test
	void rejectsCompletionWhenStackIsEmpty() {
		assertThatThrownBy(() -> service.completeConcept(1L, 10L))
				.isInstanceOf(LearningStackException.class)
				.hasMessageContaining("empty");
	}

	@Test
	void duplicateStartDoesNotPushAnotherEntry() {
		service.startConcept(1L, 20L);
		service.startConcept(1L, 20L);

		assertThat(service.getCurrentStack(1L))
				.extracting(LearningStackEntry::getConceptId)
				.containsExactly(10L, 20L);
	}

	private Concept concept(Long id, String name) {
		return new Concept(id, 1L, name, name, name, RelationType.CORE,
				LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, NOW);
	}

	private static final class InMemorySessionRepository implements LearningSessionRepository {
		private final Map<Long, LearningSession> items = new LinkedHashMap<>();
		public LearningSession save(LearningSession session) { items.put(session.getId(), session); return session; }
		public Optional<LearningSession> findById(Long id) { return Optional.ofNullable(items.get(id)); }
		public List<LearningSession> findAll() { return List.copyOf(items.values()); }
	}

	private static final class InMemoryConceptRepository implements ConceptRepository {
		private final Map<Long, Concept> items = new LinkedHashMap<>();
		public Concept save(Concept concept) { items.put(concept.getId(), concept); return concept; }
		public Optional<Concept> findById(Long id) { return Optional.ofNullable(items.get(id)); }
		public List<Concept> findAllBySessionId(Long sessionId) {
			return items.values().stream().filter(item -> item.getSessionId().equals(sessionId)).toList();
		}
	}

	private static final class InMemoryStackRepository implements LearningStackEntryRepository {
		private final List<LearningStackEntry> items = new ArrayList<>();
		private long sequence;
		public LearningStackEntry save(LearningStackEntry entry) {
			LearningStackEntry saved = entry;
			if (entry.getId() == null) {
				saved = new LearningStackEntry(++sequence, entry.getSessionId(), entry.getConceptId(),
						entry.getParentConceptId(), entry.getStackOrder(), entry.getEnteredAt(), entry.getCompletedAt());
			} else {
				items.removeIf(item -> item.getId().equals(entry.getId()));
			}
			items.add(saved);
			return saved;
		}
		public List<LearningStackEntry> findActiveBySessionIdOrderByStackOrder(Long sessionId) {
			return items.stream()
					.filter(item -> item.getSessionId().equals(sessionId) && item.getCompletedAt() == null)
					.sorted((left, right) -> Integer.compare(left.getStackOrder(), right.getStackOrder()))
					.toList();
		}
	}
}
