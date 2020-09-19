CREATE TYPE admin_ui.ROLE_ENUM AS ENUM ('ADMIN', 'BUSINESS');
CREATE CAST (CHARACTER VARYING as admin_ui.ROLE_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS admin_ui.user
(
    email VARCHAR(255)       NOT NULL,
    name  VARCHAR(255)       NOT NULL,
    role  admin_ui.ROLE_ENUM NOT NULL,

    PRIMARY KEY (email)
);


CREATE TYPE admin_ui.CONFIG_TYPE_ENUM AS ENUM ('T1', 'T2', 'T3');
CREATE CAST (CHARACTER VARYING as admin_ui.CONFIG_TYPE_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS admin_ui.example_config_type
(
    id          SERIAL,
    type        admin_ui.CONFIG_TYPE_ENUM NOT NULL,
    description VARCHAR(255)              NOT NULL,

    PRIMARY KEY (id)
);


CREATE TYPE admin_ui.VISIBILITY_ENUM AS ENUM ('PUBLIC', 'PRIVATE');
CREATE CAST (CHARACTER VARYING as admin_ui.VISIBILITY_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS admin_ui.example_config
(
    id                     SERIAL,
    name                   VARCHAR(255)             NOT NULL,
    visibility             admin_ui.VISIBILITY_ENUM NOT NULL,
    create_date            DATE                     NOT NULL,
    active                 BOOLEAN                  NOT NULL,
    example_config_type_id INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (example_config_type_id) REFERENCES admin_ui.example_config_type (id)
);
