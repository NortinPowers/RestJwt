drop table if exists books cascade;

create table books
(
    id     bigserial
        constraint books_pk
            primary key,
    title   varchar(80) not null,
    author varchar(80) not null
);
