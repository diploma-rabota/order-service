create sequence if not exists sqn_company
start with 1;

create table if not exists company (
        id bigint primary key default nextval('sqn_company'),
        inn varchar(20) not null,
        bik varchar(20) not null,
        name varchar(255) not null,
        address varchar(255) not null
    );