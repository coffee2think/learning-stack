package com.learningstack.api.session;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.learningstack.application.session.LearningSessionNotFoundException;
import com.learningstack.application.session.LearningSessionService;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.SessionStatus;

@WebMvcTest(LearningSessionController.class)
class LearningSessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LearningSessionService sessionService;

	@Test
	void createsSession() throws Exception {
		given(sessionService.create(anyString(), anyString())).willReturn(session(1L, "Spring Transaction"));

		mockMvc.perform(post("/api/sessions")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
							{
							  "title": "Spring Transaction",
							  "goal": "Understand transactions"
							}
							"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Spring Transaction"))
				.andExpect(jsonPath("$.status").value("ACTIVE"))
				.andExpect(jsonPath("$.rootConceptId").doesNotExist())
				.andExpect(jsonPath("$.currentConceptId").doesNotExist());
	}

	@Test
	void listsSessions() throws Exception {
		given(sessionService.findAll()).willReturn(List.of(
				session(1L, "First"),
				session(2L, "Second")));

		mockMvc.perform(get("/api/sessions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[1].id").value(2));
	}

	@Test
	void getsSessionById() throws Exception {
		given(sessionService.findById(7L)).willReturn(session(7L, "Selected"));

		mockMvc.perform(get("/api/sessions/7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.title").value("Selected"));
	}

	@Test
	void returnsNotFoundForMissingSession() throws Exception {
		given(sessionService.findById(404L)).willThrow(new LearningSessionNotFoundException(404L));

		mockMvc.perform(get("/api/sessions/404"))
				.andExpect(status().isNotFound());
	}

	private LearningSession session(Long id, String title) {
		Instant now = Instant.parse("2026-08-26T00:00:00Z");
		return new LearningSession(id, title, "Goal", null, null,
				SessionStatus.ACTIVE, now, now);
	}
}
