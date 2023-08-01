truncate table "restTest".public.roles cascade;
select setval('"restTest".public.roles_id_seq', 1, false);
truncate table "restTest".public.users cascade;
select setval('"restTest".public.users_id_seq', 1, false);
