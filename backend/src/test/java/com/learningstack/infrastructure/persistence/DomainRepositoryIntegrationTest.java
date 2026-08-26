package com.learningstack.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.learningstack.domain.bookmark.Bookmark;
import com.learningstack.domain.bookmark.BookmarkRepository;
import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;
import com.learningstack.domain.learning.LearningStackEntry;
import com.learningstack.domain.learning.LearningStackEntryRepository;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DomainRepositoryIntegrationTest {

	@Autowired
	private LearningSessionRepository sessionRepository;

	@Autowired
	private ConceptRepository conceptRepository;

	@Autowired
	private ConceptRelationRepository relationRepository;

	@Autowired
	private LearningStackEntryRepository stackRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Test
	void persistsDomainGraphAndActualLearningPath() {
		Instant now = Instant.parse("2026-08-26T00:00:00Z");
		LearningSession session = sessionRepository.save(new LearningSession(
				null, "Transaction", "Understand transactions", null, null,
				SessionStatus.ACTIVE, now, now));

		Concept transaction = saveConcept(session.getId(), "Transaction", RelationType.CORE,
				LearningDepth.DEEP, now);
		Concept acid = saveConcept(session.getId(), "ACID", RelationType.CORE,
				LearningDepth.DEEP, now);
		Concept isolation = saveConcept(session.getId(), "Isolation", RelationType.PREREQUISITE,
				LearningDepth.UNDERSTAND, now);

		LearningSession positionedSession = sessionRepository.save(new LearningSession(
				session.getId(), session.getTitle(), session.getGoal(), transaction.getId(),
				isolation.getId(), session.getStatus(), session.getCreatedAt(), now.plusSeconds(1)));

		ConceptRelation relation = relationRepository.save(new ConceptRelation(
				null, session.getId(), transaction.getId(), isolation.getId(),
				RelationType.PREREQUISITE, "Graph relation used for exploration"));

		stackRepository.save(new LearningStackEntry(
				null, session.getId(), transaction.getId(), null, 0, now, null));
		LearningStackEntry actualPathEntry = stackRepository.save(new LearningStackEntry(
				null, session.getId(), isolation.getId(), acid.getId(), 1,
				now.plusSeconds(1), null));
		Bookmark bookmark = bookmarkRepository.save(new Bookmark(
				null, session.getId(), isolation.getId(), now.plusSeconds(2)));

		assertThat(positionedSession.getRootConceptId()).isEqualTo(transaction.getId());
		assertThat(positionedSession.getCurrentConceptId()).isEqualTo(isolation.getId());
		assertThat(conceptRepository.findAllBySessionId(session.getId()))
				.extracting(Concept::getName)
				.containsExactlyInAnyOrder("Transaction", "ACID", "Isolation");
		assertThat(relationRepository.findAllBySessionId(session.getId()))
				.singleElement()
				.extracting(ConceptRelation::getId)
				.isEqualTo(relation.getId());
		assertThat(stackRepository.findAllBySessionIdOrderByStackOrder(session.getId()))
				.extracting(LearningStackEntry::getConceptId)
				.containsExactly(transaction.getId(), isolation.getId());
		assertThat(actualPathEntry.getParentConceptId()).isEqualTo(acid.getId());
		assertThat(actualPathEntry.getParentConceptId()).isNotEqualTo(relation.getSourceConceptId());
		assertThat(bookmarkRepository.findBySessionIdAndConceptId(session.getId(), isolation.getId()))
				.map(Bookmark::getId)
				.contains(bookmark.getId());
	}

	private Concept saveConcept(Long sessionId, String name, RelationType importance,
			LearningDepth depth, Instant createdAt) {
		return conceptRepository.save(new Concept(
				null, sessionId, name, name + " summary", name + " description",
				importance, depth, ConceptStatus.NOT_STARTED, createdAt));
	}
}
