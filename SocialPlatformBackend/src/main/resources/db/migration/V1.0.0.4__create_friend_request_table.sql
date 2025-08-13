CREATE TABLE "friend_request"
(
    id          SERIAL PRIMARY KEY,
    sender_id   BIGINT NOT NULL,
    received_id BIGINT NOT NULL,
    status      VARCHAR(20) NOT NULL
);