# 💰 Budget Monitoring System

A full-featured **personal finance manager** that allows users to create budgets, track expenses, generate reports, and analyze spending — all through an intuitive JavaFX frontend and a powerful Spring Boot backend.

## 📸 Interface Preview

> _Add screenshots in this folder and link them here!_

| Dashboard | Expense Chart | Add Product |
|----------|---------------|--------------|
| ![Dashboard](screenshots/dashboard.png) | ![Pie Chart](screenshots/pie_chart.png) | ![Add Product](screenshots/add_product.png) |

---

## 🧩 Key Features

- 🔐 Secure account management (hashed passwords with SHA-256)
- 💳 Budget and card management
- 📦 Expense tracking (add, edit, delete)
- 📅 Filter by date/category
- 📊 Interactive reports (e.g., pie charts by category)
- ⚙️ Admin dashboard for user and category management

---

## 🏗️ Architecture Overview

A **3-layered MVC architecture**:

- **Frontend**: JavaFX / Angular (future support)
- **Backend**: Spring Boot REST API with services and repositories
- **Database**: PostgreSQL (via Spring Data JPA)

![Architecture Diagram](screenshots/architecture_diagram.png)

---

## 🧠 Design Patterns

This project applies multiple **GoF design patterns**:

- **Strategy Pattern** – Flexible budget calculation logic
- **Singleton Pattern** – Centralized utility class for security
- **Factory Method Pattern** – DTO object generation

---

## 🧪 Testing & Quality

- ✔️ JUnit 5 with Mockito for business logic testing
- ✔️ Maven Surefire for automated test runs
- ✔️ Isolation of layers to support unit-level robustness

Example:

```java
@Test
void testSaveUser() {
    when(userRepo.save(any(User.class))).thenReturn(user);
    User result = userService.save(user);
    assertEquals("john_doe", result.getUsername());
}
