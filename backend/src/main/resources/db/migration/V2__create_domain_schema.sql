CREATE TABLE learning_sessions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    goal TEXT NOT NULL,
    root_concept_id BIGINT,
    current_concept_id BIGINT,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_learning_sessions_status
        CHECK (status IN ('ACTIVE', 'COMPLETED'))
);

CREATE TABLE concepts (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    summary TEXT NOT NULL,
    description TEXT NOT NULL,
    importance VARCHAR(32) NOT NULL,
    recommended_depth VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_concepts_session
        FOREIGN KEY (session_id) REFERENCES learning_sessions (id) ON DELETE CASCADE,
    CONSTRAINT uq_concepts_id_session UNIQUE (id, session_id),
    CONSTRAINT ck_concepts_importance
        CHECK (importance IN ('PREREQUISITE', 'CORE', 'IMPLEMENTATION', 'ADVANCED', 'RELATED')),
    CONSTRAINT ck_concepts_depth
        CHECK (recommended_depth IN ('LIGHT', 'UNDERSTAND', 'DEEP')),
    CONSTRAINT ck_concepts_status
        CHECK (status IN ('NOT_STARTED', 'LEARNING', 'PAUSED', 'COMPLETED', 'SKIPPED', 'BOOKMARKED'))
);

ALTER TABLE learning_sessions
    ADD CONSTRAINT fk_learning_sessions_root_concept
        FOREIGN KEY (root_concept_id, id) REFERENCES concepts (id, session_id),
    ADD CONSTRAINT fk_learning_sessions_current_concept
        FOREIGN KEY (current_concept_id, id) REFERENCES concepts (id, session_id);

CREATE TABLE concept_relations (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    source_concept_id BIGINT NOT NULL,
    target_concept_id BIGINT NOT NULL,
    relation_type VARCHAR(32) NOT NULL,
    reason TEXT NOT NULL,
    CONSTRAINT fk_concept_relations_session
        FOREIGN KEY (session_id) REFERENCES learning_sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_concept_relations_source
        FOREIGN KEY (source_concept_id, session_id) REFERENCES concepts (id, session_id) ON DELETE CASCADE,
    CONSTRAINT fk_concept_relations_target
        FOREIGN KEY (target_concept_id, session_id) REFERENCES concepts (id, session_id) ON DELETE CASCADE,
    CONSTRAINT uq_concept_relations_edge
        UNIQUE (session_id, source_concept_id, target_concept_id, relation_type),
    CONSTRAINT ck_concept_relations_type
        CHECK (relation_type IN ('PREREQUISITE', 'CORE', 'IMPLEMENTATION', 'ADVANCED', 'RELATED')),
    CONSTRAINT ck_concept_relations_distinct_nodes
        CHECK (source_concept_id <> target_concept_id)
);

CREATE TABLE learning_stack_entries (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    concept_id BIGINT NOT NULL,
    parent_concept_id BIGINT,
    stack_order INTEGER NOT NULL,
    entered_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    CONSTRAINT fk_learning_stack_entries_session
        FOREIGN KEY (session_id) REFERENCES learning_sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_learning_stack_entries_concept
        FOREIGN KEY (concept_id, session_id) REFERENCES concepts (id, session_id),
    CONSTRAINT fk_learning_stack_entries_parent
        FOREIGN KEY (parent_concept_id, session_id) REFERENCES concepts (id, session_id),
    CONSTRAINT ck_learning_stack_entries_order CHECK (stack_order >= 0)
);

CREATE TABLE bookmarks (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    concept_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_bookmarks_session
        FOREIGN KEY (session_id) REFERENCES learning_sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_bookmarks_concept
        FOREIGN KEY (concept_id, session_id) REFERENCES concepts (id, session_id) ON DELETE CASCADE,
    CONSTRAINT uq_bookmarks_session_concept UNIQUE (session_id, concept_id)
);

CREATE INDEX idx_concepts_session ON concepts (session_id);
CREATE INDEX idx_concept_relations_source ON concept_relations (session_id, source_concept_id);
CREATE INDEX idx_concept_relations_target ON concept_relations (session_id, target_concept_id);
CREATE INDEX idx_learning_stack_entries_session_order
    ON learning_stack_entries (session_id, stack_order);
CREATE UNIQUE INDEX uq_learning_stack_entries_active_order
    ON learning_stack_entries (session_id, stack_order)
    WHERE completed_at IS NULL;
CREATE INDEX idx_bookmarks_session ON bookmarks (session_id);
CREATE INDEX idx_learning_sessions_root_concept ON learning_sessions (root_concept_id);
CREATE INDEX idx_learning_sessions_current_concept ON learning_sessions (current_concept_id);
