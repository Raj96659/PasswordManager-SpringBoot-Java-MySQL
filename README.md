# 🔐 REV Password Manager

A secure, full-stack Password Manager application built using **Spring Boot**, **MySQL**, and **Angular (Signals)**.  
It allows users to securely store, manage, generate, and audit passwords using modern encryption and authentication mechanisms.

This project demonstrates strong security architecture, clean backend design, and modern Angular frontend practices.

---

# 📌 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Security Architecture](#-security-architecture)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Future Improvements](#-future-improvements)
- [Author](#-author)

---

# 📖 Overview

The REV Password Manager provides users with a secure vault to store credentials safely. Each user's vault is encrypted using a key derived from their master password, ensuring maximum data protection.

The application includes:

- Secure authentication  
- Encrypted password storage  
- Password generator  
- Password strength audit  
- Backup & recovery  
- Two-factor authentication  

---

# 🚀 Features

## 🔑 Authentication & Account Management

- User registration with:
  - Username  
  - Email  
  - Master password  
  - Security questions  

- Secure login using JWT authentication  
- OTP-based Two Factor Authentication (2FA)  
- Change master password with vault re-encryption  
- Account recovery using security questions  
- Profile update support  
- Secure logout  

---

## 🔒 Password Vault Management

- Add password entry  
- View stored passwords  
- Decrypt password using master password  
- Update password entry  
- Delete password entry  
- Mark as favorite  
- Add notes to entries  
- Filter by category  
- Sort by name/date  

---

## 🔑 Password Generator

- Generate secure random passwords  
- Custom options:
  - Uppercase letters  
  - Lowercase letters  
  - Numbers  
  - Symbols  
  - Custom length  
  - Exclude similar characters  

- Password strength indicator  
- Save generated password directly to vault  

---

## 🛡 Security Features

- AES-256 encryption for vault passwords  
- PBKDF2WithHmacSHA256 key derivation  
- BCrypt hashing for authentication password  
- Per-user unique encryption salt  
- JWT-based authentication  
- OTP-based verification  
- Weak password detection  
- Reused password detection  
- Password audit support  

---

## 📦 Backup & Recovery

- Export encrypted vault backup  
- Import encrypted vault backup  
- Account recovery using security questions  

---

# 🔐 Security Architecture

The application follows a multi-layer security model.

## Authentication Layer

- BCrypt hashing for login password  
- JWT token authentication  
- Stateless session management  

## Encryption Layer

- Master password never stored in plain text  
- Encryption key derived using PBKDF2  
- AES-256 used for vault encryption  
- Each user has unique encryption salt  

## Verification Layer

- OTP verification for sensitive operations  
- Security questions for account recovery  

---

# 🧱 Tech Stack

## Backend

- Java 17  
- Spring Boot  
- Spring Security  
- Spring Data JPA  
- JWT Authentication  
- AES Encryption  
- PBKDF2 Key Derivation  
- MySQL  

## Frontend

- Angular (Standalone Components)  
- Angular Signals  
- TypeScript  
- HTML  
- CSS  

## Tools

- IntelliJ IDEA  
- VS Code  
- Postman  
- MySQL Workbench  

---


👨‍💻 Author

Raj
Data Science & Software Engineering Enthusiast

⭐ Support

If you like this project, give it a star ⭐ on GitHub.


---

If you want, I can also add **architecture diagram, encryp
