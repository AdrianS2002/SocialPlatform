CREATE TABLE "user"
(
    id                 SERIAL PRIMARY KEY,
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    bio                VARCHAR(500),
    is_blocked         BOOLEAN,
    FOREIGN KEY (id) REFERENCES "auth" (id) ON DELETE CASCADE
);