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
   📍 https://blog-api-prod.up.railway.app/


## 🔐 Authentication

### Register


#### POST /api/auth/register

    {
        "username": "john",
        "password": "1234",
        "role": "NON_EMPLOYEE"
    }

Roles can be : NON_EMPLOYEE`, `EMPLOYEE`, `ADMIN

#### POST /api/auth/login
    Returns: JWT token
### 📚 Blog Endpoints



✅ List all approved blogs

#### GET /api/blogs

Response:


    {
        "id": 1,
        "title": "Felix Blogging",
        "content": "Felix first blog and it's this good",
        "authorUsername": "Felix",
        "createdAt": "2025-05-28T14:00:00"
    }
    {
        "id": 2,
        "title": "Messi vs Ronaldo",
        "content": "Can this debate ever end,No!! it's for generation to come",
        "authorUsername": "Pundit",
        "createdAt": "2025-05-28T14:00:00"
    }

✅ Get specific blog

#### GET /api/blogs/{id}

    {
        "id": 1,
        "title": "Felix Blogging",
        "content": "Felix first blog and it's this good",
        "authorUsername": "Felix",
        "createdAt": "2025-05-28T14:00:00"
    }

✍️ Create blog post

#### POST /api/blogs

##### Request Body

    {
    "title": "My First Blog",
    "content": "This is a great post!"
    }
🟡 **If you're a NON_EMPLOYEE, it will be submitted for approval.**

✏️ Update blog post

#### PUT /api/blogs/{id}

    {
    "title": "Updated Title",
    "content": "Updated content."
    }
**🟡 Requires ownership of the post. NON_EMPLOYEE edits must be approved again.**

❌ Request blog deletion

#### DELETE /api/blogs/{id}

**🟡 For NON_EMPLOYEE, this marks the blog for admin approval instead of actual deletion.**

✅ Admin: Approve blog post

#### POST /api/blogs/{id}/approve

✅ Admin: Approve delete request

#### POST /api/blogs/{id}/approve-delete

    {
        "username": "john",
        "password": "1234"
    }



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
🔗 https://blog-api-prod.up.railway.app/

For deployment details, see architecture.md

## ✅ Tests

`./mvnw test`