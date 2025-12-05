create sequence if not exists sqn_products
    start with 1;

create table if not exists products
(
    id                     bigint  not null primary key default nextval('sqn_products'),
    name                   varchar not null,
    description            varchar not null,
    category_id            bigint  not null,
    constraint fk_category_id
        foreign key (category_id)
            references category_directory (id),
    company_id     bigint  not null,
    constraint fk_company_id
        foreign key (company_id)
            references company (id),
    available_quantity     bigint  not null,
    unit           varchar not null,
    price_retail           bigint  not null,
    price_wholesale        bigint  not null,
    min_quantity_wholesale bigint  not null,
    updated_at     timestamp
);
comment on table products is 'Таблица для хранения товара продавца, его количества, цен';
comment on column products.available_quantity is 'Количество доступного товара';
comment on column products.unit is 'Едининца измерения товара';

