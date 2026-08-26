package com.learningstack.infrastructure.persistence.concept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataConceptRepository extends JpaRepository<ConceptJpaEntity, Long> {

	List<ConceptJpaEntity> findAllBySessionId(Long sessionId);
}
