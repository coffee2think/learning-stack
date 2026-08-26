package com.learningstack.domain.concept;

import java.time.Instant;
import java.util.Objects;

public class Concept {

	private final Long id;
	private final Long sessionId;
	private final String name;
	private final String summary;
	private final String description;
	private final RelationType importance;
	private final LearningDepth recommendedDepth;
	private final ConceptStatus status;
	private final Instant createdAt;

	public Concept(Long id, Long sessionId, String name, String summary, String description,
			RelationType importance, LearningDepth recommendedDepth, ConceptStatus status,
			Instant createdAt) {
		this.id = id;
		this.sessionId = Objects.requireNonNull(sessionId);
		this.name = Objects.requireNonNull(name);
		this.summary = Objects.requireNonNull(summary);
		this.description = Objects.requireNonNull(description);
		this.importance = Objects.requireNonNull(importance);
		this.recommendedDepth = Objects.requireNonNull(recommendedDepth);
		this.status = Objects.requireNonNull(status);
		this.createdAt = Objects.requireNonNull(createdAt);
	}

	public Long getId() { return id; }
	public Long getSessionId() { return sessionId; }
	public String getName() { return name; }
	public String getSummary() { return summary; }
	public String getDescription() { return description; }
	public RelationType getImportance() { return importance; }
	public LearningDepth getRecommendedDepth() { return recommendedDepth; }
	public ConceptStatus getStatus() { return status; }
	public Instant getCreatedAt() { return createdAt; }

	public Concept startLearning() {
		return withStatus(ConceptStatus.LEARNING);
	}

	public Concept complete() {
		return withStatus(ConceptStatus.COMPLETED);
	}

	private Concept withStatus(ConceptStatus newStatus) {
		return new Concept(id, sessionId, name, summary, description, importance,
				recommendedDepth, newStatus, createdAt);
	}
}
