truncate table "restTest".public.roles cascade;
select setval('"restTest".public.roles_id_seq', 1, false);
truncate table "restTest".public.users cascade;
select setval('"restTest".public.users_id_seq', 1, false);
insert into "restTest".public.roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');
insert into "restTest".public.users (user_name, password, role_id)
values ('user', 'password', 2),
       ('admin', 'password', 1);
