package com.learningstack.application.concept;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learningstack.application.session.LearningSessionNotFoundException;
import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.session.LearningSessionRepository;

@Service
public class ConceptQueryService {

	private final LearningSessionRepository sessionRepository;
	private final ConceptRepository conceptRepository;
	private final ConceptRelationRepository relationRepository;

	public ConceptQueryService(LearningSessionRepository sessionRepository,
			ConceptRepository conceptRepository,
			ConceptRelationRepository relationRepository) {
		this.sessionRepository = sessionRepository;
		this.conceptRepository = conceptRepository;
		this.relationRepository = relationRepository;
	}

	@Transactional(readOnly = true)
	public Concept findById(Long conceptId) {
		return conceptRepository.findById(conceptId)
				.orElseThrow(() -> new ConceptNotFoundException(conceptId));
	}

	@Transactional(readOnly = true)
	public List<Concept> findAllBySessionId(Long sessionId) {
		ensureSessionExists(sessionId);
		return conceptRepository.findAllBySessionId(sessionId);
	}

	@Transactional(readOnly = true)
	public List<ConceptRelation> findRelationsBySessionId(Long sessionId) {
		ensureSessionExists(sessionId);
		return relationRepository.findAllBySessionId(sessionId);
	}

	private void ensureSessionExists(Long sessionId) {
		if (sessionRepository.findById(sessionId).isEmpty()) {
			throw new LearningSessionNotFoundException(sessionId);
		}
	}
}
