package com.learningstack.infrastructure.persistence.session;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;

@Repository
public class LearningSessionRepositoryAdapter implements LearningSessionRepository {

	private final SpringDataLearningSessionRepository repository;

	public LearningSessionRepositoryAdapter(SpringDataLearningSessionRepository repository) {
		this.repository = repository;
	}

	@Override
	public LearningSession save(LearningSession session) {
		LearningSessionJpaEntity entity;
		if (session.getId() == null) {
			entity = new LearningSessionJpaEntity(session);
		} else {
			entity = repository.findById(session.getId())
					.orElseThrow(() -> new IllegalArgumentException("LearningSession not found: " + session.getId()));
			entity.update(session);
		}
		return repository.save(entity).toDomain();
	}

	@Override
	public Optional<LearningSession> findById(Long id) {
		return repository.findById(id).map(LearningSessionJpaEntity::toDomain);
	}

	@Override
	public List<LearningSession> findAll() {
		return repository.findAll().stream().map(LearningSessionJpaEntity::toDomain).toList();
	}
}
