CREATE TABLE persons (
    id  SMALLSERIAL,
    first_name   VARCHAR(32) NOT NULL,
    middle_name  VARCHAR(32),
    last_name    VARCHAR(32) NOT NULL,
    birthdate    TIMESTAMP,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
--;;
CREATE TABLE users (
    id           SMALLSERIAL,
    email        VARCHAR(64) NOT NULL UNIQUE,
    password     VARCHAR(1000) NOT NULL,
    first_name   VARCHAR(32) NOT NULL,
    middle_name  VARCHAR(32),
    last_name    VARCHAR(32) NOT NULL,
    is_active    BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN NOT NULL DEFAULT FALSE,
    person_id    SMALLINT UNIQUE,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id),
    FOREIGN KEY (person_id) REFERENCES persons(id)
);
--;;
