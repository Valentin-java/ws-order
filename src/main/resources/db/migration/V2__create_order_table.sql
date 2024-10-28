CREATE TABLE IF NOT EXISTS "ws-order-management"."ws01_order" (
    id BIGSERIAL NOT NULL,
    customer_id BIGINT NOT NULL,
    category INTEGER NOT NULL,
    short_description TEXT NOT NULL,
    detailed_description TEXT,
    amount NUMERIC,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    );