# Learning Stack
## AI 기반 개념 학습 내비게이션 서비스

**문서 버전:** MVP v0.1  
**목적:** Codex 기반 웹서비스 구현을 위한 제품 및 기술 명세

---

# 1. 서비스 개요

## 1.1 한 줄 정의

> 하나의 개념을 공부하다 만나는 선행·연관 개념을 시각적으로 탐색하고, 필요한 만큼 학습한 뒤 원래 학습 지점으로 돌아갈 수 있게 해주는 AI 기반 학습 내비게이션 서비스.

---

# 2. 해결하려는 문제

복잡한 개념을 공부하면 하나의 개념만으로 학습이 끝나지 않는다.

예를 들어 `Transaction`을 공부한다고 하면 다음과 같은 흐름이 발생한다.

```text
Transaction
↓
ACID
↓
Isolation
↓
Isolation Level
↓
Lock
↓
MVCC
```

문제는 새로운 개념을 발견하는 것 자체가 아니다.

## 문제 A. Rabbit Hole

```text
A를 이해하려면 B가 필요함
↓
B를 이해하려면 C가 필요함
↓
C를 이해하려면 D가 필요함
↓
...
```

공부 범위가 끝없이 확장된다.

## 문제 B. Context Loss

하위 개념을 공부하고 나면

```text
"내가 이걸 왜 공부하고 있었지?"
```

라는 문제가 발생한다.

## 문제 C. Depth 판단 실패

모르는 개념을 발견했을 때

```text
이걸 지금 반드시 알아야 하나?
어디까지 공부해야 하나?
나중에 공부해도 되는 내용인가?
```

를 판단하기 어렵다.

---

# 3. 제품이 제공해야 하는 핵심 가치

Learning Stack은 단순한 Knowledge Graph 서비스가 아니다.

기존 지식 그래프:

```text
A ─ B ─ C ─ D ─ E
```

Learning Stack:

```text
목표 설정
↓
필요 개념 탐색
↓
현재 학습 위치 기록
↓
필요한 깊이까지만 학습
↓
하위 개념 완료
↓
이전 학습 위치로 복귀
```

서비스의 핵심은 다음 질문에 답하는 것이다.

> **내 목표를 이해하기 위해 지금 무엇을 공부해야 하고, 어디까지 공부해야 하며, 그다음 어디로 돌아가야 하는가?**

---

# 4. 핵심 제품 원칙

## 4.1 Goal First

모든 학습은 하나의 `Learning Goal`에서 시작한다.

예:

```text
Spring에서 Transaction이 어떻게 동작하는지 이해한다.
```

AI는 해당 목표를 기준으로 필요한 개념만 추출한다.

---

## 4.2 Dependency First

각 개념 사이의 관계를 단순 연결이 아니라 의미 있는 관계로 관리한다.

관계 유형:

```text
PREREQUISITE
CORE
IMPLEMENTATION
ADVANCED
RELATED
```

사용자 표시:

| 유형 | 의미 |
|---|---|
| 🔴 선행 | 이해하기 위해 먼저 알아야 함 |
| 🔵 핵심 | 현재 주제의 중심 개념 |
| 🟡 구현 | 실제 적용 과정에서 필요 |
| ⚪ 심화 | 기본 학습 후 공부 가능 |
| ⚪ 관련 | 알아두면 좋지만 필수 아님 |

---

## 4.3 Depth Control

모든 개념을 동일한 깊이로 공부하지 않는다.

각 노드에는 학습 깊이를 지정한다.

```text
1 = 용어만 알기
2 = 개념 이해
3 = 설명 가능
4 = 직접 적용 가능
5 = 내부 원리까지 이해
```

MVP UI에서는 단순화하여 다음 세 단계만 노출한다.

```text
가볍게 보기
개념 이해
깊게 공부
```

---

## 4.4 Learning Stack

하위 개념으로 이동할 때 현재 위치를 Stack에 저장한다.

```text
Transaction
↓
ACID
↓
Isolation
↓
Lock
```

현재 상태:

```text
┌─────────────────┐
│ Lock            │ ← 현재
├─────────────────┤
│ Isolation       │
├─────────────────┤
│ ACID            │
├─────────────────┤
│ Transaction     │
└─────────────────┘
```

Lock 학습 완료:

```text
POP Lock
↓
Isolation으로 복귀
```

---

## 4.5 Return Point

각 하위 학습에는 반드시 다음 정보가 존재한다.

```text
왜 이 개념을 공부하게 되었는가?
어떤 개념에서 출발했는가?
완료 후 어디로 돌아가야 하는가?
```

예:

```text
현재: MVCC

학습 이유:
Isolation Level에서 동시성 제어 원리를 이해하기 위해

Return Point:
Isolation Level
```

---

# 5. 주요 사용자

## Primary Persona

복잡한 기술 개념을 체계적으로 공부해야 하는 개발 학습자.

대표 상황:

- Java / Spring
- Database
- Network
- Operating System
- Computer Science
- AI
- Cloud

초기 MVP는 개발 학습을 중심으로 설계하되 특정 분야에 종속되지 않는다.

---

# 6. MVP 핵심 사용자 흐름

## Flow 1. 새로운 학습 시작

사용자:

```text
트랜잭션을 공부하고 싶다.
```

시스템:

```text
학습 목표 분석
↓
핵심 개념 추출
↓
Knowledge Graph 생성
↓
추천 Learning Path 생성
```

결과:

```text
Transaction
├─ Commit / Rollback
├─ ACID
│   └─ Isolation
│       └─ Isolation Level
├─ Transaction Boundary
└─ @Transactional
```

---

# 7. 메인 화면

## 7.1 화면 구성

Desktop 기준:

```text
┌────────────────────────────────────────────┐
│ Learning Goal                             │
│ Spring Transaction 이해하기              │
├───────────────────┬────────────────────────┤
│                   │                        │
│ Knowledge Graph   │ Current Concept        │
│                   │                        │
│                   │ Isolation Level        │
│                   │                        │
│                   │ 왜 필요한가            │
│                   │ 알아야 할 것           │
│                   │ 지금 몰라도 되는 것    │
│                   │                        │
├───────────────────┴────────────────────────┤
│ Learning Stack                            │
│ Transaction > ACID > Isolation > ...      │
└────────────────────────────────────────────┘
```

---

# 8. Knowledge Graph

## 8.1 Node

Node 기본 데이터:

```json
{
  "id": "concept-001",
  "name": "Transaction",
  "summary": "여러 데이터베이스 작업을 하나의 논리적 작업 단위로 묶는 개념",
  "importance": "CORE",
  "depth": 3,
  "status": "LEARNING"
}
```

---

## 8.2 Edge

```json
{
  "source": "transaction",
  "target": "acid",
  "relation": "CORE",
  "reason": "트랜잭션의 핵심 특성을 설명하는 개념"
}
```

---

# 9. Node 상태

```text
NOT_STARTED
LEARNING
PAUSED
COMPLETED
SKIPPED
BOOKMARKED
```

UI 예:

```text
○ 미학습
◉ 학습 중
✓ 완료
→ 나중에
```

---

# 10. Concept Detail

노드를 클릭하면 오른쪽 패널에서 다음 정보를 표시한다.

## 기본 정보

```text
Isolation Level
```

## 한 줄 설명

```text
동시에 실행되는 트랜잭션이 서로의 작업을 어느 정도까지 볼 수 있는지를 결정한다.
```

## 왜 알아야 하는가

```text
동시성 환경에서 데이터 정합성과 성능의 균형을 이해하기 위해 필요하다.
```

## 현재 목표에서 필요한 수준

```text
[ 개념 이해 ]
```

## 지금 알아야 할 것

```text
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
Dirty Read
Non-Repeatable Read
Phantom Read
```

## 지금 몰라도 되는 것

```text
Gap Lock
Next-Key Lock
PostgreSQL SSI 구현
InnoDB Lock 내부 자료구조
```

---

# 11. 학습 시작

사용자가 특정 노드에서

```text
[이 개념 공부하기]
```

를 선택하면 현재 노드를 Stack에 Push한다.

Before:

```text
Transaction
ACID
Isolation
```

사용자가 `Lock` 선택:

```text
Transaction
ACID
Isolation
Lock
```

Lock이 Current Node가 된다.

---

# 12. 학습 완료

Current Node에서:

```text
[학습 완료]
```

선택.

시스템:

```text
현재 Node → COMPLETED
↓
Stack POP
↓
Parent Node로 복귀
```

화면:

```text
✓ Lock 학습 완료

Isolation으로 돌아갑니다.
```

---

# 13. Later 기능

심화 개념을 클릭했을 경우:

```text
MVCC

현재 학습 목표에서는 심화 개념입니다.

Transaction의 기본 이해를 위해
지금 반드시 공부할 필요는 없습니다.

[그래도 학습]
[나중에 보기]
```

`나중에 보기` 선택 시:

```text
status = BOOKMARKED
```

Learning Stack에는 Push하지 않는다.

---

# 14. Learning Stack 데이터 구조

```json
[
  {
    "conceptId": "transaction",
    "enteredFrom": null
  },
  {
    "conceptId": "acid",
    "enteredFrom": "transaction"
  },
  {
    "conceptId": "isolation",
    "enteredFrom": "acid"
  }
]
```

현재 노드:

```text
stack[last]
```

---

# 15. Learning Session

하나의 학습 목표 단위를 `LearningSession`으로 관리한다.

예:

```json
{
  "id": "session-001",
  "title": "Spring Transaction 이해하기",
  "goal": "Spring Backend에서 Transaction의 동작 원리를 이해한다.",
  "rootConceptId": "transaction",
  "currentConceptId": "isolation",
  "createdAt": "..."
}
```

---

# 16. 핵심 데이터 모델

## User

```text
id
email
name
created_at
```

## LearningSession

```text
id
user_id
title
goal
root_concept_id
current_concept_id
created_at
updated_at
```

## Concept

```text
id
session_id
name
summary
description
importance
recommended_depth
status
created_at
```

## ConceptRelation

```text
id
session_id
source_concept_id
target_concept_id
relation_type
reason
```

## LearningStack

```text
id
session_id
concept_id
parent_concept_id
stack_order
entered_at
completed_at
```

## Bookmark

```text
id
session_id
concept_id
created_at
```

---

# 17. ERD

```text
User
  │
  └── LearningSession
          │
          ├── Concept
          │      │
          │      └── ConceptRelation
          │
          ├── LearningStack
          │
          └── Bookmark
```

---

# 18. AI의 역할

AI는 콘텐츠 생성자가 아니라 **학습 구조 설계자** 역할을 한다.

AI가 담당할 기능:

```text
1. 학습 목표 분석
2. 핵심 개념 추출
3. 선행 개념 추출
4. 개념 관계 분류
5. 학습 깊이 판단
6. 지금 공부할 내용 선정
7. 나중에 볼 내용 선정
8. 개념 설명 생성
```

---

# 19. AI Graph 생성 요청

입력:

```json
{
  "goal": "Spring에서 Transaction이 어떻게 동작하는지 이해하고 싶다."
}
```

AI 출력:

```json
{
  "title": "Spring Transaction 이해",
  "rootConcept": "Transaction",
  "concepts": [
    {
      "key": "transaction",
      "name": "Transaction",
      "summary": "여러 DB 작업을 하나의 논리적 작업 단위로 묶는 개념",
      "importance": "CORE",
      "depth": 3
    },
    {
      "key": "acid",
      "name": "ACID",
      "summary": "트랜잭션이 보장해야 하는 네 가지 특성",
      "importance": "CORE",
      "depth": 3
    }
  ],
  "relations": [
    {
      "source": "transaction",
      "target": "acid",
      "type": "CORE",
      "reason": "Transaction의 특성을 이해하기 위한 핵심 개념"
    }
  ]
}
```

---

# 20. AI 출력 규칙

AI 응답은 반드시 JSON Schema를 사용한다.

그래프 생성 시 최대 Node:

```text
초기: 8개
최대: 15개
```

한 번에 지나치게 큰 그래프를 생성하지 않는다.

중요 원칙:

> 그래프는 전체 지식 세계를 표현하는 것이 아니라 현재 목표를 달성하기 위한 최소한의 학습 지도를 표현한다.

---

# 21. Progressive Expansion

초기 그래프:

```text
Transaction
├─ ACID
├─ Commit/Rollback
├─ Boundary
└─ Spring Transaction
```

사용자가 Isolation을 선택:

```text
Isolation
├─ Isolation Level
├─ Concurrent Transaction
└─ Lock
```

필요할 때만 하위 Graph를 확장한다.

이를 통해 Graph Explosion을 막는다.

---

# 22. AI Concept Expansion API

요청:

```http
POST /api/concepts/{conceptId}/expand
```

Request:

```json
{
  "sessionId": 1
}
```

AI Context:

```text
전체 Learning Goal
현재 Concept
상위 Concept
현재 Stack
이미 생성된 Concept
```

AI가 이미 존재하는 개념을 중복 생성하지 않도록 한다.

---

# 23. REST API

## Session

### 새로운 학습 생성

```http
POST /api/sessions
```

```json
{
  "goal": "Spring Transaction 이해하기"
}
```

---

### 학습 목록

```http
GET /api/sessions
```

---

### 학습 상세

```http
GET /api/sessions/{sessionId}
```

---

# 24. Concept API

### Concept 조회

```http
GET /api/concepts/{conceptId}
```

### Concept 확장

```http
POST /api/concepts/{conceptId}/expand
```

### 학습 시작

```http
POST /api/concepts/{conceptId}/start
```

### 학습 완료

```http
POST /api/concepts/{conceptId}/complete
```

### 나중에 보기

```http
POST /api/concepts/{conceptId}/bookmark
```

---

# 25. Stack API

현재 Learning Stack:

```http
GET /api/sessions/{sessionId}/stack
```

응답:

```json
{
  "items": [
    {
      "conceptId": 1,
      "name": "Transaction"
    },
    {
      "conceptId": 2,
      "name": "ACID"
    },
    {
      "conceptId": 3,
      "name": "Isolation"
    }
  ],
  "current": {
    "conceptId": 3,
    "name": "Isolation"
  }
}
```

---

# 26. Backend 권장 기술 스택

```text
Java 21
Spring Boot 3
Spring Web
Spring Data JPA
Spring Security
PostgreSQL
Flyway
OpenAI API
Gradle
```

초기 인증 구현이 부담되면 MVP에서는 사용자 인증을 제외하고 Single User로 개발해도 된다.

---

# 27. Frontend 권장 기술 스택

```text
React
TypeScript
Vite
React Flow
TanStack Query
Zustand
Tailwind CSS
```

Graph UI:

```text
React Flow
```

사용 이유:

- Node 기반 UI
- Edge 표현
- Zoom / Pan
- Node Click
- Custom Node
- Dynamic Graph Expansion

지원이 용이하다.

---

# 28. 전체 Architecture

```text
┌──────────────────────┐
│ React Frontend       │
│ React Flow           │
└──────────┬───────────┘
           │ REST
           ▼
┌──────────────────────┐
│ Spring Boot          │
│                      │
│ Session Service      │
│ Concept Service      │
│ LearningStackService │
│ AI Service           │
└──────────┬───────────┘
           │
      ┌────┴─────┐
      ▼          ▼
 PostgreSQL   OpenAI API
```

---

# 29. Backend Layer

```text
Controller
    ↓
Application Service
    ↓
Domain
    ↓
Repository
```

주요 Service:

```text
LearningSessionService
ConceptService
ConceptGraphService
LearningStackService
AIConceptService
BookmarkService
```

---

# 30. 핵심 비즈니스 로직

## startConcept()

```text
1. 현재 Session 조회
2. 현재 Concept 조회
3. 대상 Concept 조회
4. LearningStack PUSH
5. currentConcept 변경
6. Concept 상태 LEARNING 변경
```

---

## completeConcept()

```text
1. 현재 Concept COMPLETED
2. Stack POP
3. Parent Concept 조회
4. currentConcept = Parent
5. Session 저장
```

Root Concept인 경우:

```text
Session COMPLETED
```

---

# 31. 중복 Concept 처리

AI가 다음을 생성할 수 있다.

```text
Transaction Isolation
Isolation
Isolation Level
```

의미가 중복될 가능성이 있다.

MVP에서는 AI에게 기존 Concept 목록을 전달하여 중복을 최소화한다.

추후:

```text
Embedding
+
Semantic Similarity
```

도입 가능.

MVP에서는 구현하지 않는다.

---

# 32. Graph Layout

권장 방향:

```text
Root
 ↓
Level 1
 ↓
Level 2
```

기본 방향:

```text
Top → Bottom
```

Node 상태 표현:

```text
미학습
학습 중
완료
나중에 보기
```

Node Border 또는 Badge로 구분한다.

---

# 33. 그래프에서 강조할 요소

현재 노드는 가장 강하게 강조한다.

예:

```text
            Transaction ✓
                 │
               ACID ✓
                 │
             Isolation ◉
               /     \
          Lock ○    MVCC →
```

범례:

```text
✓ 완료
◉ 현재
○ 미학습
→ 나중에
```

---

# 34. Breadcrumb

화면 상단에 항상 표시한다.

```text
Transaction
>
ACID
>
Isolation
>
Isolation Level
```

각 Breadcrumb 클릭 시 이전 Concept으로 이동할 수 있다.

단, 단순 화면 이동과 Learning Stack 변경은 구분한다.

---

# 35. Navigation과 Learning Stack의 차이

Graph 탐색:

```text
단순 확인
```

Learning Start:

```text
실제 학습 Context 변경
Stack PUSH
```

따라서 Node Click만으로 Stack에 추가하지 않는다.

Node Click:

```text
Detail 표시
```

사용자가:

```text
[이 개념 공부하기]
```

버튼을 눌러야 Stack Push.

---

# 36. MVP 화면

MVP에서는 4개 화면만 구현한다.

## Screen 1

```text
Home
```

기능:

```text
학습 목표 입력
기존 학습 Session 목록
```

---

## Screen 2

```text
Learning Workspace
```

핵심 화면.

구성:

```text
Graph
Concept Detail
Learning Stack
```

---

## Screen 3

```text
Bookmarks
```

나중에 공부할 Concept 목록.

---

## Screen 4

```text
Completed Sessions
```

완료된 학습 기록.

---

# 37. Home

```text
무엇을 공부하고 싶나요?

[ Spring Transaction이 어떻게 동작하는지 이해하고 싶다 ]

                    [학습 지도 만들기]
```

아래:

```text
최근 학습

Spring Transaction 이해하기
진행률 42%

JPA Persistence Context
진행률 18%
```

---

# 38. Workspace 상세 Layout

```text
┌────────────────────────────────────────────────────┐
│ Spring Transaction 이해하기                       │
│ Transaction > ACID > Isolation                    │
├─────────────────────────┬──────────────────────────┤
│                         │ Isolation                │
│                         │                          │
│      Knowledge Graph    │ 동시 트랜잭션의...      │
│                         │                          │
│                         │ 중요도: 핵심             │
│                         │ 깊이: 개념 이해          │
│                         │                          │
│                         │ [공부하기]               │
│                         │ [나중에 보기]            │
├─────────────────────────┴──────────────────────────┤
│ Learning Stack                                    │
│ Transaction > ACID > Isolation                    │
└────────────────────────────────────────────────────┘
```

---

# 39. 진행률

단순 계산:

```text
completed core concepts
/
total core concepts
```

심화 및 Related Concept은 진행률에서 제외한다.

---

# 40. MVP에서 하지 않을 것

초기 버전에 다음 기능은 넣지 않는다.

```text
사용자 간 Graph 공유
실시간 협업
커뮤니티
Quiz 자동 생성
Flash Card
영상 강의
PDF 업로드
RAG
Vector DB
노트 에디터
Obsidian 연동
GitHub 연동
Gamification
추천 알고리즘
모바일 앱
```

핵심 기능을 먼저 검증한다.

---

# 41. MVP 검증 질문

서비스가 해결해야 하는 핵심 질문:

> 하위 개념을 공부한 뒤 원래 학습 Context로 돌아오는 데 실제 도움이 되는가?

보조 질문:

```text
그래프가 현재 학습 위치를 이해하는 데 도움이 되는가?

AI가 추천한 학습 깊이가 적절한가?

"나중에 보기" 기능이 과도한 Rabbit Hole을 줄이는가?

Learning Stack이 현재 학습 목적을 기억하는 데 도움이 되는가?
```

---

# 42. MVP 성공 기준

사용자가 다음 흐름을 문제없이 수행할 수 있어야 한다.

```text
학습 Goal 생성
↓
AI Graph 생성
↓
Concept 선택
↓
학습 시작
↓
하위 Concept 발견
↓
하위 Concept 학습
↓
완료
↓
기존 Concept으로 복귀
```

이 흐름이 성공하면 MVP의 핵심 기능은 완성된 것으로 본다.

---

# 43. 주요 Acceptance Criteria

## AC-01

사용자는 자연어로 학습 목표를 입력할 수 있다.

---

## AC-02

시스템은 목표에 대한 Concept Graph를 생성한다.

---

## AC-03

각 Concept에는 다음 정보가 존재한다.

```text
이름
간단 설명
중요도
추천 학습 깊이
왜 필요한가
```

---

## AC-04

사용자는 Concept을 클릭하여 상세 정보를 볼 수 있다.

---

## AC-05

사용자는 Concept을 학습 시작할 수 있다.

---

## AC-06

학습 시작 시 해당 Concept이 Learning Stack에 추가된다.

---

## AC-07

사용자가 완료 버튼을 누르면 Concept이 완료 처리된다.

---

## AC-08

완료된 Concept은 Stack에서 제거되고 Parent Concept으로 돌아간다.

---

## AC-09

사용자는 심화 Concept을 `나중에 보기`로 저장할 수 있다.

---

## AC-10

사용자가 브라우저를 종료한 후 다시 접속하더라도 현재 Session과 Stack이 유지된다.

---

# 44. 개발 우선순위

## Phase 1 — 기본 Domain

AI 없이 구현.

```text
LearningSession
Concept
ConceptRelation
LearningStack
```

테스트용 Transaction Graph를 JSON으로 넣는다.

목표:

> Learning Stack과 Return UX 검증

---

## Phase 2 — Graph UI

React Flow 구현.

```text
Node 표시
Edge 표시
Node 상태
Node Click
Current Node 강조
```

---

## Phase 3 — Learning Navigation

```text
공부하기
Stack Push
학습 완료
Stack Pop
Parent 복귀
Bookmark
```

이 단계가 MVP의 핵심이다.

---

## Phase 4 — AI Graph Generation

OpenAI API 연결.

```text
Goal
↓
Concept
↓
Relations
↓
Graph
```

Structured Output을 사용한다.

---

## Phase 5 — Dynamic Expansion

Node의:

```text
[하위 개념 더 보기]
```

를 구현한다.

AI가 선택된 Concept을 기준으로 추가 Concept을 생성한다.

---

## Phase 6 — UX 개선

```text
Breadcrumb
Progress
Graph Focus
Current Node 강조
Animation
Empty State
Error State
Loading State
```

---

# 45. Codex 구현 순서

Codex에게 전체 서비스를 한 번에 구현시키지 않는다.

아래 Task 단위로 진행한다.

## Task 1

```text
Spring Boot 프로젝트와 React 프로젝트의 기본 구조를 생성한다.
PostgreSQL과 연결한다.
```

## Task 2

```text
LearningSession, Concept, ConceptRelation,
LearningStack Entity와 Repository를 구현한다.
```

## Task 3

```text
테스트용 Transaction Knowledge Graph를 생성하고
REST API로 반환한다.
```

## Task 4

```text
React Flow를 사용하여 Graph UI를 구현한다.
```

## Task 5

```text
Concept 선택 시 Detail Panel을 구현한다.
```

## Task 6

```text
Learning Stack Push / Pop 로직을 구현한다.
```

## Task 7

```text
학습 완료 후 Parent Concept으로 복귀하는 기능을 구현한다.
```

## Task 8

```text
Bookmark 기능을 구현한다.
```

## Task 9

```text
OpenAI API를 연결하고 Learning Goal로부터
Concept Graph를 생성한다.
```

## Task 10

```text
Concept Dynamic Expansion 기능을 구현한다.
```

---

# 46. Codex 개발 원칙

Codex는 다음 원칙을 따라 구현한다.

## 코드 구조

```text
기능 단위로 작은 변경을 수행한다.
```

## 매 Task마다

```text
1. 현재 코드 확인
2. 구현 계획 작성
3. 코드 수정
4. 테스트 실행
5. 결과 요약
```

## 금지

```text
요구되지 않은 기능 추가
불필요한 추상화
MVP 단계의 Microservice
과도한 디자인 패턴
AI 호출 로직과 Domain 로직 혼합
```

---

# 47. Repository 구조 예시

```text
learning-stack/
│
├── backend/
│   ├── src/main/java/...
│   │
│   ├── domain/
│   │   ├── session/
│   │   ├── concept/
│   │   └── learning/
│   │
│   ├── application/
│   │
│   ├── infrastructure/
│   │
│   └── api/
│
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   ├── features/
│   │   │   ├── graph/
│   │   │   ├── concept/
│   │   │   └── learning-stack/
│   │   ├── components/
│   │   └── api/
│
├── docker-compose.yml
└── README.md
```

---

# 48. 초기 Demo Data

첫 개발 단계에서는 AI 없이 아래 데이터를 사용한다.

```text
Transaction
├── Commit / Rollback
├── Transaction Boundary
├── ACID
│   ├── Atomicity
│   ├── Consistency
│   ├── Isolation
│   │   └── Isolation Level
│   │       ├── Dirty Read
│   │       ├── Non-Repeatable Read
│   │       ├── Phantom Read
│   │       └── Lock
│   │           └── MVCC
│   └── Durability
└── Spring @Transactional
```

Depth:

```text
Transaction                깊게 공부
Commit / Rollback          깊게 공부
Transaction Boundary       깊게 공부
ACID                       깊게 공부
Atomicity                  개념 이해
Consistency                가볍게 보기
Isolation                  깊게 공부
Isolation Level            개념 이해
Dirty Read                 개념 이해
Non-Repeatable Read        개념 이해
Phantom Read               개념 이해
Lock                       개념 이해
MVCC                       가볍게 보기
Durability                 가볍게 보기
@Transactional             깊게 공부
```

---

# 49. 가장 중요한 UX 검증 시나리오

사용자가 Transaction을 공부한다.

```text
Transaction
```

ACID가 이해되지 않는다.

```text
Transaction
→ ACID
```

Isolation이 이해되지 않는다.

```text
Transaction
→ ACID
→ Isolation
```

Lock이 궁금하다.

```text
Transaction
→ ACID
→ Isolation
→ Lock
```

Lock 학습 완료.

시스템:

```text
✓ Lock 학습 완료

Isolation을 계속 공부하세요.

[Isolation으로 돌아가기]
```

Isolation 완료.

```text
✓ Isolation

ACID로 돌아가기
```

ACID 완료.

```text
✓ ACID

Transaction으로 돌아가기
```

최종적으로 사용자는 처음의 학습 Context를 잃지 않는다.

---

# 50. 제품의 핵심 차별점

이 서비스의 핵심은 Knowledge Graph가 아니다.

Knowledge Graph는 표현 방식이다.

실제 핵심 기능은:

```text
Learning Goal
+
Dependency Graph
+
Depth Control
+
Learning Stack
+
Return Point
```

이다.

즉,

> **지식의 관계를 보여주는 서비스가 아니라, 복잡한 지식 속에서 사용자가 길을 잃지 않도록 학습 경로와 복귀 지점을 관리하는 서비스**

로 정의한다.

---

# 51. MVP 최우선 개발 목표

첫 번째 버전에서는 AI 품질보다 다음 UX가 먼저 완성되어야 한다.

```text
A 공부
↓
B가 필요함
↓
B 공부
↓
C가 필요함
↓
C 공부 완료
↓
B로 복귀
↓
B 공부 완료
↓
A로 복귀
```

이 경험이 자연스럽게 동작한다면 Learning Stack의 핵심 가설을 검증할 수 있다.

반대로 이 경험이 불편하다면 아무리 좋은 AI나 Knowledge Graph를 붙여도 제품의 핵심 문제를 해결하지 못한다.

따라서 개발 우선순위는 반드시 다음과 같이 유지한다.

```text
1. Learning Stack
2. Return Point
3. Knowledge Graph UX
4. Depth Control
5. AI Graph Generation
6. 부가 기능
```