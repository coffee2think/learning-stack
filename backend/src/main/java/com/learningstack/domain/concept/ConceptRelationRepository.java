package com.learningstack.domain.concept;

import java.util.List;

public interface ConceptRelationRepository {

	ConceptRelation save(ConceptRelation relation);
	List<ConceptRelation> findAllBySessionId(Long sessionId);
}
