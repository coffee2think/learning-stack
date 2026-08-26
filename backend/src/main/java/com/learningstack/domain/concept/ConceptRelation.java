package com.learningstack.domain.concept;

import java.util.Objects;

public class ConceptRelation {

	private final Long id;
	private final Long sessionId;
	private final Long sourceConceptId;
	private final Long targetConceptId;
	private final RelationType relationType;
	private final String reason;

	public ConceptRelation(Long id, Long sessionId, Long sourceConceptId, Long targetConceptId,
			RelationType relationType, String reason) {
		this.id = id;
		this.sessionId = Objects.requireNonNull(sessionId);
		this.sourceConceptId = Objects.requireNonNull(sourceConceptId);
		this.targetConceptId = Objects.requireNonNull(targetConceptId);
		this.relationType = Objects.requireNonNull(relationType);
		this.reason = Objects.requireNonNull(reason);
	}

	public Long getId() { return id; }
	public Long getSessionId() { return sessionId; }
	public Long getSourceConceptId() { return sourceConceptId; }
	public Long getTargetConceptId() { return targetConceptId; }
	public RelationType getRelationType() { return relationType; }
	public String getReason() { return reason; }
}
