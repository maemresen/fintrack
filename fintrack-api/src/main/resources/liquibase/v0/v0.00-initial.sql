CREATE TABLE budget
(
    id   BIGINT       NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_budget PRIMARY KEY (id)
);

CREATE TABLE statement
(
    id           BIGINT                      NOT NULL,
    created_by   VARCHAR(255)                NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by   VARCHAR(255)                NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description  VARCHAR(255),
    amount       DOUBLE PRECISION            NOT NULL,
    currency     VARCHAR(255)                NOT NULL,
    type         VARCHAR(255)                NOT NULL,
    date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    category     VARCHAR(255),
    budget_id    BIGINT                      NOT NULL,
    CONSTRAINT pk_statement PRIMARY KEY (id)
);

ALTER TABLE statement
    ADD CONSTRAINT FK_STATEMENT_ON_BUDGET FOREIGN KEY (budget_id) REFERENCES budget (id);