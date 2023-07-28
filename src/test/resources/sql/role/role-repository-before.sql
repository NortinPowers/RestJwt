truncate table "restTest".public.roles cascade;
select setval('"restTest".public.roles_id_seq', 1, false);
insert into "restTest".public.roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');
