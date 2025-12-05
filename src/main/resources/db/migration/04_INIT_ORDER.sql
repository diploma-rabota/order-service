create sequence if not exists sqn_order
    start with 1;

create table if not exists items
(
    id                bigint  not null primary key default nextval('sqn_order'),
    company_seller_id bigint  not null,
    constraint fk_company_seller_id
        foreign key (company_seller_id)
            references company (id),
    company_buyer_id  bigint  not null,
    constraint fk_company_buyer_id
        foreign key (company_buyer_id)
            references company (id),
    status            varchar not null,
    delivery_status   varchar,
    created_at        bigint  not null,
    updated_at        timestamp,
    is_excel_ready    boolean,
    total_price       bigint
);
comment on table items is 'Таблица для хранения количество товаров на складе';
