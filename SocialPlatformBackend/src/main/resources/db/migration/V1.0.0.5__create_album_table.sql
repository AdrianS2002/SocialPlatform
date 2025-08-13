CREATE TABLE album
(
    id                 SERIAL PRIMARY KEY,
    title              VARCHAR(250) NOT NULL,
    description        VARCHAR(500),
    user_id            INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES  "user" (id) ON DELETE CASCADE
);