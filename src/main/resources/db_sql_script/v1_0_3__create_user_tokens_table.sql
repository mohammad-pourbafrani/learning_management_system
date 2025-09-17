DROP TABLE IF EXISTS user_tokens CASCADE;

-- Create table
CREATE TABLE user_tokens
(
    id                 SERIAL PRIMARY KEY,                  -- unique ID for the token
    user_id            INT       NOT NULL,                  -- reference to the users table
    refresh_token      TEXT      NOT NULL UNIQUE,           -- refresh token string
    access_token       TEXT      NOT NULL UNIQUE,           -- optional: store access token if needed
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the token was created
    refresh_expires_at TIMESTAMP NOT NULL,                  -- expiration date/time of refresh token
    access_expires_at   TIMESTAMP NOT NULL,                  -- expiration date/time of access token
    revoked            BOOLEAN   DEFAULT FALSE,             -- if token is revoked
    CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);