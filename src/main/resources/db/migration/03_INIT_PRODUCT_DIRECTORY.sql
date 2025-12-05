create sequence if not exists sqn_category
    start with 1;

create table if not exists
    category_directory (
                id bigint primary key default nextval('sqn_category'),
                name varchar(255) not null
);
