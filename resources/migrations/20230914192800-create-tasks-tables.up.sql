CREATE TABLE task_types (
    id SMALLSERIAL,
    name VARCHAR(64),
    PRIMARY KEY (id)
);
--;
CREATE TABLE task_statuses (
    id SMALLSERIAL,
    name VARCHAR(64),
    PRIMARY KEY (id)
);
--;;
CREATE TABLE tasks (
    id SERIAL,
    name VARCHAR(64),
    description VARCHAR(1000),
    type_id SMALLINT,
    status_id SMALLINT,
    deadline TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP::TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES task_types(id),
    FOREIGN KEY (status_id) REFERENCES task_statuses(id)
);
--;;
CREATE TABLE tasks_users (
    task_id INT,
    user_id SMALLINT,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
--;;
