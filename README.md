# Budget Monitoring System

The Budget Monitoring System is a cross-platform budgeting application that empowers users to manage their personal finances with clarity and control. Designed with a modern UI and solid backend architecture, it offers features like account creation, budget assignment, categorized expense tracking, reporting with graphs, and admin tools — built using JavaFX + Spring Boot and migrated to Angular + Node.js for web support.

---

## Interface Preview

| Landing Page | Add Expense | View Chart |
|--------------|-------------|------------|
| ![Landing](screenshots/landing.png) | ![Add Product](screenshots/add_product.png) | ![Pie Chart](screenshots/pie_chart.png) |

| Profile | Spending Trends | Admin Panel |
|--------|------------------|-------------|
| ![Profile](screenshots/profile.png) | ![Trends](screenshots/spending_trends.png) | ![Admin](screenshots/admin_activity.png) |

---

## Key Features

- Secure login and registration with password hashing (SHA-256)
- Multiple budgets and cards per user
- Expense operations: add, edit, delete products linked to cards
- Expense filtering by category and date
- Dynamic reports: pie charts and monthly trends
- Admin dashboard: manage users and categories, track activity logs
- Chat and activity logs: internal messaging and login/logout history
- Angular UI with responsive design

---

## Architecture Overview

The application follows a layered MVC structure:

- Frontend: Angular (web), JavaFX (desktop legacy)
- Backend: Spring Boot (Java), Node.js (experimental)
- Persistence: PostgreSQL using Spring Data JPA

### System Layers

- Presentation Layer: Angular, UI services
- Business Layer: Spring services, DTOs, validation logic
- Data Layer: Entity relationships, repositories

![Architecture Diagram](screenshots/architecture_diagram.png)

---

## Design and Modeling Highlights

- UML Class Diagram with core entities, services, DTOs, and controllers
- Applied GoF Design Patterns:
  - Strategy Pattern – for flexible budgeting logic
  - Singleton Pattern – centralized security utility (e.g., password hashing)
  - Factory Method Pattern – for dynamic creation of DTOs

### Interaction Models

- Sequence and communication diagrams for:
  - User sign-up
  - Product addition with automatic budget update

---

## Testing and Quality Assurance

- JUnit 5 with Mockito for unit testing
- Service-level tests for:
  - UserService
  - BudgetService
  - ProductService
  - Admin and Category services
- Testing strategy uses Arrange-Act-Assert (AAA) structure
- Maven Surefire Plugin for automated test runs

#### Example:

```java
@Test
void testSaveUser() {
    when(userRepo.save(any(User.class))).thenReturn(user);
    User result = userService.save(user);
    assertEquals("john_doe", result.getUsername());
}
