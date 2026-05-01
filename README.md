# 🏦 Vulnerable Bank Application (AppSec Training Lab)

## 📌 Overview

This project is a **deliberately vulnerable banking system** designed for hands-on **Application Security (AppSec)
training**.

It simulates a real-world microservices environment where users can:

* explore vulnerabilities
* practice exploitation techniques
* analyze insecure system behavior

The application intentionally contains flaws across multiple layers (API, authentication, data handling, and
infrastructure).

---

## 🎯 Purpose

This project is meant to:

* Teach **offensive security techniques**
* Simulate **real-world insecure architecture**
* Encourage use of tools like:

    * Burp Suite / OWASP ZAP
    * Nmap
    * Semgrep
    * Trivy
* Serve as a base for **DevSecOps and security experimentation**

---

## 🏗️ Architecture

The system follows a **microservices architecture**:

* **API Gateway**

    * Entry point for all requests

* **Auth Service**

    * Handles authentication
    * Intentionally weak logic

* **Data Service**

    * Handles database operations
    * Exposes vulnerable endpoints

* **Event Service (Kafka)**

    * Processes asynchronous events
    * Stores event data
    * Potential attack surface for message manipulation

* **MariaDB**

    * Main data storage

---

## ⚙️ Technologies

* Java 21
* Spring Boot
* Maven (multi-module)
* MariaDB
* Liquibase
* Docker & Docker Compose
* Apache Kafka
* GitHub Actions (security pipeline)

---

## 🚀 Getting Started

### Prerequisites

* Java 21
* Maven
* Docker

---

### 🔧 Build

```bash
mvn clean install
```

---

### 🐳 Run

```bash
docker compose up --build
```

---

## 🌐 Services

| Service      | Port                |
|--------------|---------------------|
| API Gateway  | 8080                |
| Auth Service | 8081                |
| Data Service | 8082                |
| Kafka UI     | 8090 *(if enabled)* |
| MariaDB      | 3306                |

---

## 🔐 Automated Security Scanning

This project includes a **DevSecOps pipeline** with automated scans:

| Tool       | Purpose                         |
|------------|---------------------------------|
| Semgrep    | Static analysis (SAST)          |
| Gitleaks   | Secret detection                |
| TruffleHog | Deep secret scanning            |
| OWASP ZAP  | Dynamic testing (DAST)          |
| Nmap       | Network discovery               |
| Trivy      | Container & dependency scanning |

📦 After each pipeline run:
Download the **`security-reports` artifact** to review findings.

---

## 🧨 Included Vulnerabilities

This system intentionally includes:

* Plain-text password storage
* Weak authentication
* Missing authorization (IDOR)
* No input validation
* SQL injection potential
* Sensitive data exposure
* Insecure inter-service communication
* Kafka/event manipulation risks
* No rate limiting

---

## 🧪 Training Approach

This project is designed to be explored, not guided.

You are expected to:

* Analyze the codebase
* Intercept and inspect traffic
* Discover endpoints manually
* Use security tools to map the attack surface

---

## 📚 Learning Goals

* Understand vulnerabilities in microservices
* Practice real attack techniques
* Learn how security tools detect issues
* Explore DevSecOps pipelines in practice

---

## ⚠️ Security Disclaimer

🚨 This application is **intentionally vulnerable**.

* Do NOT deploy in production
* Do NOT expose publicly
* Use only in isolated environments

---

## 🤝 Contribution

Contributions are welcome:

* Add new vulnerabilities
* Expand services (FTP, file storage, messaging)
* Improve attack scenarios
* Enhance security tooling

---

## 📄 License

For educational purposes only. Use responsibly.
