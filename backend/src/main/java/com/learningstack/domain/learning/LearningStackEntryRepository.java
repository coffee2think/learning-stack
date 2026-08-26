package com.learningstack.domain.learning;

import java.util.List;

public interface LearningStackEntryRepository {

	LearningStackEntry save(LearningStackEntry entry);
	List<LearningStackEntry> findAllBySessionIdOrderByStackOrder(Long sessionId);
}
