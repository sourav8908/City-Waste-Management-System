-- Database creation
CREATE DATABASE IF NOT EXISTS waste_management;
USE waste_management;

-- Drop tables if they exist
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(10) NOT NULL DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create complaints table
CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    complaint_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_date TIMESTAMP NOT NULL,
    resolved_date TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert default admin user
INSERT INTO users (username, password, name, address, phone_number, email, role)
VALUES ('admin', 'admin123', 'Admin User', 'City Municipal Office', '9876543210', 'admin@waste.com', 'admin');

-- Insert sample users
INSERT INTO users (username, password, name, address, phone_number, email, role)
VALUES 
('user1', 'user123', 'John Doe', '123 Main St, Apt 4B', '1234567890', 'john@example.com', 'user'),
('user2', 'user123', 'Jane Smith', '456 Park Ave', '2345678901', 'jane@example.com', 'user'),
('user3', 'user123', 'Robert Johnson', '789 Broadway St', '3456789012', 'robert@example.com', 'user');

-- Insert sample complaints
INSERT INTO complaints (user_id, complaint_type, description, status, created_date)
VALUES 
(2, 'NEW_DUSTBIN', 'Need a new dustbin at the corner of Main St and Park Ave. The area has no waste collection facility.', 'PENDING', NOW() - INTERVAL 5 DAY),
(2, 'DUSTBIN_OVERFLOW', 'The dustbin near Park Ave subway station is overflowing. It has not been emptied for days.', 'IN_PROGRESS', NOW() - INTERVAL 3 DAY),
(3, 'DUSTBIN_DAMAGED', 'The dustbin at Broadway and 5th is damaged. The lid is broken and waste is spilling out.', 'PENDING', NOW() - INTERVAL 2 DAY),
(4, 'DUSTBIN_OVERFLOW', 'Dustbin overflow at Market Street near the shopping mall. Very unhygienic condition.', 'RESOLVED', NOW() - INTERVAL 10 DAY);

-- Set the resolved date for resolved complaints
UPDATE complaints SET resolved_date = NOW() - INTERVAL 1 DAY WHERE status = 'RESOLVED'; 