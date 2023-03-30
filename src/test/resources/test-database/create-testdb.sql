DROP TABLE IF EXISTS theatre CASCADE;
CREATE TABLE theatre (
                         id SERIAL PRIMARY KEY,
                         name TEXT,
                         seats_ground_floor INTEGER,
                         seats_balcony INTEGER,
                         seats_mezzanine INTEGER,
                         building TEXT,
                         street TEXT,
                         town TEXT,
                         country TEXT
);

DROP TABLE IF EXISTS schedule CASCADE;
CREATE TABLE schedule (
                          id SERIAL PRIMARY KEY,
                          performance_id INTEGER,
                          theatre_id INTEGER REFERENCES theatre ON DELETE CASCADE,
                          date_time TIMESTAMP,
                          free_seats_ground_floor INTEGER,
                          free_seats_balcony INTEGER,
                          free_seats_mezzanine INTEGER,
                          price_ground_floor INTEGER,
                          price_balcony INTEGER,
                          price_mezzanine INTEGER,
                          UNIQUE(performance_id, theatre_id, date_time)
);

DROP TABLE IF EXISTS participant CASCADE;
CREATE TABLE participant (
                             id SERIAL PRIMARY KEY,
                             firstname TEXT,
                             lastname TEXT
);

DROP TABLE IF EXISTS performance CASCADE;
CREATE TABLE performance (
                             id SERIAL PRIMARY KEY,
                             title TEXT,
                             duration TIME,
                             genre TEXT,
                             rating float4,
                             director INTEGER REFERENCES participant ON DELETE RESTRICT
);

DROP TABLE IF EXISTS ticket CASCADE;
CREATE TABLE ticket (
                        id SERIAL PRIMARY KEY,
                        person_firstname TEXT,
                        person_lastname TEXT,
                        seat INTEGER,
                        price float4,
                        schedule INTEGER REFERENCES schedule ON DELETE CASCADE
);

DROP TABLE IF EXISTS participance CASCADE;
CREATE TABLE participance (
                              id SERIAL PRIMARY KEY,
                              perf INTEGER REFERENCES performance ON DELETE CASCADE,
                              part INTEGER REFERENCES participant ON DELETE CASCADE,
                              role TEXT,
                              UNIQUE(perf, part)
);
