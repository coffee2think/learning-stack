package com.learningstack.infrastructure.persistence.bookmark;

import java.time.Instant;

import com.learningstack.domain.bookmark.Bookmark;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookmarks")
public class BookmarkJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false)
	private Long sessionId;

	@Column(name = "concept_id", nullable = false)
	private Long conceptId;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected BookmarkJpaEntity() {
	}

	public BookmarkJpaEntity(Bookmark bookmark) {
		update(bookmark);
	}

	public void update(Bookmark bookmark) {
		this.sessionId = bookmark.getSessionId();
		this.conceptId = bookmark.getConceptId();
		this.createdAt = bookmark.getCreatedAt();
	}

	public Bookmark toDomain() {
		return new Bookmark(id, sessionId, conceptId, createdAt);
	}
}
