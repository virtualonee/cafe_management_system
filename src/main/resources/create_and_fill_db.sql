DROP TABLE IF EXISTS Shift CASCADE;
DROP TABLE IF EXISTS Reservation CASCADE;
DROP TABLE IF EXISTS Cafe_order CASCADE;
DROP TABLE IF EXISTS Dish CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;
DROP TABLE IF EXISTS Cafe CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Dish_Order CASCADE;
DROP TABLE IF EXISTS Reservation CASCADE;



CREATE TABLE Person (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username varchar(100) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    role varchar (100)
);

CREATE TABLE Cafe (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL,
    address varchar(100) NOT NULL,
    person_id int REFERENCES Person(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE Shift (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL,
    price decimal,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

CREATE TABLE Employee (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    fio varchar(100) NOT NULL,
    phone varchar(100) NOT NULL,
    job_title varchar(100) NOT NULL,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE,
    shift_id bigint REFERENCES Shift(id) ON DELETE SET NULL
);

CREATE TABLE Dish (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(200) NOT NULL,
    price decimal,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

CREATE TABLE Cafe_order (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    made_at timestamp,
    comment text,
    status varchar(100),
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

CREATE TABLE Dish_Order (
    cafe_order_id bigint REFERENCES Cafe_order(id) ON DELETE CASCADE,
    dish_id bigint REFERENCES Dish(id) ON DELETE CASCADE
);

CREATE TABLE Reservation (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    table_number int,
    peopleAmount int,
    comment text,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

-- password in bcrypt = 'test"
INSERT INTO Person(id, username, password, role) VALUES (0, 'test', '$2a$10$WsoMjjalhwdWEHBW.AJUNOrHYBvWo9.mDt27A4epA6Pwws2IuIilu', 'USER');
INSERT INTO Person(id, username, password) VALUES (-1, 'fakeUser', '$2a$10$WsoMjjalhwdWEHBW.AJUNOrHYBvWo9.mDt27A4epA6Pwws2IuIilu');

INSERT INTO Cafe(id, name, address, person_id) VALUES (0, 'Test cafe 1', '1215 Morgan Street', 0);
INSERT INTO Cafe(id, name, address, person_id) VALUES (-1, 'Fake cafe', 'Fake address', -1);

INSERT INTO Employee(fio, phone, job_title, cafe_id) VALUES ('testFio1', 'testPhone1', 'Cook', 0);
INSERT INTO Employee(fio, phone, job_title, cafe_id) VALUES ('testFio2', 'testPhone2', 'Waiter', 0);
INSERT INTO Employee(fio, phone, job_title, cafe_id) VALUES ('testFio3', 'testPhone3', 'Waiter', 0);

INSERT INTO Shift(id, name, price, cafe_id) VALUES (0, 'From 6.00 to 18.00', 1200, 0);
INSERT INTO Shift(id, name, price, cafe_id) VALUES (1, 'From 8.00 to 20.00', 800, 0);
INSERT INTO Shift(id, name, price, cafe_id) VALUES (2, 'From 10.00 to 22.00', 1000, 0);

