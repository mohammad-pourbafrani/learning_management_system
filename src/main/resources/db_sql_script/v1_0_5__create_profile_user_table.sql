DROP TABLE IF EXISTS profile CASCADE;

-- Create table
CREATE TABLE profile
(
    id                  SERIAL PRIMARY KEY, -- unique ID for the profile
    user_id             INT NOT NULL,       -- reference to the users table
    education_degree_id INT,
    education_title_id  INT,
    first_name          VARCHAR(50),
    last_name           VARCHAR(50),
    bio                 TEXT,
    avatar              VARCHAR(255),       -- url image profile
    date_of_birth       DATE,
    gender              VARCHAR(20) DEFAULT 'MALE' CHECK (gender IN ('MALE', 'FEMALE')),
    created_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profile FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_education_degree FOREIGN KEY (education_degree_id) REFERENCES education_degree (id) ON DELETE SET NULL,
    CONSTRAINT fk_education_title FOREIGN KEY (education_title_id) REFERENCES education_title (id) ON DELETE SET NULL
);

--create trigger on updated_at
DROP TRIGGER IF EXISTS update_profile_updated_at ON profile;
CREATE TRIGGER update_profile_updated_at
    BEFORE UPDATE
    ON profile
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
