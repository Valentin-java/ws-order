CREATE TABLE IF NOT EXISTS "ws-order-management"."ws01_message" (
    id BIGSERIAL NOT NULL,
    sender_id BIGINT NOT NULL,
    bid_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
    );
