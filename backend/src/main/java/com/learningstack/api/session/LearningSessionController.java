package com.learningstack.api.session;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learningstack.application.session.LearningSessionNotFoundException;
import com.learningstack.application.session.LearningSessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
public class LearningSessionController {

	private final LearningSessionService sessionService;

	public LearningSessionController(LearningSessionService sessionService) {
		this.sessionService = sessionService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LearningSessionResponse create(@Valid @RequestBody CreateLearningSessionRequest request) {
		return LearningSessionResponse.from(sessionService.create(request.title(), request.goal()));
	}

	@GetMapping
	public List<LearningSessionResponse> findAll() {
		return sessionService.findAll().stream()
				.map(LearningSessionResponse::from)
				.toList();
	}

	@GetMapping("/{sessionId}")
	public LearningSessionResponse findById(@PathVariable Long sessionId) {
		return LearningSessionResponse.from(sessionService.findById(sessionId));
	}

	@ExceptionHandler(LearningSessionNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void handleNotFound() {
	}
}
