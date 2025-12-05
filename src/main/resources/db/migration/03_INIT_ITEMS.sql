create sequence if not exists sqn_items
    start with 1;

create table if not exists items
(
    id             bigint  not null primary key default nextval('sqn_items'),
    product_id     bigint  not null,
    constraint fk_product_id
        foreign key (product_id)
            references product_directory (id),
    company_id     bigint  not null,
    constraint fk_company_id
        foreign key (company_id)
            references company (id),
    quantity       bigint  not null,
    unit           varchar not null,
    price_per_unit bigint  not null,
    updated_at     timestamp
);
comment on table items is 'Таблица для хранения количество товаров на складе';
comment on column items.quantity is 'Количество товара';
comment on column items.unit is 'Едининца измерения товара';

