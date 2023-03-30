INSERT INTO theatre VALUES
                        (1, 'Большой', 450, 230, 100, 23, 'Лесная', 'Москва', 'Россия'),
                        (2, 'Молодежный', 50, 30, 30, 3, 'Ленина', 'Москва', 'Россия'),
                        (3, 'Пионерский', 45, 55, 10, 6, 'Алмазная', 'Минск', 'Беларусь');

INSERT INTO schedule VALUES
                         (1, 1, 1, '2020-10-19 10:30:00', 2, 3, 4, 1000, 2000, 3000),
                         (2, 1, 1, '2020-11-19 20:30:00', 2, 3, 4, 1000, 2000, 3000),
                         (3, 1, 2, '2020-10-30 19:00:00', 20, 30, 0, 100, 200, 300);

INSERT INTO participant VALUES
                            (1, 'Чернов', 'Василий'),
                            (2, 'Безруков', 'Сергей'),
                            (3, 'Алексеев', 'Петр');

INSERT INTO performance VALUES
                            (1, 'Мурзилка', '01:00', 'Спектакль', 7.5, 1),
                            (2, 'Война и мир', '02:00', 'Мюзикл', 9.3, 1),
                            (3, 'Анна Каренина', '02:40', 'Мюзикл', 9.6, 2);

INSERT INTO ticket VALUES
                       (1, 'Пупкин', 'Василий', 4, 500, 1),
                       (2, 'Пупкина', 'Екатерина', 5, 500, 1),
                       (3, 'Попов', 'Владимир', 6, 500, 2),
                       (4, 'Петров', 'Иван', 4, 500, 2),
                       (5, 'Иванова', 'Анна', 5, 500, 3),
                       (6, 'Краснова', 'Екатерина', 50, 500, 2);

INSERT INTO participance VALUES
                             (1, 1, 1, 'актер'),
                             (2, 2, 1, 'актер');
