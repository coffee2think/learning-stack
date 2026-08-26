package com.learningstack.api.concept;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learningstack.application.concept.ConceptNotFoundException;
import com.learningstack.application.concept.ConceptQueryService;
import com.learningstack.application.session.LearningSessionNotFoundException;

@RestController
public class ConceptController {

	private final ConceptQueryService conceptQueryService;

	public ConceptController(ConceptQueryService conceptQueryService) {
		this.conceptQueryService = conceptQueryService;
	}

	@GetMapping("/api/concepts/{conceptId}")
	public ConceptResponse findById(@PathVariable Long conceptId) {
		return ConceptResponse.from(conceptQueryService.findById(conceptId));
	}

	@GetMapping("/api/sessions/{sessionId}/concepts")
	public List<ConceptResponse> findAllBySessionId(@PathVariable Long sessionId) {
		return conceptQueryService.findAllBySessionId(sessionId).stream()
				.map(ConceptResponse::from)
				.toList();
	}

	@GetMapping("/api/sessions/{sessionId}/relations")
	public List<ConceptRelationResponse> findRelationsBySessionId(@PathVariable Long sessionId) {
		return conceptQueryService.findRelationsBySessionId(sessionId).stream()
				.map(ConceptRelationResponse::from)
				.toList();
	}

	@ExceptionHandler({ConceptNotFoundException.class, LearningSessionNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void handleNotFound() {
	}
}
