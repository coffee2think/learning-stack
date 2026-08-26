package com.learningstack.api.session;

import java.time.Instant;

import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.SessionStatus;

public record LearningSessionResponse(
		Long id,
		String title,
		String goal,
		Long rootConceptId,
		Long currentConceptId,
		SessionStatus status,
		Instant createdAt,
		Instant updatedAt) {

	public static LearningSessionResponse from(LearningSession session) {
		return new LearningSessionResponse(
				session.getId(),
				session.getTitle(),
				session.getGoal(),
				session.getRootConceptId(),
				session.getCurrentConceptId(),
				session.getStatus(),
				session.getCreatedAt(),
				session.getUpdatedAt());
	}
}
