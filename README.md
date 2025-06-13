# City Waste Management System

A full-stack Node.js and MySQL application for managing city waste complaints, with CI/CD using Jenkins and deployment to AWS EC2.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Local Development Setup](#local-development-setup)
4. [Docker Setup](#docker-setup)
5. [Jenkins CI/CD Pipeline](#jenkins-cicd-pipeline)
6. [AWS EC2 Deployment](#aws-ec2-deployment)
7. [Project Structure](#project-structure)
8. [Module-by-Module Explanation](#module-by-module-explanation)
9. [Troubleshooting](#troubleshooting)
10. [Credits](#credits)

---

## Project Overview

This project is a web-based City Waste Management System. Citizens can register, log in, and submit complaints about waste management issues. Admins can view and manage all complaints.

---

## Features

- User registration and login
- Complaint submission and tracking
- Admin dashboard for complaint management
- RESTful API (Node.js + Express)
- MySQL database
- Frontend: HTML/CSS/JS
- Dockerized for easy deployment
- CI/CD with Jenkins
- Deployment to AWS EC2

---

## Local Development Setup

### 1. **Clone the Repository**
```sh
git clone https://github.com/sourav8908/City-Waste-Management-System.git
cd City-Waste-Management-System/WasteManagementSystem
```

### 2. **Install Node.js Dependencies**
```sh
npm install
```

### 3. **Set Up MySQL Database**
- Install MySQL locally if not already installed.
- Create the database and tables:
  ```sh
  mysql -u root -p < database.sql
  ```
- Update `server.js` if your MySQL credentials are different.

### 4. **Run the Application**
```sh
npm start
```
- Access the app at: [http://localhost:3000](http://localhost:3000)

---

## Docker Setup

### 1. **Build and Run with Docker Compose (Recommended)**
```sh
docker-compose up --build
```
- App: [http://localhost:3000](http://localhost:3000)
- MySQL: localhost:3307

### 2. **Stop Containers**
```sh
docker-compose down
```

---

## Jenkins CI/CD Pipeline

### 1. **Jenkins Setup**
- Install Jenkins (locally or on a server)
- Install required plugins: Docker Pipeline, Git, Credentials Binding

### 2. **Add Credentials**
- Docker Hub credentials (for pushing images)
- AWS EC2 SSH key (for deployment)
- MySQL credentials (if needed)

### 3. **Pipeline Stages**
- **Checkout:** Pull code from GitHub
- **Install Dependencies:** `npm install` (inside Node.js Docker container)
- **Build Docker Image:** `docker build -t <image> .`
- **Push Docker Image:** `docker push <image>`
- **Deploy to EC2:** SSH into EC2 and run Docker commands

### 4. **Trigger Builds**
- Use Poll SCM or GitHub webhook

---

## AWS EC2 Deployment

### 1. **Launch EC2 Instance**
- Use Ubuntu 20.04/22.04/24.04 (Free Tier eligible)
- Open ports 22 (SSH) and 3000 (app) in the security group

### 2. **SSH into EC2**
```sh
ssh -i /path/to/your-key.pem ubuntu@<your-ec2-public-ip>
```

### 3. **Install Docker on EC2**
```sh
sudo apt update
sudo apt install -y docker.io
sudo systemctl start docker
sudo usermod -aG docker ubuntu
exit
# Reconnect:
ssh -i /path/to/your-key.pem ubuntu@<your-ec2-public-ip>
```

### 4. **(First Time Only) Docker Login**
```sh
docker login
```

### 5. **Jenkins Deploy Stage**
- Jenkins will SSH into EC2 and run:
  ```sh
  docker pull <your-dockerhub-username>/waste-management:latest
  docker stop waste-management || true
  docker rm waste-management || true
  docker run -d --name waste-management -p 3000:3000 \
    -e MYSQL_HOST=<mysql-host> \
    -e MYSQL_USER=<mysql-user> \
    -e MYSQL_PASSWORD=<mysql-password> \
    -e MYSQL_DATABASE=waste_management \
    <your-dockerhub-username>/waste-management:latest
  ```

### 6. **Access the App**
- [http://<your-ec2-public-ip>:3000](http://<your-ec2-public-ip>:3000)

---

## Project Structure

```
WasteManagementSystem/
├── server.js
├── package.json
├── Dockerfile
├── docker-compose.yml
├── database.sql
├── src/
│   └── main/webapp/...
├── Jenkinsfile
└── ...
```

---

## Module-by-Module Explanation

### **1. Backend (Node.js/Express)**
- Handles API routes for registration, login, complaints
- Connects to MySQL using `mysql2`
- Serves static frontend files

### **2. Frontend (HTML/CSS/JS)**
- User and admin dashboards
- Registration and login forms
- Uses fetch API to communicate with backend

### **3. Database (MySQL)**
- `users` table: stores user info
- `complaints` table: stores complaints

### **4. Docker**
- `Dockerfile`: Defines how to build the app image
- `docker-compose.yml`: Orchestrates app and MySQL for local dev

### **5. Jenkins Pipeline**
- Automates build, test, Docker image push, and deployment to EC2

### **6. AWS EC2**
- Hosts the production Docker container
- Exposes app to the internet

---

## Troubleshooting

- **App not accessible on EC2:**  
  - Check security group rules (port 3000 open)
  - Check if Docker container is running: `docker ps`
  - Check logs: `docker logs waste-management`
  - Ensure `server.js` uses `0.0.0.0` in `app.listen`

- **Jenkins build fails:**  
  - Check credentials and environment variables
  - Check Docker and Node.js installation on Jenkins agent

- **Database connection issues:**  
  - Ensure correct MySQL host, user, password, and database name

---

## Credits

- Project by Sourav Mohanty
