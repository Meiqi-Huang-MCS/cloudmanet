CREATE DATABASE manetcloudservice;
USE manetcloudservice;

CREATE TABLE client (
    id INT NOT NULL AUTO_INCREMENT,
    longitude FLOAT NOT NULL,
    latitude FLOAT NOT NULL,
    netid INT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE manet (
    netid INT NOT NULL AUTO_INCREMENT,
    netstatus BIT(1) NOT NULL,
    start_time DATETIME NOT NULL,
    PRIMARY KEY (netid)
);
