package com.learningstack.application.learning;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.learning.LearningStackEntry;
import com.learningstack.domain.learning.LearningStackEntryRepository;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;

@Service
public class LearningStackService {

	private final LearningSessionRepository sessionRepository;
	private final ConceptRepository conceptRepository;
	private final LearningStackEntryRepository stackRepository;

	public LearningStackService(LearningSessionRepository sessionRepository,
			ConceptRepository conceptRepository,
			LearningStackEntryRepository stackRepository) {
		this.sessionRepository = sessionRepository;
		this.conceptRepository = conceptRepository;
		this.stackRepository = stackRepository;
	}

	@Transactional
	public List<LearningStackEntry> startConcept(Long sessionId, Long conceptId) {
		LearningSession session = requireSession(sessionId);
		Concept target = requireConceptInSession(conceptId, sessionId);
		Long currentConceptId = session.getCurrentConceptId();
		if (currentConceptId == null) {
			throw new LearningStackException("Session has no current concept: " + sessionId);
		}
		if (currentConceptId.equals(conceptId)) {
			return getCurrentStack(sessionId);
		}

		Instant now = Instant.now();
		List<LearningStackEntry> stack = getCurrentStack(sessionId);
		if (stack.isEmpty()) {
			requireConceptInSession(currentConceptId, sessionId);
			stackRepository.save(new LearningStackEntry(
					null, sessionId, currentConceptId, null, 0, now, null));
		}

		int nextOrder = stack.isEmpty() ? 1 : stack.get(stack.size() - 1).getStackOrder() + 1;
		stackRepository.save(new LearningStackEntry(
				null, sessionId, conceptId, currentConceptId, nextOrder, now, null));
		conceptRepository.save(target.startLearning());
		sessionRepository.save(session.moveTo(conceptId, now));
		return getCurrentStack(sessionId);
	}

	@Transactional
	public List<LearningStackEntry> completeConcept(Long sessionId, Long conceptId) {
		LearningSession session = requireSession(sessionId);
		if (!conceptId.equals(session.getCurrentConceptId())) {
			throw new LearningStackException("Only the current concept can be completed: " + conceptId);
		}
		Concept current = requireConceptInSession(conceptId, sessionId);
		List<LearningStackEntry> stack = getCurrentStack(sessionId);
		if (stack.isEmpty()) {
			throw new LearningStackException("Learning stack is empty for session: " + sessionId);
		}

		LearningStackEntry top = stack.get(stack.size() - 1);
		if (!top.getConceptId().equals(conceptId)) {
			throw new LearningStackException("Current concept does not match the top stack entry: " + conceptId);
		}

		Instant now = Instant.now();
		conceptRepository.save(current.complete());
		stackRepository.save(top.complete(now));
		sessionRepository.save(session.moveTo(top.getParentConceptId(), now));
		return getCurrentStack(sessionId);
	}

	@Transactional(readOnly = true)
	public List<LearningStackEntry> getCurrentStack(Long sessionId) {
		requireSession(sessionId);
		return stackRepository.findActiveBySessionIdOrderByStackOrder(sessionId);
	}

	private LearningSession requireSession(Long sessionId) {
		return sessionRepository.findById(sessionId)
				.orElseThrow(() -> new LearningStackException("LearningSession not found: " + sessionId));
	}

	private Concept requireConceptInSession(Long conceptId, Long sessionId) {
		Concept concept = conceptRepository.findById(conceptId)
				.orElseThrow(() -> new LearningStackException("Concept not found: " + conceptId));
		if (!concept.getSessionId().equals(sessionId)) {
			throw new LearningStackException("Concept does not belong to session: " + conceptId);
		}
		return concept;
	}
}
