package com.learningstack.application.concept;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learningstack.application.session.LearningSessionNotFoundException;
import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@ExtendWith(MockitoExtension.class)
class ConceptQueryServiceTest {

	@Mock LearningSessionRepository sessionRepository;
	@Mock ConceptRepository conceptRepository;
	@Mock ConceptRelationRepository relationRepository;
	@InjectMocks ConceptQueryService service;

	@Test
	void returnsConceptById() {
		Concept concept = concept(1L, 10L, "Transaction");
		given(conceptRepository.findById(1L)).willReturn(Optional.of(concept));

		assertThat(service.findById(1L)).isSameAs(concept);
	}

	@Test
	void throwsWhenConceptDoesNotExist() {
		given(conceptRepository.findById(404L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> service.findById(404L))
				.isInstanceOf(ConceptNotFoundException.class)
				.hasMessageContaining("404");
	}

	@Test
	void returnsConceptsForExistingSession() {
		LearningSession session = session(10L);
		List<Concept> concepts = List.of(concept(1L, 10L, "Transaction"));
		given(sessionRepository.findById(10L)).willReturn(Optional.of(session));
		given(conceptRepository.findAllBySessionId(10L)).willReturn(concepts);

		assertThat(service.findAllBySessionId(10L)).containsExactlyElementsOf(concepts);
	}

	@Test
	void returnsRelationsForExistingSession() {
		ConceptRelation relation = new ConceptRelation(3L, 10L, 1L, 2L,
				RelationType.CORE, "Core concept");
		given(sessionRepository.findById(10L)).willReturn(Optional.of(session(10L)));
		given(relationRepository.findAllBySessionId(10L)).willReturn(List.of(relation));

		assertThat(service.findRelationsBySessionId(10L)).containsExactly(relation);
	}

	@Test
	void throwsBeforeQueryingConceptsWhenSessionDoesNotExist() {
		given(sessionRepository.findById(404L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> service.findAllBySessionId(404L))
				.isInstanceOf(LearningSessionNotFoundException.class);
		verify(sessionRepository).findById(404L);
	}

	@Test
	void throwsBeforeQueryingRelationsWhenSessionDoesNotExist() {
		given(sessionRepository.findById(404L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> service.findRelationsBySessionId(404L))
				.isInstanceOf(LearningSessionNotFoundException.class);
		verify(sessionRepository).findById(404L);
	}

	private Concept concept(Long id, Long sessionId, String name) {
		return new Concept(id, sessionId, name, "Summary", "Description",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.NOT_STARTED,
				Instant.parse("2026-08-26T00:00:00Z"));
	}

	private LearningSession session(Long id) {
		Instant now = Instant.parse("2026-08-26T00:00:00Z");
		return new LearningSession(id, "Session", "Goal", null, null,
				SessionStatus.ACTIVE, now, now);
	}
}
