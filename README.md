# 🏦 Vulnerable Bank Application (AppSec Training Lab)

## 📌 Overview

This project is a deliberately vulnerable banking application built for **application security training and experimentation**. It is designed using a **microservices architecture** with Java 21, Spring Boot, and MariaDB, and includes intentional security flaws for hands-on learning.

The goal is to simulate real-world insecure systems and practice identifying, exploiting, and eventually fixing vulnerabilities.

---

## 🏗️ Architecture

The system is composed of the following services:

* **API Gateway**

  * Entry point for client requests
  * Routes traffic to backend services

* **Auth Service**

  * Handles login functionality
  * Intentionally weak authentication logic

* **Data Service**

  * Handles all database operations
  * Exposes REST endpoints for CRUD operations

* **MariaDB**

  * Relational database storing application data

---

## ⚙️ Technologies Used

* Java 21
* Spring Boot
* Maven (multi-module)
* MariaDB
* Liquibase (database migrations)
* Docker & Docker Compose

---

## 🚀 Getting Started

### Prerequisites

* Java 21
* Maven
* Docker & Docker Compose

---

### 🔧 Build the Project

```
mvn clean install
```

---

### 🐳 Run the Application

```
docker-compose up --build
```

---

### 🌐 Services & Ports

| Service      | Port |
| ------------ | ---- |
| API Gateway  | 8080 |
| Auth Service | 8081 |
| Data Service | 8082 |
| MariaDB      | 3306 |

---

## 🔌 Example Endpoints

### Login

```
POST /auth/login
```

### Get User

```
GET /api/users/{id}
```

### Create User

```
POST /data/users
```

---

## ⚠️ Security Disclaimer

This application is **intentionally insecure** and should **NOT** be used in production.

---

## 🧨 Included Vulnerabilities

The system includes (but is not limited to):

* Plain-text password storage
* Weak authentication logic
* Missing authorization checks
* Insecure direct object references (IDOR)
* No input validation
* Potential for SQL injection (if extended)
* Sensitive data exposure via APIs
* Hardcoded com.bank.service communication

---

## 🧪 Suggested Training Scenarios

You can use this project to practice:

* Authentication bypass
* Privilege escalation
* Data exposure attacks
* API abuse
* Service-to-com.bank.service exploitation

---

## 📚 Learning Goals

* Understand common web application vulnerabilities
* Practice exploitation techniques
* Learn how microservices impact security
* Compare insecure vs secure implementations

---

## ⚠️ Important Notes

* Run this project in an isolated environment (e.g., local machine or lab VM)
* Do not expose it to the public internet
* Use only for educational and ethical purposes

---

## 🤝 Contribution

This project is intended as a personal or team training lab. Feel free to extend it with additional vulnerabilities, services, or attack scenarios.

---

## 📄 License

This project is provided for educational purposes only. Use responsibly.
