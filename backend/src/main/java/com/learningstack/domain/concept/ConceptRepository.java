package com.learningstack.domain.concept;

import java.util.List;
import java.util.Optional;

public interface ConceptRepository {

	Concept save(Concept concept);
	Optional<Concept> findById(Long id);
	List<Concept> findAllBySessionId(Long sessionId);
}
