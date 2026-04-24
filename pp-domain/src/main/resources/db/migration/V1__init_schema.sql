CREATE TABLE documents
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    created_on          DATETIME(6) NULL,
    document_created_at DATETIME(6) NULL,
    document_updated_at DATETIME(6) NULL,
    updated_on          DATETIME(6) NULL,
    created_by          VARCHAR(255) NULL,
    external_id         VARCHAR(255) NOT NULL,
    external_owner_id   VARCHAR(255) NOT NULL,
    status              VARCHAR(255) NOT NULL,
    title               VARCHAR(255) NOT NULL,
    updated_by          VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_documents_external_id UNIQUE (external_id)
) ENGINE=InnoDB;

CREATE TABLE rules
(
    id          INT          NOT NULL AUTO_INCREMENT,
    active      BIT          NOT NULL,
    created_on  DATETIME(6) NULL,
    updated_on  DATETIME(6) NULL,
    created_by  VARCHAR(255) NULL,
    description VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_rules_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE document_tags
(
    document_id BIGINT NOT NULL,
    tags        VARCHAR(255) NULL,
    CONSTRAINT fk_document_tags_document
        FOREIGN KEY (document_id) REFERENCES documents (id)
) ENGINE=InnoDB;

CREATE TABLE document_rule_violations
(
    id                BIGINT NOT NULL AUTO_INCREMENT,
    rule_id           INT    NOT NULL,
    violating         BIT    NOT NULL,
    created_on        DATETIME(6) NULL,
    document_id       BIGINT NOT NULL,
    updated_on        DATETIME(6) NULL,
    violation_details VARCHAR(1000) NULL,
    created_by        VARCHAR(255) NULL,
    updated_by        VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_document_rule_violations_document_rule UNIQUE (document_id, rule_id),
    CONSTRAINT fk_document_rule_violations_document
        FOREIGN KEY (document_id) REFERENCES documents (id),
    CONSTRAINT fk_document_rule_violations_rule
        FOREIGN KEY (rule_id) REFERENCES rules (id)
) ENGINE=InnoDB;

CREATE INDEX idx_documents_external_id ON documents (external_id);

CREATE INDEX idx_document_rule_violations_document_id
    ON document_rule_violations (document_id);

CREATE INDEX idx_document_rule_violations_rule_id
    ON document_rule_violations (rule_id);

CREATE INDEX idx_document_rule_violations_violating
    ON document_rule_violations (violating);
