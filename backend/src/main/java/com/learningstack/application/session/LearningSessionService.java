package com.learningstack.application.session;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@Service
public class LearningSessionService {

	private final LearningSessionRepository sessionRepository;

	public LearningSessionService(LearningSessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Transactional
	public LearningSession create(String title, String goal) {
		Instant now = Instant.now();
		LearningSession session = new LearningSession(
				null, title, goal, null, null, SessionStatus.ACTIVE, now, now);
		return sessionRepository.save(session);
	}

	@Transactional(readOnly = true)
	public List<LearningSession> findAll() {
		return sessionRepository.findAll();
	}

	@Transactional(readOnly = true)
	public LearningSession findById(Long sessionId) {
		return sessionRepository.findById(sessionId)
				.orElseThrow(() -> new LearningSessionNotFoundException(sessionId));
	}
}
