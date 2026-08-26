package com.learningstack.infrastructure.persistence.concept;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRepository;

@Repository
public class ConceptRepositoryAdapter implements ConceptRepository {

	private final SpringDataConceptRepository repository;

	public ConceptRepositoryAdapter(SpringDataConceptRepository repository) {
		this.repository = repository;
	}

	@Override
	public Concept save(Concept concept) {
		ConceptJpaEntity entity;
		if (concept.getId() == null) {
			entity = new ConceptJpaEntity(concept);
		} else {
			entity = repository.findById(concept.getId())
					.orElseThrow(() -> new IllegalArgumentException("Concept not found: " + concept.getId()));
			entity.update(concept);
		}
		return repository.save(entity).toDomain();
	}

	@Override
	public Optional<Concept> findById(Long id) {
		return repository.findById(id).map(ConceptJpaEntity::toDomain);
	}

	@Override
	public List<Concept> findAllBySessionId(Long sessionId) {
		return repository.findAllBySessionId(sessionId).stream()
				.map(ConceptJpaEntity::toDomain)
				.toList();
	}
}
