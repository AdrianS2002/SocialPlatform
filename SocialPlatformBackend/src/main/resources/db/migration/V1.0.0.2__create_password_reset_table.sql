
CREATE TABLE password_reset_tokens (
                                       id SERIAL PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL,
                                       code VARCHAR(255) NOT NULL,
                                       created_at TIMESTAMP NOT NULL,
                                       used BOOLEAN NOT NULL
);
