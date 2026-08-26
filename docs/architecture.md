# Learning Stack Architecture

## 1. 문서 목적

이 문서는 Learning Stack의 MVP 기술 선택과 구현 경계를 기록한다.
제품 동작은 `product-spec.md`, 작업 범위와 순서는
`implementation-checklist.md`를 따른다.

## 2. 아키텍처 원칙

- MVP는 하나의 Backend와 하나의 Frontend로 구성한다.
- Backend는 API, Application, Domain, Infrastructure 책임을 분리한다.
- Learning Stack은 실제 사용자가 진입한 경로를 저장하며,
  `ConceptRelation`만으로 복귀 경로를 추론하지 않는다.
- Graph Node 선택과 학습 시작을 분리한다. Node 선택은 조회 상태만 바꾸고,
  명시적인 학습 시작 동작만 Learning Stack을 변경한다.
- Bookmark는 Learning Stack과 현재 학습 Context를 변경하지 않는다.
- Core MVP가 검증되기 전에는 OpenAI 의존 기능을 구현하지 않는다.

## 3. 기술 선택 검토 결과

TASK 00의 기본 기술 조합은 MVP 요구사항에 적합하며 상호 호환 가능하다.

- Java 21은 장기 지원 버전이며 Spring Boot 3 기반 Backend에 적합하다.
- Spring Data JPA, PostgreSQL, Flyway 조합은 관계형 Domain과 명시적인
  Schema Migration 요구에 적합하다.
- React, TypeScript, Vite 조합은 단일 페이지 MVP를 작게 시작하기에 적합하다.
- React Flow는 Node/Edge, Zoom, Pan, Custom Node를 제공하므로 Graph 탐색 UI에
  적합하다. 구현 시 현재 공식 NPM 패키지명인 `@xyflow/react`를 사용한다.
- TanStack Query는 REST 기반 Server State를, Zustand는 선택된 Node 같은
  Client State만 담당하도록 역할을 분리한다.
- Tailwind CSS는 Vite와 함께 사용할 수 있다. 초기화 시 현재 공식 Vite
  Plugin인 `@tailwindcss/vite` 사용을 기본으로 한다.
- OpenAI API는 후속 AI 단계에 적합하지만 Core MVP의 런타임 의존성에는
  포함하지 않는다.

검토 결과 기술 교체가 필요한 항목은 없다. 각 라이브러리의 정확한 버전은
해당 초기화 TASK에서 당시의 안정 버전과 공식 호환 범위를 확인해 고정한다.

## 4. 확정 기술 스택

### Backend

| 영역 | 선택 | 비고 |
|---|---|---|
| Language | Java 21 | LTS |
| Framework | Spring Boot 3 | Spring Web 사용 |
| Build | Gradle | Gradle Wrapper 포함 |
| ORM | Spring Data JPA | Domain 규칙은 Entity/Repository 밖의 Application/Domain 계층에도 명확히 둔다. |
| Database | PostgreSQL | 단일 관계형 Database |
| Migration | Flyway | 모든 Schema 변경을 Migration으로 관리 |
| API | REST | JSON 기반 Frontend 통신 |

Spring Security는 인증을 제외하는 MVP 범위에서는 도입하지 않는다. 인증이
요구되는 후속 작업에서 별도로 결정한다.

### Frontend

| 영역 | 선택 | 비고 |
|---|---|---|
| UI | React + TypeScript | Component 기반 SPA |
| Build/Dev Server | Vite | React TypeScript 구성 |
| Graph | React Flow (`@xyflow/react`) | Graph 표현 및 탐색 |
| Server State | TanStack Query (`@tanstack/react-query`) | API 조회, Cache, Mutation |
| Client State | Zustand | Server에 속하지 않는 UI 상태만 관리 |
| Styling | Tailwind CSS | Vite Plugin 방식 |

### AI

| 영역 | 선택 | 도입 시점 |
|---|---|---|
| AI Provider | OpenAI API | Core MVP 완료 후 AI Phase |

AI 연동은 추상화된 Interface 뒤에 두어 Domain/Application 로직이 OpenAI SDK에
직접 의존하지 않게 한다.

## 5. MVP 운영 범위

- 인증 기능을 제외한다.
- Single User 기준으로 개발한다.
- Microservice를 도입하지 않는다.
- Core MVP에서는 Seed Data로 Learning Stack, Return Point, Graph 탐색,
  Concept Start/Complete, Bookmark, Depth 표시를 먼저 검증한다.

## 6. 후속 TASK의 버전 확정 규칙

TASK 00에서는 기술 계열과 역할을 확정한다. 실제 Project 초기화 시에는 다음을
지킨다.

- Backend는 Java 21을 지원하는 최신 안정 Spring Boot 3.x와 호환 Gradle
  Wrapper를 선택한다.
- Frontend는 선택한 React 버전을 지원하는 Vite, React Flow, TanStack Query,
  Zustand 조합을 사용한다.
- Major Version은 명시적으로 고정하고 Lockfile과 Gradle Wrapper를 Commit한다.
- Tailwind CSS와 React Flow의 CSS Import 순서는 해당 버전의 공식 지침을 따른다.
