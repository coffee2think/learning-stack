package com.learningstack.infrastructure.persistence.concept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataConceptRelationRepository
		extends JpaRepository<ConceptRelationJpaEntity, Long> {

	List<ConceptRelationJpaEntity> findAllBySessionId(Long sessionId);
}
