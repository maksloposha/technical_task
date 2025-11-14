CREATE TABLE IF NOT EXISTS fishstore.fish_image_file_names
(
    fish_id          INT NOT NULL,
    image_file_names VARCHAR(255) NULL
);

SET @fk_exists = (
    SELECT COUNT(*)
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = 'fishstore'
    AND TABLE_NAME = 'fish_image_file_names'
    AND CONSTRAINT_NAME = 'fk_fish_imagefilenames_on_fish'
);

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE fishstore.fish_image_file_names
     ADD CONSTRAINT fk_fish_imagefilenames_on_fish
     FOREIGN KEY (fish_id) REFERENCES fishstore.fish (id) ON DELETE CASCADE',
    'SELECT "Foreign key already exists" AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'fishstore'
    AND TABLE_NAME = 'fish'
    AND COLUMN_NAME = 'image_file_name'
);

SET @migration_sql = IF(@column_exists > 0,
    'INSERT INTO fishstore.fish_image_file_names (fish_id, image_file_names)
     SELECT id, image_file_name
     FROM fishstore.fish
     WHERE image_file_name IS NOT NULL
     ON DUPLICATE KEY UPDATE image_file_names = VALUES(image_file_names)',
    'SELECT "Column already migrated" AS message'
);

PREPARE stmt FROM @migration_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @drop_sql = IF(@column_exists > 0,
    'ALTER TABLE fishstore.fish DROP COLUMN image_file_name',
    'SELECT "Column already dropped" AS message'
);

PREPARE stmt FROM @drop_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
