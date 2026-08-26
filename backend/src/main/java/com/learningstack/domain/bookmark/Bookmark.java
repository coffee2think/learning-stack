package com.learningstack.domain.bookmark;

import java.time.Instant;
import java.util.Objects;

public class Bookmark {

	private final Long id;
	private final Long sessionId;
	private final Long conceptId;
	private final Instant createdAt;

	public Bookmark(Long id, Long sessionId, Long conceptId, Instant createdAt) {
		this.id = id;
		this.sessionId = Objects.requireNonNull(sessionId);
		this.conceptId = Objects.requireNonNull(conceptId);
		this.createdAt = Objects.requireNonNull(createdAt);
	}

	public Long getId() { return id; }
	public Long getSessionId() { return sessionId; }
	public Long getConceptId() { return conceptId; }
	public Instant getCreatedAt() { return createdAt; }
}
