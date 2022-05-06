# drop table if exists events;
#
# drop table if exists files;
#
# drop table if exists property;
#
# drop table if exists users;

create table events
(
    id        bigint not null auto_increment,
    operation integer,
    created   datetime(6),
    file_id   bigint,
    user_id   bigint,
    primary key (id)
) engine = InnoDB;

create table files
(
    id        bigint       not null auto_increment,
    bucket    varchar(255) not null,
    file_name varchar(255) not null,
    location  varchar(255) not null,
    primary key (id)
) engine = InnoDB ;

create table property
(
    id         bigint       not null auto_increment,
    prop_key   varchar(255) not null,
    prop_value varchar(255) not null,
    primary key (id)
) engine = InnoDB ;

create table users
(
    id       bigint not null auto_increment,
    active   bit,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    role     varchar(255),
    primary key (id)
) engine = InnoDB ;

alter table property
    add constraint  unique (prop_key) ;

alter table users
    add constraint  unique (email) ;

alter table events
    add constraint  foreign key (file_id) references files (id) ;

alter table events
    add constraint  foreign key (user_id) references users (id);

insert into users(name, email, password, role, active)
VALUES ('Sergei', 'a@gmail.com', '$2a$12$HQji1wlVdbthb/kr3jUUxOSlbiNIgcAUTlG/GcCTJmdvKBXx/1cK.', 'ADMIN', true),
       ('Igor', 'u@gmail.com', '$2a$12$HQji1wlVdbthb/kr3jUUxOSlbiNIgcAUTlG/GcCTJmdvKBXx/1cK.', 'USER', true),
       ('Oleg', 'm@gmail.com', '$2a$12$HQji1wlVdbthb/kr3jUUxOSlbiNIgcAUTlG/GcCTJmdvKBXx/1cK.', 'MODERATOR', true);


insert into property (prop_key, prop_value)
values ('accessKey', 'AKIAU6ALTAL4MW37ELGJ'),
       ('secretKey', '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');
