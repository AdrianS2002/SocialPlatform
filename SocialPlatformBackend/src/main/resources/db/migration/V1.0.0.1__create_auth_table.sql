CREATE TABLE "auth"
(
    id                 SERIAL PRIMARY KEY,
    email              VARCHAR(191) NOT NULL,
    password           VARCHAR(60) NOT NULL,
    is_validated       BOOLEAN,
    role               VARCHAR(50),
    CONSTRAINT ux_user_login UNIQUE (email)
);