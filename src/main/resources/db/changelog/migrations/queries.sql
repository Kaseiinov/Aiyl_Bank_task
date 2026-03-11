-- AIYL BANK — Ответы на SQL задачи (Задание 3)

-- Запрос 1: ТОП-5 счетов по количеству транзакций за последний месяц
SELECT a.account_number,
       a.owner_name,
       COUNT(t.id) AS tx_count
FROM accounts a
         JOIN transactions t
              ON t.source_account_id = a.id
                  OR t.target_account_id = a.id
WHERE t.created_at >= NOW() - INTERVAL '1 month'
GROUP BY a.id, a.account_number, a.owner_name
ORDER BY tx_count DESC
LIMIT 5;

-- Запрос 2: Общая сумма переводов за выбранный период
SELECT SUM(t.amount) AS total_amount
FROM transactions t
WHERE t.type = 'TRANSFER_OUT'
  AND t.status = 'SUCCESS'
  AND t.created_at BETWEEN :from AND :to;

-- Запрос 3: Счета с отрицательным балансом
SELECT account_number,
       owner_name,
       balance
FROM accounts
WHERE balance < 0
ORDER BY balance ASC;

-- ------------------------------------
-- Запрос 4: Объяснение оптимизации индексов

-- Наиболее ресурсоёмкий запрос в продакшене — это запрос ТОП-5 / выписка по счёту,
-- так как он потенциально обрабатывает миллионы строк с фильтрацией по дате.
--
-- Оптимизация: составной покрывающий индекс по (created_at)
-- Уже определён в schema.sql:
--   CREATE INDEX idx_transactions_created_at ON transactions (created_at);
--   CREATE INDEX idx_transactions_source_created ON transactions (source_account_id, created_at DESC);
--   CREATE INDEX idx_transactions_target_created ON transactions (target_account_id, created_at DESC);
--
-- Почему:
--   1. idx_transactions_created_at — позволяет PostgreSQL использовать Index Scan
--      вместо Sequential Scan при фильтрации WHERE created_at >= NOW() - INTERVAL '1 month'.
--      Без этого индекса планировщик читает каждую строку таблицы — O(n).
--      С индексом он сразу переходит к нужному диапазону дат — O(log n + k).
--
--   2. idx_transactions_source_created / idx_transactions_target_created — составные индексы,
--      используемые в запросе выписки по счёту:
--        WHERE (source_account_id = ? OR target_account_id = ?) AND created_at BETWEEN ? AND ?
--      Составной индекс покрывает и фильтр по счёту, и сортированное извлечение по дате за один проход,
--      устраняя отдельный шаг сортировки (порядок DESC уже предсортирован в индексе).
