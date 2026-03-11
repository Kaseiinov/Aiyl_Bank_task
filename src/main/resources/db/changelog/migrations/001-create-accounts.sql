CREATE TABLE accounts (
    id             BIGSERIAL       PRIMARY KEY,
    account_number VARCHAR(20)     NOT NULL UNIQUE,
    owner_name     VARCHAR(255)    NOT NULL,
    balance        NUMERIC(19,2)   NOT NULL DEFAULT 0.00,
    status         VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMP       NOT NULL DEFAULT NOW()
);

ALTER TABLE accounts
    ADD CONSTRAINT chk_accounts_status
    CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED'));