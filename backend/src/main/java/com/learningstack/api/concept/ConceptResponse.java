package com.learningstack.api.concept;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;

public record ConceptResponse(
		Long id,
		String name,
		String summary,
		String description,
		ConceptStatus status,
		RelationType importance,
		LearningDepth recommendedDepth) {

	public static ConceptResponse from(Concept concept) {
		return new ConceptResponse(
				concept.getId(),
				concept.getName(),
				concept.getSummary(),
				concept.getDescription(),
				concept.getStatus(),
				concept.getImportance(),
				concept.getRecommendedDepth());
	}
}
