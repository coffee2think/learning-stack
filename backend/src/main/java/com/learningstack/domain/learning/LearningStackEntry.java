package com.learningstack.domain.learning;

import java.time.Instant;
import java.util.Objects;

public class LearningStackEntry {

	private final Long id;
	private final Long sessionId;
	private final Long conceptId;
	private final Long parentConceptId;
	private final int stackOrder;
	private final Instant enteredAt;
	private final Instant completedAt;

	public LearningStackEntry(Long id, Long sessionId, Long conceptId, Long parentConceptId,
			int stackOrder, Instant enteredAt, Instant completedAt) {
		this.id = id;
		this.sessionId = Objects.requireNonNull(sessionId);
		this.conceptId = Objects.requireNonNull(conceptId);
		this.parentConceptId = parentConceptId;
		if (stackOrder < 0) {
			throw new IllegalArgumentException("stackOrder must not be negative");
		}
		this.stackOrder = stackOrder;
		this.enteredAt = Objects.requireNonNull(enteredAt);
		this.completedAt = completedAt;
	}

	public Long getId() { return id; }
	public Long getSessionId() { return sessionId; }
	public Long getConceptId() { return conceptId; }
	public Long getParentConceptId() { return parentConceptId; }
	public int getStackOrder() { return stackOrder; }
	public Instant getEnteredAt() { return enteredAt; }
	public Instant getCompletedAt() { return completedAt; }

	public LearningStackEntry complete(Instant completedAt) {
		if (this.completedAt != null) {
			throw new IllegalStateException("LearningStackEntry is already completed");
		}
		return new LearningStackEntry(id, sessionId, conceptId, parentConceptId,
				stackOrder, enteredAt, Objects.requireNonNull(completedAt));
	}
}
