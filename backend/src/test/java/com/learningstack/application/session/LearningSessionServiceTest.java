package com.learningstack.application.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@ExtendWith(MockitoExtension.class)
class LearningSessionServiceTest {

	@Mock
	private LearningSessionRepository sessionRepository;

	@InjectMocks
	private LearningSessionService sessionService;

	@Test
	void createsActiveSessionWithoutConceptContext() {
		given(sessionRepository.save(any(LearningSession.class)))
				.willAnswer(invocation -> invocation.getArgument(0));

		LearningSession created = sessionService.create("Spring Transaction", "Understand transactions");

		ArgumentCaptor<LearningSession> captor = ArgumentCaptor.forClass(LearningSession.class);
		verify(sessionRepository).save(captor.capture());
		assertThat(created.getStatus()).isEqualTo(SessionStatus.ACTIVE);
		assertThat(created.getRootConceptId()).isNull();
		assertThat(created.getCurrentConceptId()).isNull();
		assertThat(captor.getValue().getTitle()).isEqualTo("Spring Transaction");
	}

	@Test
	void returnsAllSessions() {
		LearningSession session = session(1L);
		given(sessionRepository.findAll()).willReturn(List.of(session));

		assertThat(sessionService.findAll()).containsExactly(session);
	}

	@Test
	void returnsSessionById() {
		LearningSession session = session(1L);
		given(sessionRepository.findById(1L)).willReturn(Optional.of(session));

		assertThat(sessionService.findById(1L)).isSameAs(session);
	}

	@Test
	void throwsWhenSessionDoesNotExist() {
		given(sessionRepository.findById(404L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> sessionService.findById(404L))
				.isInstanceOf(LearningSessionNotFoundException.class)
				.hasMessageContaining("404");
	}

	private LearningSession session(Long id) {
		Instant now = Instant.parse("2026-08-26T00:00:00Z");
		return new LearningSession(id, "Session", "Goal", null, null,
				SessionStatus.ACTIVE, now, now);
	}
}
