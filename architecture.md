# 🏗️ Architecture & Deployment Decisions

This document outlines the architectural decisions, rationale, trade-offs, and deployment strategy for the Blog API backend application.

---

## 📦 Tech Stack Overview

| Layer         | Technology            | Reason                                                                 |
|---------------|------------------------|------------------------------------------------------------------------|
| Language      | Java 21                | Strong typing, mature ecosystem, ideal for robust backend APIs        |
| Framework     | Spring Boot 3          | Production-ready, built-in security, easy REST API creation           |
| Authentication| Spring Security + JWT | Stateless and scalable, ideal for distributed environments            |
| Database      | MySQL                  | Widely used, reliable, and cloud-compatible                           |
| Containerization | Docker              | Consistent builds and deployments, platform-agnostic                  |
| Deployment    | Railway.com            | Simplifies CI/CD, HTTPS, and managed DB setup                         |

---

## 🌐 Deployment Platform

### ✅ Platform: [Railway](https://blog-api-prod.up.railway.app/)

**Why Railway?**
- GitHub-connected with CI/CD
- Managed MySQL with GUI access
- Docker support for custom builds
- Auto HTTPS and deployment monitoring

**Usage Summary:**
- Backend is containerized and deployed via Docker
- Environment variables used for credentials and config
- Exposes port `8080` and handles incoming traffic

---

## 🧱 Application Architecture

### 🔹 High-Level Flow


### 🔹 Design Principles
- **Stateless**: JWT-based auth, no server-side session
- **Role-Based Access Control**: `NON_EMPLOYEE`, `EMPLOYEE`, `ADMIN`
- **DTO Mapping**: Avoids leaking domain models
- **Layered**: Separation of concerns between controller, service, and persistence

---

## 🐳 Docker Architecture

### Services

- **App** (`blog-app`)
   - Built from `Dockerfile`
   - Exposes `8080`
- **DB** (`mysql`)
   - MySQL 8.0
   - Exposes `3306`
   - Uses persistent volume

### Volumes

- `db_data`: Persists MySQL data across container restarts

### Benefits

- Cross-platform dev environment
- Easy migration to production or staging
- Consistent environment for testing and deployment

---

## 🔐 Security Model

- **Authentication**: JWT tokens, passed in `Authorization: Bearer <token>`
- **Authorization**: Spring Security roles and `@PreAuthorize` checks
- **Password Storage**: BCrypt hashing
- **Secrets Management**: Via environment variables in Railway

---

## 📈 Scaling Strategy

### Horizontal Scaling

- Stateless API → multiple instances with load balancer
- Platform auto-scales containers on load (Railway)

### Database Scaling

- Use Railway’s managed MySQL

### Fault Tolerance

- Platform handles restarts
- Graceful error handling built into service layer

---

## ⚖️ Architectural Trade-offs

| Decision           | Benefit                          | Limitation                        |
|--------------------|----------------------------------|-----------------------------------|
| Railway deployment | Simple, quick setup              | Limited DB/network tuning options |
| JWT auth           | Stateless, scalable              | Requires extra logic              |
| MySQL              | Familiar, reliable               | Less flexible schema vs NoSQL     |
| Dockerized         | Portable, replicable             |                                   |

---

## 🔚 Summary

This system is designed to be:

- ✅ Easy to deploy
- ✅ Cleanly separated by responsibility
- ✅ Scalable under moderate load
- ✅ Secure with role-based access
- ✅ Suitable for production with minimal changes

With additional enhancements (like Redis caching, observability, and rate limiting), it can support larger workloads and be production-ready for external use.

