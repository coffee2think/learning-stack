package com.learningstack.application.concept;

public class ConceptNotFoundException extends RuntimeException {

	public ConceptNotFoundException(Long conceptId) {
		super("Concept not found: " + conceptId);
	}
}
