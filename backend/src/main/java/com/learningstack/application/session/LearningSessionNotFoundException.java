package com.learningstack.application.session;

public class LearningSessionNotFoundException extends RuntimeException {

	public LearningSessionNotFoundException(Long sessionId) {
		super("LearningSession not found: " + sessionId);
	}
}
