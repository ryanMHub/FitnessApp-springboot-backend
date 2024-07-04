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
INSERT INTO roles (name) VALUES ('ROLE_USER');

-- Create the user_roles table with foreign key constraints
CREATE TABLE user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    -- Ensure username exists in users table and delete corresponding user_roles on user deletion
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    -- Ensure role exists in roles table and delete corresponding user_roles on role deletion
    FOREIGN KEY (role) REFERENCES roles(name) ON DELETE CASCADE
);
