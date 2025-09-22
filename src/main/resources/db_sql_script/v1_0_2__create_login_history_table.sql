DROP TABLE IF EXISTS login_history CASCADE;
CREATE TABLE login_history
(
    id         SERIAL PRIMARY KEY,                                                     -- unique ID for each login attempt
    user_id    INT NOT NULL,                                                           -- reference to the users table
    login_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,                                  -- when the login occurred
    ip_address VARCHAR(45) NOT NULL,                                                   -- IP address (supports IPv6)
    user_agent TEXT,                                                                   -- browser/device info
    status     VARCHAR(20) DEFAULT 'SUCCESS' CHECK (status IN ('SUCCESS', 'FAILURE')), -- login status
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);