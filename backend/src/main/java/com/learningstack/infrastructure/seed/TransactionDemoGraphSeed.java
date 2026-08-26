package com.learningstack.infrastructure.seed;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learningstack.domain.concept.Concept;
import com.learningstack.domain.concept.ConceptRelation;
import com.learningstack.domain.concept.ConceptRelationRepository;
import com.learningstack.domain.concept.ConceptRepository;
import com.learningstack.domain.concept.ConceptStatus;
import com.learningstack.domain.concept.LearningDepth;
import com.learningstack.domain.concept.RelationType;
import com.learningstack.domain.session.LearningSession;
import com.learningstack.domain.session.LearningSessionRepository;
import com.learningstack.domain.session.SessionStatus;

@Component
@Profile("dev")
@Transactional
public class TransactionDemoGraphSeed implements ApplicationRunner {

	static final String SESSION_TITLE = "Spring Transaction 이해하기";
	static final String SESSION_GOAL = "Spring Transaction의 동작 원리를 이해한다.";

	private final LearningSessionRepository sessionRepository;
	private final ConceptRepository conceptRepository;
	private final ConceptRelationRepository relationRepository;

	public TransactionDemoGraphSeed(LearningSessionRepository sessionRepository,
			ConceptRepository conceptRepository,
			ConceptRelationRepository relationRepository) {
		this.sessionRepository = sessionRepository;
		this.conceptRepository = conceptRepository;
		this.relationRepository = relationRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (demoSessionExists()) {
			return;
		}

		Instant now = Instant.now();
		LearningSession session = sessionRepository.save(new LearningSession(
				null, SESSION_TITLE, SESSION_GOAL, null, null,
				SessionStatus.ACTIVE, now, now));

		Map<String, Concept> concepts = new LinkedHashMap<>();
		concepts.put("transaction", saveConcept(session, "Transaction",
				"여러 데이터베이스 작업을 하나의 논리적 작업 단위로 묶는 개념",
				"트랜잭션의 경계와 완료 방식, 보장 특성, Spring 적용 방식을 함께 학습한다.",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.LEARNING, now));
		concepts.put("commit-rollback", saveConcept(session, "Commit / Rollback",
				"트랜잭션의 변경을 확정하거나 취소하는 동작",
				"Commit은 변경을 영구 반영하고 Rollback은 트랜잭션 시작 전 상태로 되돌린다.",
				RelationType.CORE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("transaction-boundary", saveConcept(session, "Transaction Boundary",
				"트랜잭션이 시작되고 종료되는 작업 범위",
				"여러 작업을 하나의 원자적 단위로 다루기 위해 경계를 명확히 정의한다.",
				RelationType.CORE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("acid", saveConcept(session, "ACID",
				"트랜잭션이 보장해야 하는 네 가지 핵심 특성",
				"Atomicity, Consistency, Isolation, Durability가 신뢰할 수 있는 트랜잭션을 구성한다.",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));
		concepts.put("atomicity", saveConcept(session, "Atomicity",
				"트랜잭션의 모든 작업이 전부 성공하거나 전부 취소되는 특성",
				"부분 성공 없이 하나의 작업 단위로 처리되는 원자성을 이해한다.",
				RelationType.CORE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("consistency", saveConcept(session, "Consistency",
				"트랜잭션 전후에 데이터 규칙이 유지되는 특성",
				"제약 조건과 비즈니스 규칙을 만족하는 유효한 상태 전이를 학습한다.",
				RelationType.CORE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("isolation", saveConcept(session, "Isolation",
				"동시에 실행되는 트랜잭션이 서로 간섭하지 않도록 하는 특성",
				"동시성 환경에서 트랜잭션 간 관찰 가능성과 간섭을 이해한다.",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));
		concepts.put("isolation-level", saveConcept(session, "Isolation Level",
				"트랜잭션 간 변경의 노출 수준을 결정하는 설정",
				"격리 수준에 따른 데이터 정합성과 동시성의 균형을 학습한다.",
				RelationType.CORE, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));
		concepts.put("dirty-read", saveConcept(session, "Dirty Read",
				"커밋되지 않은 다른 트랜잭션의 데이터를 읽는 현상",
				"다른 트랜잭션이 Rollback할 수 있는 값을 읽을 때 발생하는 문제를 이해한다.",
				RelationType.PREREQUISITE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("non-repeatable-read", saveConcept(session, "Non-Repeatable Read",
				"같은 행을 다시 읽었을 때 값이 달라지는 현상",
				"조회 사이에 다른 트랜잭션이 값을 변경하고 Commit할 때 발생한다.",
				RelationType.PREREQUISITE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("phantom-read", saveConcept(session, "Phantom Read",
				"같은 조건의 재조회에서 행 집합이 달라지는 현상",
				"다른 트랜잭션의 삽입 또는 삭제로 조회 결과의 행이 나타나거나 사라진다.",
				RelationType.PREREQUISITE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("lock", saveConcept(session, "Lock",
				"동시 접근을 제어하여 데이터 충돌을 방지하는 방식",
				"공유 자원 접근 순서를 제어하는 비관적 동시성 제어의 기본을 학습한다.",
				RelationType.IMPLEMENTATION, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));
		concepts.put("mvcc", saveConcept(session, "MVCC",
				"여러 버전의 데이터를 이용해 동시 읽기와 쓰기를 처리하는 방식",
				"잠금 경합을 줄이며 일관된 스냅샷을 제공하는 내부 원리를 살펴본다.",
				RelationType.ADVANCED, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));
		concepts.put("durability", saveConcept(session, "Durability",
				"커밋된 결과가 장애 이후에도 유지되는 특성",
				"로그와 영속 저장소가 확정된 변경을 보존하는 원리를 이해한다.",
				RelationType.CORE, LearningDepth.UNDERSTAND, ConceptStatus.NOT_STARTED, now));
		concepts.put("spring-transactional", saveConcept(session, "Spring @Transactional",
				"Spring에서 선언적으로 트랜잭션 경계를 설정하는 기능",
				"애노테이션과 프록시 기반 트랜잭션 관리가 애플리케이션 코드에 적용되는 방식을 학습한다.",
				RelationType.IMPLEMENTATION, LearningDepth.DEEP, ConceptStatus.NOT_STARTED, now));

		saveRelation(session, concepts, "transaction", "commit-rollback", RelationType.CORE,
				"트랜잭션의 완료와 취소 방식을 이해하기 위한 핵심 개념");
		saveRelation(session, concepts, "transaction", "transaction-boundary", RelationType.CORE,
				"트랜잭션이 적용되는 범위를 이해하기 위한 핵심 개념");
		saveRelation(session, concepts, "transaction", "acid", RelationType.CORE,
				"트랜잭션이 제공하는 보장을 이해하기 위한 핵심 개념");
		saveRelation(session, concepts, "acid", "atomicity", RelationType.CORE, "ACID의 원자성");
		saveRelation(session, concepts, "acid", "consistency", RelationType.CORE, "ACID의 일관성");
		saveRelation(session, concepts, "acid", "isolation", RelationType.CORE, "ACID의 격리성");
		saveRelation(session, concepts, "isolation", "isolation-level", RelationType.CORE,
				"격리성을 구체적으로 조절하는 수준");
		saveRelation(session, concepts, "isolation-level", "dirty-read", RelationType.PREREQUISITE,
				"격리 수준이 방지하는 동시성 이상 현상");
		saveRelation(session, concepts, "isolation-level", "non-repeatable-read", RelationType.PREREQUISITE,
				"격리 수준이 방지하는 동시성 이상 현상");
		saveRelation(session, concepts, "isolation-level", "phantom-read", RelationType.PREREQUISITE,
				"격리 수준이 방지하는 동시성 이상 현상");
		saveRelation(session, concepts, "isolation-level", "lock", RelationType.IMPLEMENTATION,
				"격리 수준을 구현하는 동시성 제어 방식");
		saveRelation(session, concepts, "lock", "mvcc", RelationType.ADVANCED,
				"잠금 경합을 줄이는 심화 동시성 제어 방식");
		saveRelation(session, concepts, "acid", "durability", RelationType.CORE, "ACID의 지속성");
		saveRelation(session, concepts, "transaction", "spring-transactional", RelationType.IMPLEMENTATION,
				"Spring 애플리케이션에 트랜잭션을 적용하는 방법");

		Concept root = concepts.get("transaction");
		sessionRepository.save(new LearningSession(
				session.getId(), session.getTitle(), session.getGoal(), root.getId(), root.getId(),
				session.getStatus(), session.getCreatedAt(), now));
	}

	private boolean demoSessionExists() {
		return sessionRepository.findAll().stream()
				.anyMatch(session -> SESSION_TITLE.equals(session.getTitle())
						&& SESSION_GOAL.equals(session.getGoal()));
	}

	private Concept saveConcept(LearningSession session, String name, String summary,
			String description, RelationType importance, LearningDepth depth,
			ConceptStatus status, Instant createdAt) {
		return conceptRepository.save(new Concept(
				null, session.getId(), name, summary, description,
				importance, depth, status, createdAt));
	}

	private void saveRelation(LearningSession session, Map<String, Concept> concepts,
			String sourceKey, String targetKey, RelationType type, String reason) {
		relationRepository.save(new ConceptRelation(
				null, session.getId(), concepts.get(sourceKey).getId(), concepts.get(targetKey).getId(),
				type, reason));
	}
}
