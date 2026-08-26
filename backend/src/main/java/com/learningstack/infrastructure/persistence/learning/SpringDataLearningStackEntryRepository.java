package com.learningstack.infrastructure.persistence.learning;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataLearningStackEntryRepository
		extends JpaRepository<LearningStackEntryJpaEntity, Long> {

	List<LearningStackEntryJpaEntity> findAllBySessionIdOrderByStackOrderAsc(Long sessionId);
}
