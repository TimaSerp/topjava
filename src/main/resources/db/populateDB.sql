DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-01-30 10:00'::TIMESTAMP, 'Завтрак', 500),
       (100000, '2020-01-30 13:00'::TIMESTAMP, 'Обед', 1000),
       (100000, '2020-01-30 20:00'::TIMESTAMP, 'Ужин', 500),
       (100000, '2020-01-31 00:00'::TIMESTAMP, 'Еда на граничное значение', 100),
       (100000, '2020-01-31 10:00'::TIMESTAMP, 'Завтрак', 1000),
       (100000, '2020-01-31 13:00'::TIMESTAMP, 'Обед', 500),
       (100000, '2020-01-31 20:00'::TIMESTAMP, 'Ужин', 410),
       (100001, '2020-01-30 10:00'::TIMESTAMP, 'Завтрак админа', 500),
       (100001, '2020-01-30 13:00'::TIMESTAMP, 'Обед админа', 1000),
       (100001, '2020-01-30 20:00'::TIMESTAMP, 'Ужин админа', 500),
       (100001, '2020-01-31 00:00'::TIMESTAMP, 'Еда на граничное значение админа', 100),
       (100001, '2020-01-31 10:00'::TIMESTAMP, 'Завтрак админа', 1000),
       (100001, '2020-01-31 13:00'::TIMESTAMP, 'Обед админа', 500),
       (100001, '2020-01-31 20:00'::TIMESTAMP, 'Ужин админа', 410);
