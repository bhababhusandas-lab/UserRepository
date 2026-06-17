# Task Management API

A comprehensive Spring Boot REST API for managing users, tasks, and analytics with a clean layered architecture.

## Features

✅ **User Management** - Create, read, update, delete users
✅ **Task Management** - Full CRUD operations for tasks
✅ **Filtering & Sorting** - Filter tasks by status and priority with pagination
✅ **Analytics** - Get task statistics per user and across all users
✅ **Input Validation** - Request validation using Jakarta Bean Validation
✅ **Exception Handling** - Centralized error handling with meaningful messages
✅ **Clean Architecture** - Controller → Service → Repository layer pattern

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **MySQL 8.0 / PostgreSQL 14+**
- **Lombok**
- **Maven**

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    priority VARCHAR(50) DEFAULT 'MEDIUM',
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## API Endpoints

### User Management

#### Create User
```
POST /api/users
Content-Type: application/json

{
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "secure123"
}
```

#### Get User by ID
```
GET /api/users/{id}
```

#### Get User by Email
```
GET /api/users/email/{email}
```

#### Get All Users
```
GET /api/users
```

#### Update User
```
PUT /api/users/{id}
Content-Type: application/json

{
  "email": "john.updated@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "secure123"
}
```

#### Delete User
```
DELETE /api/users/{id}
```

### Task Management

#### Create Task
```
POST /api/tasks
Content-Type: application/json

{
  "title": "Complete project",
  "description": "Finish the Spring Boot project",
  "status": "PENDING",
  "priority": "HIGH",
  "userId": 1,
  "dueDate": "2024-12-31T23:59:59"
}
```

#### Get Task by ID
```
GET /api/tasks/{id}
```

#### Get Tasks by User (with Pagination)
```
GET /api/tasks/user/{userId}?page=0&size=10&sortBy=createdAt&sortDirection=desc
```

#### Filter and Sort Tasks
```
GET /api/tasks/user/{userId}/filter?status=PENDING&priority=HIGH&page=0&size=10&sortBy=dueDate&sortDirection=asc
```

Query Parameters:
- `status` (optional): PENDING, IN_PROGRESS, COMPLETED
- `priority` (optional): LOW, MEDIUM, HIGH
- `page` (default: 0): Page number for pagination
- `size` (default: 10): Number of items per page
- `sortBy` (default: createdAt): Field to sort by
- `sortDirection` (default: desc): asc or desc

#### Update Task
```
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Complete project",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "userId": 1,
  "dueDate": "2024-12-31T23:59:59"
}
```

#### Delete Task
```
DELETE /api/tasks/{id}
```

### Analytics

#### Get Analytics for Specific User
```
GET /api/analytics/user/{userId}
```

Response:
```json
{
  "userId": 1,
  "userName": "John Doe",
  "totalTasks": 10,
  "completedTasks": 6,
  "pendingTasks": 2,
  "inProgressTasks": 2,
  "highPriorityTasks": 3,
  "mediumPriorityTasks": 5,
  "lowPriorityTasks": 2,
  "completionPercentage": 60.0
}
```

#### Get Analytics for All Users
```
GET /api/analytics
```

## Project Structure

```
src/main/java/com/taskmanagement/
├── controller/
│   ├── UserController.java
│   ├── TaskController.java
│   └── AnalyticsController.java
├── service/
│   ├── UserService.java
│   ├── TaskService.java
│   └── AnalyticsService.java
├── repository/
│   ├── UserRepository.java
│   └── TaskRepository.java
├── entity/
│   ├── User.java
│   └── Task.java
├── dto/
│   ├── UserDTO.java
│   ├── TaskDTO.java
│   ├── CreateUserRequest.java
│   ├── CreateTaskRequest.java
│   └── TaskAnalyticsDTO.java
├── exception/
│   └── GlobalExceptionHandler.java
└── TaskManagementApplication.java
```

## Setup & Running

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 or PostgreSQL 14+

### Configuration

Edit `src/main/resources/application.properties`:

```properties
# For MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/task_management_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# For PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/task_management_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
```

### Build & Run

```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# Access the API
http://localhost:8080/api
```

## Future Enhancements

- [ ] User authentication and authorization (JWT)
- [ ] Role-based access control
- [ ] Advanced search and filtering
- [ ] Task comments and attachments
- [ ] Email notifications
- [ ] API documentation with Swagger/OpenAPI
- [ ] Unit and integration tests
- [ ] Deployment with Docker
- [ ] CI/CD pipeline

## License

This project is open source and available under the MIT License.