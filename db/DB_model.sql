use deliver_it;

create table countries
(
    country_id int auto_increment
        primary key,
    country text not null,
    constraint countries_name_uindex
        unique (country) using hash
);

create table cities
(
    city_id int auto_increment
        primary key,
    city text not null,
    country_id int not null,
    constraint cities_countries_fk
        foreign key (country_id) references countries (country_id)
);

create table addresses
(
    address_id int auto_increment
        primary key,
    city_id int not null,
    street_line_1 text not null,
    street_line_2 text null,
    post_code text null,
    constraint addresses_cities_city_id_fk
        foreign key (city_id) references cities (city_id)
);

create table parcel_categories
(
    parcel_category_id int auto_increment
        primary key,
    parcel_category text not null,
    constraint parcel_categories_parcel_category_uindex
        unique (parcel_category) using hash
);

create table statuses
(
    status_id int auto_increment
        primary key,
    status text not null,
    constraint statuses_status_uindex
        unique (status) using hash
);

create table users
(
    user_id int auto_increment
        primary key,
    first_name text not null,
    last_name text not null,
    email text not null
);

create table customers
(
    customer_id int not null
        primary key,
    address_id int not null,
    constraint customers_addresses_address_id_fk
        foreign key (address_id) references addresses (address_id),
    constraint customers_users_user_id_fk
        foreign key (customer_id) references users (user_id)
);

create table employees
(
    employee_id int not null
        primary key,
    password_hash text not null,
    salt double not null,
    constraint employees_password_hash_uindex
        unique (password_hash) using hash,
    constraint employees_users_user_id_fk
        foreign key (employee_id) references users (user_id)
);

create table warehouses
(
    warehouse_id int auto_increment
        primary key,
    address_id int not null,
    constraint warehouses_address_id_uindex
        unique (address_id),
    constraint warehouses_addresses_address_id_fk
        foreign key (address_id) references addresses (address_id)
);

create table shipments
(
    shipment_id int auto_increment
        primary key,
    departure_date date not null,
    arrival_date date not null,
    status_id int not null,
    warehouse_id int not null,
    constraint shipments_statuses_status_id_fk
        foreign key (status_id) references statuses (status_id),
    constraint shipments_warehouses_warehouse_id_fk
        foreign key (warehouse_id) references warehouses (warehouse_id)
);

create table weight_units
(
    weight_unit_id int auto_increment
        primary key,
    weight_unit text not null,
    constraint weight_units_unit_uindex
        unique (weight_unit) using hash
);

create table parcels
(
    parcel_id int auto_increment
        primary key,
    customer_id int not null,
    warehouse_id int not null,
    parcel_category_id int not null,
    shipment_id int not null,
    weight_amount double not null,
    weight_units_id int not null,
    constraint parcels_customers_customer_id_fk
        foreign key (customer_id) references customers (customer_id),
    constraint parcels_parcel_categories_parcel_category_id_fk
        foreign key (parcel_category_id) references parcel_categories (parcel_category_id),
    constraint parcels_shipments_shipment_id_fk
        foreign key (shipment_id) references shipments (shipment_id),
    constraint parcels_warehouses_warehouse_id_fk
        foreign key (warehouse_id) references warehouses (warehouse_id),
    constraint parcels_weight_units_weight_unit_id_fk
        foreign key (weight_units_id) references weight_units (weight_unit_id)
);

