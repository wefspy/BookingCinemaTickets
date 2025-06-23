--liquibase formatted sql

--changeset alexandr:2025-06-16--00-create-users-table
CREATE SEQUENCE users_user_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    user_id       BIGINT PRIMARY KEY DEFAULT nextval('users_user_id_seq'),
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);
--rollback DROP TABLE users; DROP SEQUENCE users_user_id_seq;


--changeset alexandr:2025-06-16--01-create-users-info-table
CREATE TABLE users_info
(
    user_id      BIGINT PRIMARY KEY,
    email        VARCHAR(255),
    phone_number VARCHAR(255),
    created_at   TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
--rollback DROP TABLE users_info;


--changeset alexandr:2025-06-16--02-create-roles-table
CREATE SEQUENCE roles_role_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE roles
(
    role_id BIGINT PRIMARY KEY DEFAULT nextval('roles_role_id_seq'),
    name    VARCHAR(255) NOT NULL UNIQUE
);
--rollback DROP TABLE roles; DROP SEQUENCE roles_role_id_seq;


--changeset alexandr:2025-06-16--03-create-roles-users-table
CREATE SEQUENCE roles_users_role_user_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE roles_users
(
    role_user_id BIGINT PRIMARY KEY DEFAULT nextval('roles_users_role_user_id_seq'),
    user_id      BIGINT NOT NULL,
    role_id      BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE,
    UNIQUE (user_id, role_id)
);
--rollback DROP TABLE roles_users; DROP SEQUENCE roles_users_role_user_id_seq;


--changeset alexandr:2025-06-16--04-create-movies-table
CREATE SEQUENCE movies_movie_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE movies
(
    movie_id            BIGINT PRIMARY KEY DEFAULT nextval('movies_movie_id_seq'),
    title               VARCHAR(255) NOT NULL UNIQUE,
    description         TEXT,
    duration_in_minutes INTEGER      NOT NULL,
    release_date        DATE         NOT NULL,
    rating              VARCHAR(255) NOT NULL,
    poster_url          VARCHAR(2048)
);
--rollback DROP TABLE movies; DROP SEQUENCE movies_movie_id_seq;


--changeset alexandr:2025-06-16--05-create-genres-table
CREATE SEQUENCE genres_genre_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE genres
(
    genre_id BIGINT PRIMARY KEY DEFAULT nextval('genres_genre_id_seq'),
    name     VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);
--rollback DROP TABLE genres; DROP genres_genre_id_seq;


--changeset alexandr:2025-06-16--06-create-genres-movies-table
CREATE SEQUENCE genres_movies_genre_movie_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE genres_movies
(
    genre_movie_id BIGINT PRIMARY KEY DEFAULT nextval('genres_movies_genre_movie_id_seq'),
    movie_id       BIGINT NOT NULL,
    genre_id       BIGINT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE,
    UNIQUE (movie_id, genre_id)
);
--rollback DROP TABLE genres_movies; DROP SEQUENCE genres_movies_genre_movie_id_seq;


--changeset alexandr:2025-06-16--07-create-halls-table
CREATE SEQUENCE halls_hall_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE halls
(
    hall_id      BIGINT PRIMARY KEY DEFAULT nextval('halls_hall_id_seq'),
    name         VARCHAR(255) NOT NULL UNIQUE,
    sound_system VARCHAR(255) NOT NULL
);
--rollback DROP TABLE halls; DROP halls_hall_id_seq;


--changeset alexandr:2025-06-16--08-create-seats-table
CREATE SEQUENCE seats_seat_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE seats
(
    seat_id     BIGINT PRIMARY KEY DEFAULT nextval('seats_seat_id_seq'),
    hall_id     BIGINT       NOT NULL,
    row_number  INTEGER      NOT NULL,
    seat_number INTEGER      NOT NULL,
    type        VARCHAR(255) NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES halls (hall_id) ON DELETE CASCADE,
    UNIQUE (hall_id, row_number, seat_number)
);
--rollback DROP TABLE seats; DROP seats_seat_id_seq;


--changeset alexandr:2025-06-16--09-create-sessions-table
CREATE SEQUENCE sessions_session_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE sessions
(
    session_id BIGINT PRIMARY KEY DEFAULT nextval('sessions_session_id_seq'),
    movie_id   BIGINT    NOT NULL,
    hall_id    BIGINT    NOT NULL,
    start_time TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (hall_id) REFERENCES halls (hall_id) ON DELETE CASCADE
);
--rollback DROP TABLE sessions; DROP sessions_session_id_seq;


--changeset alexandr:2025-06-16--10-create-session-seats-table
CREATE SEQUENCE session_seats_session_seat_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE session_seats
(
    session_seat_id BIGINT PRIMARY KEY DEFAULT nextval('session_seats_session_seat_id_seq'),
    session_id      BIGINT         NOT NULL,
    seat_id         BIGINT         NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    status          VARCHAR(255)   NOT NULL,
    FOREIGN KEY (session_id) REFERENCES sessions (session_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats (seat_id) ON DELETE CASCADE,
    UNIQUE (session_id, seat_id)
);
--rollback DROP TABLE session_seats; DROP SEQUENCE session_seats_session_seat_id_seq;


--changeset alexandr:2025-06-16--11-create-tickets-table
CREATE SEQUENCE tickets_ticket_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE tickets
(
    ticket_id        BIGINT PRIMARY KEY DEFAULT nextval('tickets_ticket_id_seq'),
    user_id          BIGINT    NOT NULL,
    session_seats_id BIGINT    NOT NULL UNIQUE,
    booking_time     TIMESTAMP NOT NULL,
    qr_code          TEXT      NOT NULL,
    used             BOOLEAN   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_info (user_id) ON DELETE CASCADE,
    FOREIGN KEY (session_seats_id) REFERENCES session_seats (session_seat_id) ON DELETE CASCADE
);
--rollback DROP TABLE tickets; DROP tickets_ticket_id_seq;


--changeset alexandr:2025-06-16--12-create-tickets-table
CREATE SEQUENCE payments_payment_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE payments
(
    payment_id   BIGINT PRIMARY KEY DEFAULT nextval('payments_payment_id_seq'),
    user_id      BIGINT         NOT NULL,
    ticket_id    BIGINT         NOT NULL,
    amount       DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(255)   NOT NULL,
    payment_date TIMESTAMP      NOT NULL
);
--rollback DROP TABLE payments; DROP payments_payment_id_seq
