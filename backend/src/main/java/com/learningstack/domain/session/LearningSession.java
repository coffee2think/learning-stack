package com.learningstack.domain.session;

import java.time.Instant;
import java.util.Objects;

public class LearningSession {

	private final Long id;
	private final String title;
	private final String goal;
	private final Long rootConceptId;
	private final Long currentConceptId;
	private final SessionStatus status;
	private final Instant createdAt;
	private final Instant updatedAt;

	public LearningSession(Long id, String title, String goal, Long rootConceptId,
			Long currentConceptId, SessionStatus status, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.title = Objects.requireNonNull(title);
		this.goal = Objects.requireNonNull(goal);
		this.rootConceptId = rootConceptId;
		this.currentConceptId = currentConceptId;
		this.status = Objects.requireNonNull(status);
		this.createdAt = Objects.requireNonNull(createdAt);
		this.updatedAt = Objects.requireNonNull(updatedAt);
	}

	public Long getId() { return id; }
	public String getTitle() { return title; }
	public String getGoal() { return goal; }
	public Long getRootConceptId() { return rootConceptId; }
	public Long getCurrentConceptId() { return currentConceptId; }
	public SessionStatus getStatus() { return status; }
	public Instant getCreatedAt() { return createdAt; }
	public Instant getUpdatedAt() { return updatedAt; }

	public LearningSession moveTo(Long conceptId, Instant changedAt) {
		return new LearningSession(id, title, goal, rootConceptId, conceptId,
				status, createdAt, changedAt);
	}
}
