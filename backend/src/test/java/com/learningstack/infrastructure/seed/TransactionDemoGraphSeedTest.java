package com.learningstack.infrastructure.seed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.DefaultApplicationArguments;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@ExtendWith(MockitoExtension.class)
class TransactionDemoGraphSeedTest {

	@Mock LearningSessionRepository sessionRepository;
	@Mock ConceptRepository conceptRepository;
	@Mock ConceptRelationRepository relationRepository;

	private TransactionDemoGraphSeed seed;

	@BeforeEach
	void setUp() {
		seed = new TransactionDemoGraphSeed(sessionRepository, conceptRepository, relationRepository);
	}

	@Test
	void createsTheTransactionDemoGraphAndPositionsSessionAtRoot() throws Exception {
		given(sessionRepository.findAll()).willReturn(List.of());
		AtomicLong sessionIds = new AtomicLong(1);
		given(sessionRepository.save(any())).willAnswer(invocation -> {
			LearningSession value = invocation.getArgument(0);
			if (value.getId() != null) {
				return value;
			}
			return new LearningSession(sessionIds.getAndIncrement(), value.getTitle(), value.getGoal(),
					value.getRootConceptId(), value.getCurrentConceptId(), value.getStatus(),
					value.getCreatedAt(), value.getUpdatedAt());
		});
		AtomicLong conceptIds = new AtomicLong(100);
		given(conceptRepository.save(any())).willAnswer(invocation -> {
			Concept value = invocation.getArgument(0);
			return new Concept(conceptIds.getAndIncrement(), value.getSessionId(), value.getName(),
					value.getSummary(), value.getDescription(), value.getImportance(),
					value.getRecommendedDepth(), value.getStatus(), value.getCreatedAt());
		});

		seed.run(new DefaultApplicationArguments());

		ArgumentCaptor<Concept> conceptCaptor = ArgumentCaptor.forClass(Concept.class);
		verify(conceptRepository, times(15)).save(conceptCaptor.capture());
		List<Concept> concepts = conceptCaptor.getAllValues();
		assertThat(concepts).extracting(Concept::getName).containsExactly(
				"Transaction", "Commit / Rollback", "Transaction Boundary", "ACID", "Atomicity",
				"Consistency", "Isolation", "Isolation Level", "Dirty Read", "Non-Repeatable Read",
				"Phantom Read", "Lock", "MVCC", "Durability", "Spring @Transactional");
		assertThat(concepts.getFirst().getStatus()).isEqualTo(ConceptStatus.LEARNING);

		ArgumentCaptor<ConceptRelation> relationCaptor = ArgumentCaptor.forClass(ConceptRelation.class);
		verify(relationRepository, times(14)).save(relationCaptor.capture());
		assertThat(relationCaptor.getAllValues())
				.extracting(ConceptRelation::getSourceConceptId, ConceptRelation::getTargetConceptId)
				.containsExactly(
						tuple(100L, 101L), tuple(100L, 102L), tuple(100L, 103L),
						tuple(103L, 104L), tuple(103L, 105L), tuple(103L, 106L),
						tuple(106L, 107L), tuple(107L, 108L), tuple(107L, 109L),
						tuple(107L, 110L), tuple(107L, 111L), tuple(111L, 112L),
						tuple(103L, 113L), tuple(100L, 114L));

		ArgumentCaptor<LearningSession> sessionCaptor = ArgumentCaptor.forClass(LearningSession.class);
		verify(sessionRepository, times(2)).save(sessionCaptor.capture());
		LearningSession positioned = sessionCaptor.getAllValues().getLast();
		assertThat(positioned.getRootConceptId()).isEqualTo(100L);
		assertThat(positioned.getCurrentConceptId()).isEqualTo(100L);
	}

	@Test
	void skipsSeedWhenTheDemoSessionAlreadyExists() throws Exception {
		Instant now = Instant.parse("2026-08-26T00:00:00Z");
		given(sessionRepository.findAll()).willReturn(new ArrayList<>(List.of(new LearningSession(
				1L, TransactionDemoGraphSeed.SESSION_TITLE, TransactionDemoGraphSeed.SESSION_GOAL,
				10L, 10L, SessionStatus.ACTIVE, now, now))));

		seed.run(new DefaultApplicationArguments());

		verify(sessionRepository, never()).save(any());
		verify(conceptRepository, never()).save(any());
		verify(relationRepository, never()).save(any());
	}
}
