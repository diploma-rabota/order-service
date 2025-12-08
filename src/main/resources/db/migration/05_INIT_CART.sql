create sequence if not exists sqn_cart
    start with 1;


create table if not exists cart (
                      id bigint  not null primary key default nextval('sqn_cart'),
                      user_id bigint not null,
                      constraint fk_cart_user
                          foreign key (user_id)
                              references duser (id),
                      company_id bigint not null,
                      constraint fk_cart_company
                          foreign key (company_id)
                              references company (id),
                      total_sum bigint not null,
                      created_at timestamp default now(),
                      updated_at timestamp default now()
);


create sequence if not exists sqn_cart_item
    start with 1;

create table if not exists cart_item (
                           id bigint not null primary key default nextval('sqn_cart_item'),
                           cart_id bigint not null,
                           constraint fk_cart_item
                               foreign key (cart_id)
                                   references cart (id),
                           product_id bigint not null,
                           constraint fk_cart_item_product
                               foreign key (product_id)
                                   references product (id),
                           quantity bigint not null
);
