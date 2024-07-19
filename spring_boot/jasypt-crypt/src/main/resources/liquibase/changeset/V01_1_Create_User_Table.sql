--liquibase formatted sql
--changeset v01_1:CreateUserTable endDelimiter:;
--comment Create User Table script

CREATE TABLE User (
    id int,
    validFrom datetime,
    userFirstName varchar(100),
    userLastName varchar(100),
    userMiddleName varchar(100),
    userName varchar(100),
    password varchar(100)
);