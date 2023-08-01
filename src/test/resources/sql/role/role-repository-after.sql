truncate table "restTest".public.roles cascade;
select setval('"restTest".public.roles_id_seq', 1, false);
