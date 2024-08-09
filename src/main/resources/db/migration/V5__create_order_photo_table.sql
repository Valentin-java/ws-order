CREATE TABLE IF NOT EXISTS "ws01_order_photo" (
    id BIGSERIAL NOT NULL,
    photo_data BYTEA NOT NULL,
    order_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES "ws01_order" (id) ON DELETE CASCADE
    );
