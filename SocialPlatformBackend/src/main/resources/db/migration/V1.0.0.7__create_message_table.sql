CREATE TABLE message (
                         id          SERIAL PRIMARY KEY,
                         sender_id   INTEGER NOT NULL,
                         receiver_id INTEGER NOT NULL,
                         content     TEXT    NOT NULL,
                         sent_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_message_sender   FOREIGN KEY(sender_id)   REFERENCES "user"(id) ON DELETE CASCADE,
                         CONSTRAINT fk_message_receiver FOREIGN KEY(receiver_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE INDEX idx_message_pair_sent_at
    ON message((LEAST(sender_id, receiver_id)), (GREATEST(sender_id, receiver_id)), sent_at);
