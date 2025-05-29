
---

# 📄 `architecture.md`


# 🏗️ Architecture & Deployment Notes

This document outlines the deployment decisions and architecture used for the Blog API backend project.

---

## 📦 Tech & Hosting Stack

| Component         | Choice                 |
|------------------|------------------------|
| Backend           | Java + Spring Boot     |
| Authentication    | JWT + Spring Security  |
| Database          | MySQL (Dockerized)     |
| Deployment        | Docker on Render       |
| CI/CD             | GitHub → Render        |

---

## 🌐 Deployment Strategy

### Platform: [Render.com](https://render.com)

**Why Render?**
- Simple Docker-based deployments
- Built-in managed MySQL support
- HTTPS and autoscaling included
- Easy to configure environment variables

---

## 🧱 Docker Architecture

### Containers:

1. **blog-app**
   - Spring Boot app
   - Exposes `8080`
   - Built from `Dockerfile`

2. **blog-db**
   - MySQL 8.0
   - Exposes `3306`
   - Initialized with `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`

### Volumes:

- `db_data`: persistent storage for MySQL

---

## 🌍 Network Flow


JWT tokens are passed via `Authorization: Bearer <token>`. Spring Security handles role checks.

---

## 📈 Scaling Strategy

1. **App Scaling**
    - Horizontal scaling with multiple container instances
    - Stateless design allows for container replication

2. **Database Scaling**
    - Use managed database with vertical scaling
    - Enable backups & read replicas if needed

3. **Caching Layer**
    - Redis for caching blog queries

4. **Load Balancing**
    - Render handles this automatically for web services

---

## 🛡️ Security

- Passwords hashed with BCrypt
- JWT secret stored in environment variables


---

## 📌 Future Improvements

- Add Redis for caching frequently-read posts
- Add rate limiting


---

## 🔚 Summary

This app is production-ready for lightweight traffic, easy to scale and maintain, and has clean separation between components.
