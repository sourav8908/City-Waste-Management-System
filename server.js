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
  host: 'localhost',
  user: 'root',      // Replace with your MySQL username
  password: 'root',      // Replace with your MySQL password
  database: 'waste_management'
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
  
  console.log('Registration attempt:', { username, name, address, phoneNumber, email });
  
  // Split name into first_name and last_name (simple split by first space)
  const nameParts = name.split(' ');
  const firstName = nameParts[0];
  const lastName = nameParts.slice(1).join(' ') || '';
  
  // Insert user into database
  const user = {
    first_name: firstName,
    last_name: lastName,
    username: username,
    email: email,
    phone: phoneNumber,
    address: address,
    password: password,
    role: 'CITIZEN'
  };
  
  console.log('User data to insert:', user);
  
  const query = 'INSERT INTO users SET ?';
  
  db.query(query, user, (err, result) => {
    if (err) {
      console.error('Error registering user:', err);
      let errorMessage = 'Registration failed';
      
      // Provide more specific error messages
      if (err.code === 'ER_DUP_ENTRY') {
        errorMessage = 'Username already exists. Please choose a different one.';
      }
      
      return res.status(500).json({ 
        success: false, 
        message: errorMessage,
        error: err.message,
        code: err.code
      });
    }
    
    console.log('User registered:', result.insertId);
    res.status(201).json({ 
      success: true, 
      message: 'Registration successful!',
      userId: result.insertId
    });
  });
});

// Login endpoint
app.post('/api/login', (req, res) => {
  const { username, password } = req.body;
  
  console.log('Login attempt:', username, password);
  
  // Query to find user
  const query = 'SELECT * FROM users WHERE username = ? AND password = ?';
  
  db.query(query, [username, password], (err, results) => {
    if (err) {
      console.error('Login error:', err);
      return res.status(500).json({ 
        success: false, 
        message: 'Login failed', 
        error: err.message 
      });
    }
    
    console.log('Login query results:', results);
    
    if (results.length === 0) {
      return res.status(401).json({ 
        success: false, 
        message: 'Invalid username or password' 
      });
    }
    
    const user = results[0];
    
    // Don't send password to client
    delete user.password;
    
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