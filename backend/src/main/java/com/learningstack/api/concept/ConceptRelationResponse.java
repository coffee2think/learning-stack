package com.learningstack.api.concept;

import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.RelationType;

public record ConceptRelationResponse(
		Long id,
		Long sourceConceptId,
		Long targetConceptId,
		RelationType relationType,
		String reason) {

	public static ConceptRelationResponse from(ConceptRelation relation) {
		return new ConceptRelationResponse(
				relation.getId(),
				relation.getSourceConceptId(),
				relation.getTargetConceptId(),
				relation.getRelationType(),
				relation.getReason());
	}
}
