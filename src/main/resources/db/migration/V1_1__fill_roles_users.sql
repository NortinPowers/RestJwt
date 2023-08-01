insert into roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into users (user_name, password, role_id)
values ('admin', 'admin', 1),
       ('user', 'user', 2);
