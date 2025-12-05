create sequence if not exists sqn_user
    start with 1;

create table if not exists duser
(
    id         bigint primary key default nextval('sqn_user'),
    user_name  varchar(100) not null,
    password   varchar(255) not null,
    company_id varchar(255),
    email      varchar(100) not null,
    address    varchar(255)
);