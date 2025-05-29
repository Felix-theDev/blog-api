# 📝 Blog API Backend (Java + Spring Boot)

This is a simple blog backend API for creating, listing, updating, approving, and deleting blog posts with user role-based access control.

## 🚀 Features

- Public access to approved blogs
- Authenticated users can create/update/delete posts
- Non-employees require admin approval for posts/updates/deletions
- Admins can approve or delete submissions
- JWT-based authentication
- MySQL as the database
- Docker + Docker Compose support
- Cloud-ready (Railway deployment)

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3
- Spring Security + JWT
- MySQL
- Docker & Docker Compose
- JUnit + Mockito

---

## 📦 Setup & Run (Local)

### 1. Clone the repository

`git clone https://github.com/felix-theDev/blog-api.git`
`cd blog-api`

### 2. Build the project

`./mvnw clean package`

### 3. Run with Docker Compose

`   docker-compose up --build`

   The app will be running at:
   📍 http://localhost:8080


## 🔐 Authentication

### Register


#### POST /api/auth/register

{
    "username": "john",
    "password": "1234",
    "role": "NON_EMPLOYEE"
}

#### POST /api/auth/login

{
    "username": "john",
    "password": "1234"
}

Returns: JWT token

| Method | Endpoint                         | Access        | Description                            |
|--------|----------------------------------|---------------|----------------------------------------|
| POST   | `/api/auth/register`             | Publice       | Register a new user                    |
| POST   | `/api/auth/login`                | Authenticated | Login an existing user                 |
| GET    | `/api/blogs`                     | Public        | List all approved blog posts           |
| GET    | `/api/blogs/{id}`                | Public        | Get specific blog post by ID           |
| POST   | `/api/blogs`                     | Authenticated | Create blog post                       |
| PUT    | `/api/blogs/{id}`                | Authenticated | Update blog post (only own)            |
| DELETE | `/api/blogs/{id}`                | Authenticated | Request delete (Non-employee approval) |
| POST   | `/api/blogs/{id}/approve`        | Admin only    | Approve a blog post                    |
| POST   | `/api/blogs/{id}/approve-delete` | Admin only    | Approve delete request                 |

## 🐳 Docker Overview

To build and run with Docker:

`docker-compose up --build`

MySQL container runs on localhost:3306

App listens on localhost:8080

## ☁️ Deployment

Live app deployed on Railway:
🔗 https://your-app-name.onrender.com

For deployment details, see architecture.md

## ✅ Tests

`./mvnw test`