CREATE DATABASE learning_db;
USE learning_db;

CREATE TABLE students (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    age INT
);

INSERT INTO students VALUES (1, 'Kusum', 22);
INSERT INTO students VALUES (2, 'Aman', 23);

SELECT * FROM students;
