package com.learningstack.infrastructure.persistence.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookmarkRepository extends JpaRepository<BookmarkJpaEntity, Long> {

	Optional<BookmarkJpaEntity> findBySessionIdAndConceptId(Long sessionId, Long conceptId);
	List<BookmarkJpaEntity> findAllBySessionId(Long sessionId);
}
