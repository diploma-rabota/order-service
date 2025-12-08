create sequence if not exists sqn_product
    start with 1;

create table if not exists product
(
    id                 bigint  not null primary key default nextval('sqn_product'),
    name               varchar not null,
    description        varchar not null,
    category_id        bigint  not null,
    constraint fk_category_id
        foreign key (category_id)
            references category_directory (id),
    company_id         bigint  not null,
    constraint fk_company_id
        foreign key (company_id)
            references company (id),
    available_quantity bigint  not null,
    unit               varchar not null,
    price_retail       bigint  not null,
    updated_at         timestamp
);
comment on table product is 'Таблица для хранения товара продавца, его количества, цен';
comment on column product.available_quantity is 'Количество доступного товара';
comment on column product.unit is 'Единица измерения товара';


create sequence if not exists sqn_price
    start with 1;

create table if not exists price_tier
(
    id             bigint not null primary key default nextval('sqn_price'),
    product_id     bigint not null,
    constraint fk_product_id_price
        foreign key (product_id)
            references product (id)
            on delete cascade,
    quantity_from  bigint not null,
    price_per_unit bigint not null
);

comment on table price_tier is 'Оптовые пороги цен для товара';
