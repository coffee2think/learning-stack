package com.learningstack.infrastructure.persistence.concept;

import java.time.Instant;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "concepts")
public class ConceptJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false)
	private Long sessionId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "text")
	private String summary;

	@Column(nullable = false, columnDefinition = "text")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RelationType importance;

	@Enumerated(EnumType.STRING)
	@Column(name = "recommended_depth", nullable = false)
	private LearningDepth recommendedDepth;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConceptStatus status;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected ConceptJpaEntity() {
	}

	public ConceptJpaEntity(Concept concept) {
		update(concept);
	}

	public void update(Concept concept) {
		this.sessionId = concept.getSessionId();
		this.name = concept.getName();
		this.summary = concept.getSummary();
		this.description = concept.getDescription();
		this.importance = concept.getImportance();
		this.recommendedDepth = concept.getRecommendedDepth();
		this.status = concept.getStatus();
		this.createdAt = concept.getCreatedAt();
	}

	public Concept toDomain() {
		return new Concept(id, sessionId, name, summary, description, importance,
				recommendedDepth, status, createdAt);
	}
}
