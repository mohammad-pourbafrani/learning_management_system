DROP TABLE IF EXISTS education_degree CASCADE;
CREATE TABLE education_degree
(
    id     SERIAL PRIMARY KEY,
    degree VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO education_degree (degree)
VALUES ('High School Diploma'),
       ('Associate Degree'),
       ('Bachelor’s Degree'),
       ('Master’s Degree'),
       ('Doctorate (Ph.D.)'),
       ('Professional Degree'),
       ('Certificate Program');


DROP TABLE IF EXISTS education_title CASCADE;
CREATE TABLE education_title
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO education_title (title)
VALUES ('Computer Engineering'),
       ('Software Engineering'),
       ('Information Technology'),
       ('Computer Science'),
       ('Electrical Engineering'),
       ('Mechanical Engineering'),
       ('Civil Engineering'),
       ('Industrial Engineering'),
       ('Business Administration'),
       ('Economics'),
       ('Accounting'),
       ('Finance'),
       ('Marketing'),
       ('Psychology'),
       ('Sociology'),
       ('Political Science'),
       ('Law'),
       ('Architecture'),
       ('Medicine'),
       ('Nursing'),
       ('Pharmacy'),
       ('Biology'),
       ('Chemistry'),
       ('Physics'),
       ('Mathematics'),
       ('Education'),
       ('Art and Design'),
       ('Graphic Design'),
       ('Communication'),
       ('International Relations');