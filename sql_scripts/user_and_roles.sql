USE fitness_app;

-- Create the users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    not_expired BOOLEAN DEFAULT TRUE NOT NULL,
    not_locked BOOLEAN DEFAULT TRUE NOT NULL,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

-- Create the roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert a default role
INSERT INTO roles (name) VALUES ('GUEST');
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

-- Create the user_roles table with foreign key constraints
USE fitness_app;

CREATE TABLE user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_ID BIGINT NOT NULL,
    role_ID BIGINT NOT NULL,
    -- Ensure username exists in users table and delete corresponding user_roles on user deletion
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    -- Ensure role exists in roles table and delete corresponding user_roles on role deletion
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Create the Token table
USE fitness_app;

CREATE TABLE Token (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   token VARCHAR(2048) NOT NULL,
   tokenType VARCHAR(50) NOT NULL DEFAULT 'BEARER',
   revoked BOOLEAN NOT NULL,
   expired BOOLEAN NOT NULL,
   user_id BIGINT,  -- Foreign key to the Users table
   CONSTRAINT fk_user
       FOREIGN KEY (user_id)
           REFERENCES Users(id)
           ON DELETE CASCADE
);
