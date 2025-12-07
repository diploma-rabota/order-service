create sequence if not exists sqn_category
    start with 1;

create table if not exists
    category_directory (
                id bigint primary key default nextval('sqn_category'),
                name varchar(255) not null
);


insert into category_directory (name) values
                                          ('Одежда'),
                                          ('Электроника'),
                                          ('Мебель'),
                                          ('Инструменты'),
                                          ('Продукты'),
                                          ('Бытовая техника'),
                                          ('Спорт и отдых'),
                                          ('Дом и сад'),
                                          ('Автотовары'),
                                          ('Красота и здоровье'),
                                          ('Игрушки'),
                                          ('Канцелярия'),
                                          ('Зоотовары'),
                                          ('Строительные материалы'),
                                          ('Детские товары'),
                                          ('Посуда'),
                                          ('Офисные товары'),
                                          ('Хозтовары');
