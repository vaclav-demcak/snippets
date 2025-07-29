CREATE DATABASE IF NOT EXISTS booking;
USE booking;
CREATE TABLE IF NOT EXISTS train (
                                   id bigint AUTO_INCREMENT,
                                   line_price double,
                                   name varchar(255),
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS passenger (
                            id bigint AUTO_INCREMENT,
                            name varchar(255),
                            price double,
    user_id bigint,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS user (
                            id bigint AUTO_INCREMENT,
                            name varchar(255),
                            password varchar(255),
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS ticket (
                                    id bigint AUTO_INCREMENT,
                                    file_path varchar(255),
                                    last_train varchar(255),
    password varchar(255),
    PRIMARY KEY (id)
    );
INSERT INTO
    train(line_price, name)
VALUES
    (25.5, "11EuroTrip"),
    (5.5, "18d"),
    (5.5, "um73"),
    (5.5, "132p"),
    (11, "522st");