create sequence if not exists sqn_cart
    start with 1;

create sequence if not exists sqn_cart_item
    start with 1;

create sequence if not exists sqn_order
    start with 1;

create sequence if not exists sqn_order_item
    start with 1;

create table if not exists cart
(
    id         bigint primary key default nextval('sqn_cart'),
    user_id    bigint    not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,

    constraint uk_cart_user unique (user_id)
    );

create table if not exists cart_item
(
    id               bigint primary key default nextval('sqn_cart_item'),
    cart_id          bigint       not null,
    product_article  varchar(100) not null,
    quantity         integer      not null,
    created_at       timestamp    not null default current_timestamp,
    updated_at       timestamp    not null default current_timestamp,

    constraint fk_cart_item_cart
    foreign key (cart_id)
    references cart (id)
    on delete cascade,

    constraint uk_cart_item_cart_article unique (cart_id, product_article),

    constraint chk_cart_item_quantity
    check (quantity > 0)
    );

create table if not exists orders
(
    id            bigint primary key default nextval('sqn_order'),
    user_id       bigint         not null,
    user_email    varchar(50)    not null,
    status        varchar(50)    not null,
    total_amount  numeric(19, 2) not null,
    created_at    timestamp      not null default current_timestamp,
    updated_at    timestamp      not null default current_timestamp,

    constraint chk_orders_total_amount
    check (total_amount >= 0)
    );

create table if not exists order_item
(
    id               bigint primary key default nextval('sqn_order_item'),
    order_id         bigint         not null,
    product_article  varchar(100)   not null,
    product_name     varchar(255)   not null,
    quantity         integer        not null,
    price            numeric(19, 2) not null,
    line_total       numeric(19, 2) not null,
    created_at       timestamp      not null default current_timestamp,

    constraint fk_order_item_order
    foreign key (order_id)
    references orders (id)
    on delete cascade,

    constraint chk_order_item_quantity
    check (quantity > 0),

    constraint chk_order_item_price
    check (price >= 0),

    constraint chk_order_item_line_total
    check (line_total >= 0)
    );

create index if not exists idx_cart_user_id
    on cart (user_id);

create index if not exists idx_cart_item_cart_id
    on cart_item (cart_id);

create index if not exists idx_cart_item_product_article
    on cart_item (product_article);

create index if not exists idx_orders_user_id
    on orders (user_id);

create index if not exists idx_orders_status
    on orders (status);

create index if not exists idx_order_item_order_id
    on order_item (order_id);

create index if not exists idx_order_item_product_article
    on order_item (product_article);