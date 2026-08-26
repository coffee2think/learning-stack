package com.learningstack.infrastructure.persistence.learning;

import java.time.Instant;

import com.learningstack.domain.learning.LearningStackEntry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_stack_entries")
public class LearningStackEntryJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false)
	private Long sessionId;

	@Column(name = "concept_id", nullable = false)
	private Long conceptId;

	@Column(name = "parent_concept_id")
	private Long parentConceptId;

	@Column(name = "stack_order", nullable = false)
	private int stackOrder;

	@Column(name = "entered_at", nullable = false)
	private Instant enteredAt;

	@Column(name = "completed_at")
	private Instant completedAt;

	protected LearningStackEntryJpaEntity() {
	}

	public LearningStackEntryJpaEntity(LearningStackEntry entry) {
		update(entry);
	}

	public void update(LearningStackEntry entry) {
		this.sessionId = entry.getSessionId();
		this.conceptId = entry.getConceptId();
		this.parentConceptId = entry.getParentConceptId();
		this.stackOrder = entry.getStackOrder();
		this.enteredAt = entry.getEnteredAt();
		this.completedAt = entry.getCompletedAt();
	}

	public LearningStackEntry toDomain() {
		return new LearningStackEntry(id, sessionId, conceptId, parentConceptId,
				stackOrder, enteredAt, completedAt);
	}
}
