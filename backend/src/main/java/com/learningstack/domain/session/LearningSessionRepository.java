package com.learningstack.domain.session;

import java.util.List;
import java.util.Optional;

public interface LearningSessionRepository {

	LearningSession save(LearningSession session);
	Optional<LearningSession> findById(Long id);
	List<LearningSession> findAll();
}
