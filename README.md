# City Waste Management System

A comprehensive web application for managing city waste complaints, enabling citizens to report waste management issues and administrators to efficiently track and resolve them.

## Features

### User Features
- **User Registration**: Citizens can register with their details
- **User Login/Logout**: Secure authentication system
- **Raise Complaints**: Submit detailed complaints with location information
- **Photo Evidence**: Upload photos of waste management issues
- **Track Status**: View status updates of submitted complaints
- **User Dashboard**: Overview of all complaints submitted by the user

### Admin Features
- **Admin Login/Logout**: Secure administrative access
- **Complaint Management**: View, update status, and delete complaints
- **Filtering & Sorting**: Filter complaints by status and type
- **Dashboard & Statistics**: View complaint metrics and trends
- **Photo Viewing**: Examine photo evidence submitted by users

## Project Versions

This project comes in two versions:

### 1. HTML/CSS/JavaScript (Client-Side Only)
- Runs entirely in the browser with no server requirements
- Uses localStorage for data persistence
- Perfect for demos and quick testing
- Just open `index.html` in a browser to run

### 2. Node.js/MySQL (Full-Stack)
- Complete server implementation using Node.js and Express
- MySQL database for persistent data storage
- API endpoints for all functionality
- Requires server setup (see README_SERVER.md)

## Technology Stack

### Client-Side Version
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Storage**: Browser's localStorage
- **Photo Storage**: Base64 encoding in localStorage

### Server Version
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Backend**: Node.js, Express
- **Database**: MySQL
- **API**: RESTful API endpoints

## Getting Started

### Running the HTML Version
1. Clone this repository
2. Navigate to the WasteManagementSystem directory
3. Open `src/main/webapp/index.html` in any modern browser
4. No server setup required!

### Running the Node.js/MySQL Version
1. See `README_SERVER.md` for complete setup instructions
2. Requires Node.js and MySQL installed

## Default Login Credentials

### HTML Version
- **Admin**: 
  - Username: admin
  - Password: admin123
- **Sample User**: 
  - Username: user1
  - Password: user123

### MySQL Version
- **Admin**: 
  - Username: admin
  - Password: admin123
- **Sample User**: 
  - Username: citizen1
  - Password: password123

## Project Structure

```
WasteManagementSystem/
├── src/
│   └── main/
│       └── webapp/
│           ├── admin/           # Admin dashboard pages
│           ├── css/             # Stylesheets
│           ├── user/            # User dashboard pages
│           ├── index.html       # Landing page
│           ├── login.html       # Login page
│           └── register.html    # Registration page
├── server.js                    # Node.js server file
├── package.json                 # Node.js dependencies
├── database.sql                 # SQL setup script
├── README.md                    # This file
└── README_SERVER.md             # Server setup instructions
```

## Key Features in Detail

### Location-Based Complaints
Users can specify detailed location information including street, landmark, area, and pincode to help authorities locate the issue accurately.

### Photo Evidence
The system allows users to upload photographic evidence of waste management issues, which helps administrators better understand and prioritize complaints.

### Status Tracking
Complaints progress through three statuses:
- **Pending**: Newly submitted complaints awaiting action
- **In Progress**: Complaints currently being addressed
- **Resolved**: Complaints that have been successfully resolved

### User Verification
Contact information is collected for verification purposes, allowing municipal staff to contact citizens if additional information is needed.

## Development

This project is designed with a simple architecture for easy extension:

- **CSS Variables**: Easy theme customization
- **Modular JavaScript**: Clear separation of concerns
- **Responsive Design**: Works on mobile and desktop devices

## License

This project is released under the MIT License. See the LICENSE file for details.

## Contributors

- Original implementation: [Your Name]
- Design & Enhancement: Claude AI Assistant 