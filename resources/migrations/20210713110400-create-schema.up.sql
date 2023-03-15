CREATE TABLE users (
    id           SMALLSERIAL,
    email        VARCHAR(64) NOT NULL,
    password     VARCHAR(1000) NOT NULL,
    username     VARCHAR(32) NOT NULL,
    first_name   VARCHAR(32) NOT NULL,
    middle_name  VARCHAR(32),
    last_name    VARCHAR(32) NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE,
    is_deleted   BOOLEAN DEFAULT FALSE,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
--;;
