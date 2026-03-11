-- Быстрый поиск счета по номеру счета (используется при каждом переводе)
CREATE INDEX idx_accounts_account_number
    ON accounts (account_number);

-- Быстрая фильтрация транзакций по дате
CREATE INDEX idx_transactions_created_at
    ON transactions (created_at);

-- Быстрый поиск всех транзакций для заданного исходного счета
CREATE INDEX idx_transactions_source_account
    ON transactions (source_account_id);

-- Быстрый поиск всех транзакций для заданного целевого счета
CREATE INDEX idx_transactions_target_account
    ON transactions (target_account_id);

-- Составной индекс: исходный счет + дата (desc) — для выписки и аналитики
CREATE INDEX idx_transactions_source_created
    ON transactions (source_account_id, created_at DESC);

-- Составной индекс: целевой счет + дата (desc) — для выписки и аналитики
CREATE INDEX idx_transactions_target_created
    ON transactions (target_account_id, created_at DESC);