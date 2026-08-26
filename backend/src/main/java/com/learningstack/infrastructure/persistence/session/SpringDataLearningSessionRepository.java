package com.learningstack.infrastructure.persistence.session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataLearningSessionRepository
		extends JpaRepository<LearningSessionJpaEntity, Long> {
}
