-- BUDGET 1
INSERT INTO public.budget (id, name)
VALUES (1, 'Test Budget 1');

-- Statement August 2023 - 1
INSERT INTO public.statement (id, created_date, updated_date, description, amount, currency, type, date, category, budget_id)
VALUES (3, '2023-08-06 00:21:46.000000', '2023-08-06 00:21:48.000000', 'Test Expense 2', 100, 'EUR', 'EXPENSE', '2023-08-06 00:22:11.000000', null, 1);

-- Statement August 2023 - 2
INSERT INTO public.statement (id, created_date, updated_date, description, amount, currency, type, date, category, budget_id)
VALUES (4, '2023-08-06 00:21:46.000000', '2023-08-06 00:21:48.000000', 'Test Income 2', 100, 'EUR', 'EXPENSE', '2023-08-06 00:22:11.000000', null, 1);

-- Statement September 2023 - 1
INSERT INTO public.statement (id, created_date, updated_date, description, amount, currency, type, date, category, budget_id)
VALUES (5, '2023-08-06 00:21:46.000000', '2023-08-06 00:21:48.000000', 'Test Expense 3', 100, 'EUR', 'EXPENSE', '2023-09-06 00:22:11.000000', null, 1);

-- Reset sequences
SELECT setval('budget_id_seq', (SELECT MAX(id) FROM budget));
SELECT setval('statement_id_seq', (SELECT MAX(id) FROM statement));
