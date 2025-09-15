--crete table users
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    phone      VARCHAR(15)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    role       VARCHAR(20) DEFAULT 'student' CHECK (role IN ('admin', 'student', 'instructor')),
    password   VARCHAR(255) NOT NULL,
    enable     BOOLEAN     DEFAULT FALSE, -- for OTP verification
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

--create trigger on updated_at
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
