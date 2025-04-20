# Server Setup Guide for Waste Management System

This guide explains how to set up and run the Node.js server that connects the HTML frontend to your MySQL database.

## Prerequisites

1. **Node.js**: Download and install from [nodejs.org](https://nodejs.org/)
2. **MySQL**: Make sure MySQL is installed and running
3. **Waste Management database**: Make sure you've created the database using the provided SQL script

## Setup Instructions

1. **Install Dependencies**

   Open a command prompt in the WasteManagementSystem folder and run:

   ```
   npm install
   ```

   This will install all the required packages defined in package.json.

2. **Configure Database Connection**

   Open `server.js` and update the MySQL connection settings with your credentials:

   ```javascript
   const db = mysql.createConnection({
     host: 'localhost',
     user: 'root',      // Replace with your MySQL username
     password: '',      // Replace with your MySQL password
     database: 'waste_management'
   });
   ```

3. **Start the Server**

   Run the following command to start the server:

   ```
   npm start
   ```

   If you want to automatically restart the server when you make changes (during development), use:

   ```
   npm run dev
   ```

   You should see a message that the server is running at http://localhost:3000.

## How It Works

1. **Registration Process**:
   - When a user registers on the frontend, their data is sent to the `/api/register` endpoint
   - The server splits the name into first_name and last_name
   - User data is stored directly in the MySQL database
   - Upon successful registration, they can login

2. **Login Process**:
   - Login credentials are sent to the `/api/login` endpoint
   - The server checks the MySQL database for matching username and password
   - If successful, user data is sent back to the client

3. **Other Features**:
   - The server provides API endpoints for managing complaints
   - User dashboard data and admin dashboard data are fetched from the database

## Troubleshooting

- **Connection Issues**: Make sure MySQL is running and your credentials are correct
- **Missing Tables**: Verify that you've run the database.sql script to create all required tables
- **Port Conflicts**: If port 3000 is already in use, you can change the port in server.js

## Security Notes

- This is a basic implementation for demonstration purposes
- In a production environment, you should:
  - Use environment variables for database credentials
  - Implement password hashing
  - Add user authentication with JWT or sessions
  - Add input validation and sanitization
  - Use HTTPS 