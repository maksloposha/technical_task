CREATE TABLE IF NOT EXISTS fishstore.roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS fishstore.users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    enabled  BIT(1) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
