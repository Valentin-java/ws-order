CREATE TABLE IF NOT EXISTS "ws-order-management"."ws01_order_photo" (
    id BIGSERIAL NOT NULL,
    order_id BIGINT NOT NULL,
    photo_data BYTEA NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    );