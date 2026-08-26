package com.learningstack.infrastructure.persistence.session;

import java.time.Instant;

import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_sessions")
public class LearningSessionJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "text")
	private String goal;

	@Column(name = "root_concept_id")
	private Long rootConceptId;

	@Column(name = "current_concept_id")
	private Long currentConceptId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SessionStatus status;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected LearningSessionJpaEntity() {
	}

	public LearningSessionJpaEntity(LearningSession session) {
		update(session);
	}

	public void update(LearningSession session) {
		this.title = session.getTitle();
		this.goal = session.getGoal();
		this.rootConceptId = session.getRootConceptId();
		this.currentConceptId = session.getCurrentConceptId();
		this.status = session.getStatus();
		this.createdAt = session.getCreatedAt();
		this.updatedAt = session.getUpdatedAt();
	}

	public LearningSession toDomain() {
		return new LearningSession(id, title, goal, rootConceptId, currentConceptId,
				status, createdAt, updatedAt);
	}
}
