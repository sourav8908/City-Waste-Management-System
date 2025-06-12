const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');
const path = require('path');

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'src/main/webapp')));

// MySQL Connection
const db = mysql.createConnection({
  host: process.env.MYSQL_HOST || 'localhost',
  user: process.env.MYSQL_USER || 'root',
  password: process.env.MYSQL_PASSWORD || 'root',
  database: process.env.MYSQL_DATABASE || 'waste_management'
});

// Connect to MySQL
db.connect(err => {
  if (err) {
    console.error('Error connecting to MySQL:', err);
    return;
  }
  console.log('Connected to MySQL database');
});

// Routes
app.post('/api/register', (req, res) => {
  const { username, password, name, address, phoneNumber, email } = req.body;
  
  // Log only non-sensitive information
  console.log('New registration attempt for username:', username);
  
  // Insert user into database with correct column names
  const user = {
    username: username,
    password: password,
    name: name,
    address: address,
    phone_number: phoneNumber,
    email: email,
    role: 'CITIZEN'
  };
  
  const query = 'INSERT INTO users SET ?';
  
  db.query(query, user, (err, result) => {
    if (err) {
      console.error('Registration error:', err.code);
      let errorMessage = 'Registration failed';
      
      // Provide more specific error messages
      if (err.code === 'ER_DUP_ENTRY') {
        errorMessage = 'Username already exists. Please choose a different one.';
      }
      
      return res.status(500).json({ 
        success: false, 
        message: errorMessage
      });
    }
    
    console.log('User registered successfully with ID:', result.insertId);
    res.status(201).json({ 
      success: true, 
      message: 'Registration successful!'
    });
  });
});

// Login endpoint
app.post('/api/login', (req, res) => {
  const { username, password } = req.body;
  
  // Log only username, not password
  console.log('Login attempt for username:', username);
  
  // Query to find user
  const query = 'SELECT * FROM users WHERE username = ? AND password = ?';
  
  db.query(query, [username, password], (err, results) => {
    if (err) {
      console.error('Login error:', err.code);
      return res.status(500).json({ 
        success: false, 
        message: 'Login failed'
      });
    }
    
    if (results.length === 0) {
      console.log('Failed login attempt for username:', username);
      return res.status(401).json({ 
        success: false, 
        message: 'Invalid username or password' 
      });
    }
    
    const user = results[0];
    
    // Don't send password to client
    delete user.password;
    
    console.log('Successful login for username:', username);
    res.json({ 
      success: true, 
      message: 'Login successful', 
      user: user 
    });
  });
});

// Get complaints for a user
app.get('/api/complaints/user/:userId', (req, res) => {
  const userId = req.params.userId;
  
  const query = 'SELECT * FROM complaints WHERE user_id = ?';
  
  db.query(query, [userId], (err, results) => {
    if (err) {
      console.error('Error fetching complaints:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Failed to fetch complaints', 
        error: err.message 
      });
    }
    
    res.json({ 
      success: true, 
      complaints: results 
    });
  });
});

// Get all complaints (for admin)
app.get('/api/complaints', (req, res) => {
  const query = `
    SELECT c.*, u.first_name, u.last_name, u.email, u.phone, u.address 
    FROM complaints c 
    JOIN users u ON c.user_id = u.id
  `;
  
  db.query(query, (err, results) => {
    if (err) {
      console.error('Error fetching all complaints:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Failed to fetch complaints', 
        error: err.message 
      });
    }
    
    res.json({ 
      success: true, 
      complaints: results 
    });
  });
});

// Add a new complaint
app.post('/api/complaints', (req, res) => {
  const { userId, complaintType, description } = req.body;
  
  const complaint = {
    user_id: userId,
    complaint_type: complaintType,
    description: description,
    status: 'PENDING',
    created_date: new Date()
  };
  
  const query = 'INSERT INTO complaints SET ?';
  
  db.query(query, complaint, (err, result) => {
    if (err) {
      console.error('Error adding complaint:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Failed to add complaint', 
        error: err.message 
      });
    }
    
    res.status(201).json({ 
      success: true, 
      message: 'Complaint added successfully!',
      complaintId: result.insertId
    });
  });
});

// Update complaint status
app.put('/api/complaints/:id/status', (req, res) => {
  const complaintId = req.params.id;
  const { status } = req.body;
  
  let updateQuery = 'UPDATE complaints SET status = ? WHERE id = ?';
  let queryParams = [status, complaintId];
  
  // If status is RESOLVED, set resolved_date
  if (status === 'RESOLVED') {
    updateQuery = 'UPDATE complaints SET status = ?, resolved_date = ? WHERE id = ?';
    queryParams = [status, new Date(), complaintId];
  }
  
  db.query(updateQuery, queryParams, (err, result) => {
    if (err) {
      console.error('Error updating complaint status:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Failed to update complaint status', 
        error: err.message 
      });
    }
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ 
        success: false, 
        message: 'Complaint not found' 
      });
    }
    
    res.json({ 
      success: true, 
      message: `Complaint status updated to ${status}` 
    });
  });
});

// Delete a complaint
app.delete('/api/complaints/:id', (req, res) => {
  const complaintId = req.params.id;
  
  const query = 'DELETE FROM complaints WHERE id = ?';
  
  db.query(query, [complaintId], (err, result) => {
    if (err) {
      console.error('Error deleting complaint:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Failed to delete complaint', 
        error: err.message 
      });
    }
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ 
        success: false, 
        message: 'Complaint not found' 
      });
    }
    
    res.json({ 
      success: true, 
      message: 'Complaint deleted successfully' 
    });
  });
});

// Start server
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
}); 