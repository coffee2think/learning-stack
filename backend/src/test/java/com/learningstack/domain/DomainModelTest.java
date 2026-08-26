package com.learningstack.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;
import com.learningstack.domain.learning.LearningStackEntry;
import com.learningstack.domain.session.SessionStatus;

class DomainModelTest {

	@Test
	void learningStackEntryRemembersTheActualEntryPath() {
		Instant enteredAt = Instant.parse("2026-08-26T00:00:00Z");
		LearningStackEntry entry = new LearningStackEntry(
				1L, 10L, 300L, 200L, 2, enteredAt, null);

		assertThat(entry.getConceptId()).isEqualTo(300L);
		assertThat(entry.getParentConceptId()).isEqualTo(200L);
		assertThat(entry.getStackOrder()).isEqualTo(2);
	}

	@Test
	void enumValuesMatchTheDomainDefinition() {
		assertThat(ConceptStatus.values()).containsExactly(
				ConceptStatus.NOT_STARTED,
				ConceptStatus.LEARNING,
				ConceptStatus.PAUSED,
				ConceptStatus.COMPLETED,
				ConceptStatus.SKIPPED,
				ConceptStatus.BOOKMARKED);
		assertThat(RelationType.values()).containsExactly(
				RelationType.PREREQUISITE,
				RelationType.CORE,
				RelationType.IMPLEMENTATION,
				RelationType.ADVANCED,
				RelationType.RELATED);
		assertThat(LearningDepth.values()).containsExactly(
				LearningDepth.LIGHT,
				LearningDepth.UNDERSTAND,
				LearningDepth.DEEP);
		assertThat(SessionStatus.values()).containsExactly(
				SessionStatus.ACTIVE,
				SessionStatus.COMPLETED);
	}
}
