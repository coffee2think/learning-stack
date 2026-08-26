package com.learningstack.infrastructure.persistence.concept;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;

@Repository
public class ConceptRelationRepositoryAdapter implements ConceptRelationRepository {

	private final SpringDataConceptRelationRepository repository;

	public ConceptRelationRepositoryAdapter(SpringDataConceptRelationRepository repository) {
		this.repository = repository;
	}

	@Override
	public ConceptRelation save(ConceptRelation relation) {
		ConceptRelationJpaEntity entity;
		if (relation.getId() == null) {
			entity = new ConceptRelationJpaEntity(relation);
		} else {
			entity = repository.findById(relation.getId())
					.orElseThrow(() -> new IllegalArgumentException("ConceptRelation not found: " + relation.getId()));
			entity.update(relation);
		}
		return repository.save(entity).toDomain();
	}

	@Override
	public List<ConceptRelation> findAllBySessionId(Long sessionId) {
		return repository.findAllBySessionId(sessionId).stream()
				.map(ConceptRelationJpaEntity::toDomain)
				.toList();
	}
}
