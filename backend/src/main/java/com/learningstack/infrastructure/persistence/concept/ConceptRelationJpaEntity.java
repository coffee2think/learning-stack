package com.learningstack.infrastructure.persistence.concept;

import com.learningstack.domain.concept.ConceptRelation;
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
@Table(name = "concept_relations")
public class ConceptRelationJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false)
	private Long sessionId;

	@Column(name = "source_concept_id", nullable = false)
	private Long sourceConceptId;

	@Column(name = "target_concept_id", nullable = false)
	private Long targetConceptId;

	@Enumerated(EnumType.STRING)
	@Column(name = "relation_type", nullable = false)
	private RelationType relationType;

	@Column(nullable = false, columnDefinition = "text")
	private String reason;

	protected ConceptRelationJpaEntity() {
	}

	public ConceptRelationJpaEntity(ConceptRelation relation) {
		update(relation);
	}

	public void update(ConceptRelation relation) {
		this.sessionId = relation.getSessionId();
		this.sourceConceptId = relation.getSourceConceptId();
		this.targetConceptId = relation.getTargetConceptId();
		this.relationType = relation.getRelationType();
		this.reason = relation.getReason();
	}

	public ConceptRelation toDomain() {
		return new ConceptRelation(id, sessionId, sourceConceptId, targetConceptId,
				relationType, reason);
	}
}
