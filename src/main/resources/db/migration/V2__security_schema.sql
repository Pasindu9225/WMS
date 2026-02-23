-- Requirement: Design Company Group, Company, Warehouse core tables
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(50) UNIQUE NOT NULL,
    group_id VARCHAR(255),
    company_id VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    group_id VARCHAR(255),
    company_id VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
    );