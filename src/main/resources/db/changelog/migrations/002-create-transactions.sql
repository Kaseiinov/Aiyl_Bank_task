CREATE TABLE transactions (
    id                BIGSERIAL       PRIMARY KEY,
    source_account_id BIGINT,
    target_account_id BIGINT,
    amount            NUMERIC(19,2)   NOT NULL,
    type              VARCHAR(20)     NOT NULL,
    status            VARCHAR(10)     NOT NULL,
    balance_after     NUMERIC(19,2),
    failure_reason    TEXT,
    created_at        TIMESTAMP       NOT NULL DEFAULT NOW()
);

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_source_account
    FOREIGN KEY (source_account_id) REFERENCES accounts (id);

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_target_account
    FOREIGN KEY (target_account_id) REFERENCES accounts (id);

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_amount
    CHECK (amount > 0);

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_type
    CHECK (type IN ('TRANSFER_OUT', 'TRANSFER_IN'));

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_status
    CHECK (status IN ('SUCCESS', 'FAILED'));