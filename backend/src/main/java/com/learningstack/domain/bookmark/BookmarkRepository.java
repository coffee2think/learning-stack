package com.learningstack.domain.bookmark;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

	Bookmark save(Bookmark bookmark);
	Optional<Bookmark> findBySessionIdAndConceptId(Long sessionId, Long conceptId);
	List<Bookmark> findAllBySessionId(Long sessionId);
}
