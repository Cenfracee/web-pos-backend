CREATE DATABASE IF NOT EXISTS posDB;

USE posDB;

CREATE TABLE IF NOT EXISTS Customer
(
    id varchar(30) null,
    name varchar(50) null,
    address varchar(50) null
);

create unique index Customer_id_uindex
	on Customer (id);

alter table Customer
    add constraint Customer_pk
        primary key (id);



CREATE TABLE IF NOT EXISTS Item
(
    code varchar(10) null,
    description varchar(50) null,
    qtyOnHand int null,
    unitPrice double null
);

create unique index Item_code_uindex
	on Item (code);

alter table Item
    add constraint Item_pk
        primary key (code);

