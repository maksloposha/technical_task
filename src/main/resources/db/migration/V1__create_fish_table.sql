CREATE TABLE IF NOT EXISTS fishstore.fish
(
    id              INT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(255) NULL,
    price DOUBLE NOT NULL,
    catch_date      datetime NULL,
    image_file_name VARCHAR(255) NULL,
    CONSTRAINT pk_fish PRIMARY KEY (id)
);
