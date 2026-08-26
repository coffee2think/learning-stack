package com.learningstack.infrastructure.persistence.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.learningstack.domain.bookmark.Bookmark;
import com.learningstack.domain.bookmark.BookmarkRepository;

@Repository
public class BookmarkRepositoryAdapter implements BookmarkRepository {

	private final SpringDataBookmarkRepository repository;

	public BookmarkRepositoryAdapter(SpringDataBookmarkRepository repository) {
		this.repository = repository;
	}

	@Override
	public Bookmark save(Bookmark bookmark) {
		BookmarkJpaEntity entity;
		if (bookmark.getId() == null) {
			entity = new BookmarkJpaEntity(bookmark);
		} else {
			entity = repository.findById(bookmark.getId())
					.orElseThrow(() -> new IllegalArgumentException("Bookmark not found: " + bookmark.getId()));
			entity.update(bookmark);
		}
		return repository.save(entity).toDomain();
	}

	@Override
	public Optional<Bookmark> findBySessionIdAndConceptId(Long sessionId, Long conceptId) {
		return repository.findBySessionIdAndConceptId(sessionId, conceptId)
				.map(BookmarkJpaEntity::toDomain);
	}

	@Override
	public List<Bookmark> findAllBySessionId(Long sessionId) {
		return repository.findAllBySessionId(sessionId).stream()
				.map(BookmarkJpaEntity::toDomain)
				.toList();
	}
}
