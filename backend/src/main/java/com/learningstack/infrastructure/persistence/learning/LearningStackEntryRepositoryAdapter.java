package com.learningstack.infrastructure.persistence.learning;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.learningstack.domain.learning.LearningStackEntry;
import com.learningstack.domain.learning.LearningStackEntryRepository;

@Repository
public class LearningStackEntryRepositoryAdapter implements LearningStackEntryRepository {

	private final SpringDataLearningStackEntryRepository repository;

	public LearningStackEntryRepositoryAdapter(SpringDataLearningStackEntryRepository repository) {
		this.repository = repository;
	}

	@Override
	public LearningStackEntry save(LearningStackEntry entry) {
		LearningStackEntryJpaEntity entity;
		if (entry.getId() == null) {
			entity = new LearningStackEntryJpaEntity(entry);
		} else {
			entity = repository.findById(entry.getId())
					.orElseThrow(() -> new IllegalArgumentException("LearningStackEntry not found: " + entry.getId()));
			entity.update(entry);
		}
		return repository.save(entity).toDomain();
	}

	@Override
	public List<LearningStackEntry> findAllBySessionIdOrderByStackOrder(Long sessionId) {
		return repository.findAllBySessionIdOrderByStackOrderAsc(sessionId).stream()
				.map(LearningStackEntryJpaEntity::toDomain)
				.toList();
	}
}
