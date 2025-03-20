
    alter table if exists posts 
       drop constraint if exists FK5lidm6cqbc7u4xhqpxm898qme;

    drop table if exists posts cascade;

    drop table if exists users cascade;

    create table posts (
        id bigserial not null,
        content varchar(5000) not null,
        createdAt timestamp(6),
        title varchar(255) not null,
        updatedAt timestamp(6),
        user_id bigint not null,
        primary key (id)
    );

    create table users (
        id bigserial not null,
        createdAt timestamp(6),
        email varchar(255) not null unique,
        fullName varchar(255),
        password varchar(255) not null,
        updatedAt timestamp(6),
        username varchar(255) not null unique,
        primary key (id)
    );

    alter table if exists posts 
       add constraint FK5lidm6cqbc7u4xhqpxm898qme 
       foreign key (user_id) 
       references users;
