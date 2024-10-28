CREATE TABLE IF NOT EXISTS "ws-order-management"."ws01_bid" (
    id BIGSERIAL NOT NULL,
    order_id BIGINT NOT NULL,
    specialist_id BIGINT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    );
