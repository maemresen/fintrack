INSERT INTO public.budget (id, name)
VALUES (1, 'Test Budget 1');
SELECT setval('budget_id_seq', (SELECT MAX(id) FROM budget));

INSERT INTO public.statement (id, created_date, updated_date, description, amount, currency, type, date, category,
                              budget_id)
VALUES (1, '2023-08-06 00:21:46.000000', '2023-08-06 00:21:48.000000', 'Test Expense 1', 100, 'EUR', 'EXPENSE',
        '2023-08-06 00:22:11.000000', null, 1);
INSERT INTO public.statement (id, created_date, updated_date, description, amount, currency, type, date, category,
                              budget_id)
VALUES (2, '2023-08-06 00:21:46.000000', '2023-08-06 00:21:48.000000', 'Test Income 1', 100, 'EUR', 'EXPENSE',
        '2023-08-06 00:22:11.000000', null, 1);
SELECT setval('statement_id_seq', (SELECT MAX(id) FROM statement));
