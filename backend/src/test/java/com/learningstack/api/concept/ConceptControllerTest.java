package com.learningstack.api.concept;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.learningstack.application.concept.ConceptNotFoundException;
import com.learningstack.application.concept.ConceptQueryService;
import com.learningstack.application.session.LearningSessionNotFoundException;
import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;

@WebMvcTest(ConceptController.class)
class ConceptControllerTest {

	@Autowired MockMvc mockMvc;
	@MockitoBean ConceptQueryService service;

	@Test
	void getsConceptById() throws Exception {
		given(service.findById(1L)).willReturn(concept(1L, 10L, "Transaction"));

		mockMvc.perform(get("/api/concepts/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Transaction"))
				.andExpect(jsonPath("$.summary").value("Summary"))
				.andExpect(jsonPath("$.description").value("Description"))
				.andExpect(jsonPath("$.status").value("NOT_STARTED"))
				.andExpect(jsonPath("$.importance").value("CORE"))
				.andExpect(jsonPath("$.recommendedDepth").value("DEEP"));
	}

	@Test
	void listsConceptsBySession() throws Exception {
		given(service.findAllBySessionId(10L)).willReturn(List.of(
				concept(1L, 10L, "Transaction"), concept(2L, 10L, "ACID")));

		mockMvc.perform(get("/api/sessions/10/concepts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[1].name").value("ACID"));
	}

	@Test
	void listsRelationsBySession() throws Exception {
		given(service.findRelationsBySessionId(10L)).willReturn(List.of(
				new ConceptRelation(3L, 10L, 1L, 2L, RelationType.CORE, "Core concept")));

		mockMvc.perform(get("/api/sessions/10/relations"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(3))
				.andExpect(jsonPath("$[0].sourceConceptId").value(1))
				.andExpect(jsonPath("$[0].targetConceptId").value(2))
				.andExpect(jsonPath("$[0].relationType").value("CORE"))
				.andExpect(jsonPath("$[0].reason").value("Core concept"));
	}

	@Test
	void returnsNotFoundForMissingConcept() throws Exception {
		given(service.findById(404L)).willThrow(new ConceptNotFoundException(404L));

		mockMvc.perform(get("/api/concepts/404"))
				.andExpect(status().isNotFound());
	}

	@Test
	void returnsNotFoundForMissingSessionConceptList() throws Exception {
		given(service.findAllBySessionId(404L)).willThrow(new LearningSessionNotFoundException(404L));

		mockMvc.perform(get("/api/sessions/404/concepts"))
				.andExpect(status().isNotFound());
	}

	@Test
	void returnsNotFoundForMissingSessionRelationList() throws Exception {
		given(service.findRelationsBySessionId(404L)).willThrow(new LearningSessionNotFoundException(404L));

		mockMvc.perform(get("/api/sessions/404/relations"))
				.andExpect(status().isNotFound());
	}

	private Concept concept(Long id, Long sessionId, String name) {
		return new Concept(id, sessionId, name, "Summary", "Description",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.NOT_STARTED,
				Instant.parse("2026-08-26T00:00:00Z"));
	}
}
