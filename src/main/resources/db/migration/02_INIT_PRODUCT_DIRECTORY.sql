create sequence if not exists sqn_product
    start with 1;

create table if not exists
    product_directory (
                id bigint primary key default nextval('sqn_product'),
                name varchar(255) not null
);
