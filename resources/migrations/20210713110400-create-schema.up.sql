CREATE TABLE users (
    id           SMALLSERIAL,
    email        VARCHAR(64) NOT NULL,
    password     VARCHAR(1000) NOT NULL,
    first_name   VARCHAR(32) NOT NULL,
    middle_name  VARCHAR(32),
    last_name    VARCHAR(32) NOT NULL,
    is_active    BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN NOT NULL DEFAULT FALSE,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
--;;
