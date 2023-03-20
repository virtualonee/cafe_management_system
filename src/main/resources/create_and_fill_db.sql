DROP TABLE IF EXISTS Shift CASCADE;
DROP TABLE IF EXISTS Reservation CASCADE;
DROP TABLE IF EXISTS "Order" CASCADE;
DROP TABLE IF EXISTS Dish CASCADE;
DROP TABLE IF EXISTS  CASCADE;
DROP TABLE IF EXISTS Cafe CASCADE;
DROP TABLE IF EXISTS "User" CASCADE;

CREATE TABLE "User" (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name varchar(100) NOT NULL UNIQUE,
    password varchar(20) NOT NULL
);

CREATE TABLE Cafe (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL,
    address varchar(100) NOT NULL,
    user_id int REFERENCES "User"(id) ON DELETE CASCADE,
    places int
);

CREATE TABLE Job_Title (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE Shift (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100)
);

CREATE TABLE Employee (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    fio varchar(100) NOT NULL,
    phone varchar(100) NOT NULL,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE,
    job_title_id bigint REFERENCES Job_Title(id) ON DELETE SET NULL,
    shift_id bigint REFERENCES Shift(id) ON DELETE SET NULL
);

CREATE TABLE Dish (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(200) NOT NULL,
    price money,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

CREATE TABLE "Order" (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    made_at date,
    comment text,
    status int,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

CREATE TABLE Dish_Order (
    order_id bigint REFERENCES "Order"(id) ON DELETE CASCADE,
    dish_id bigint REFERENCES Dish(id) ON DELETE CASCADE
);

CREATE TABLE Reservation (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    table_number int,
    peopleAmount int,
    comment text,
    cafe_id bigint REFERENCES Cafe(id) ON DELETE CASCADE
);

INSERT INTO "User"(id, user_name, password) VALUES (0, 'test', 'test');

INSERT INTO Cafe(id, name, address, user_id) VALUES (0, 'Test cafe 1', '1215 Morgan Street', 0);
