package com.learningstack.api.session;

import jakarta.validation.constraints.NotBlank;

public record CreateLearningSessionRequest(
		@NotBlank String title,
		@NotBlank String goal) {
}
