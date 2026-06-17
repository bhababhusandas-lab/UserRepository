# Sample Requests for Task Management API

## 1. USER MANAGEMENT

### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "firstName": "Alice",
    "lastName": "Johnson",
    "password": "password123"
  }'
```

### Get All Users
```bash
curl http://localhost:8080/api/users
```

### Get User by ID
```bash
curl http://localhost:8080/api/users/1
```

### Get User by Email
```bash
curl http://localhost:8080/api/users/email/alice@example.com
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice.updated@example.com",
    "firstName": "Alice",
    "lastName": "Smith",
    "password": "newpassword123"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 2. TASK MANAGEMENT

### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema",
    "description": "Create ER diagram and normalize tables",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "userId": 1,
    "dueDate": "2024-12-31T17:00:00"
  }'
```

### Get Task by ID
```bash
curl http://localhost:8080/api/tasks/1
```

### Get All Tasks for a User (with Pagination)
```bash
curl "http://localhost:8080/api/tasks/user/1?page=0&size=10&sortBy=createdAt&sortDirection=desc"
```

### Filter Tasks by Status
```bash
curl "http://localhost:8080/api/tasks/user/1/filter?status=PENDING&page=0&size=10"
```

### Filter Tasks by Priority
```bash
curl "http://localhost:8080/api/tasks/user/1/filter?priority=HIGH&page=0&size=10"
```

### Filter Tasks by Status and Priority
```bash
curl "http://localhost:8080/api/tasks/user/1/filter?status=IN_PROGRESS&priority=HIGH&page=0&size=10&sortBy=dueDate&sortDirection=asc"
```

### Update Task
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema",
    "description": "Create ER diagram and normalize tables - Updated",
    "status": "COMPLETED",
    "priority": "HIGH",
    "userId": 1,
    "dueDate": "2024-12-31T17:00:00"
  }'
```

### Delete Task
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

## 3. ANALYTICS

### Get Analytics for Specific User
```bash
curl http://localhost:8080/api/analytics/user/1
```

### Get Analytics for All Users
```bash
curl http://localhost:8080/api/analytics
```

## Expected Response Examples

### User Response
```json
{
  "id": 1,
  "email": "alice@example.com",
  "firstName": "Alice",
  "lastName": "Johnson",
  "status": "ACTIVE",
  "createdAt": "2024-06-17T10:00:00",
  "updatedAt": "2024-06-17T10:00:00"
}
```

### Task Response
```json
{
  "id": 1,
  "title": "Design database schema",
  "description": "Create ER diagram and normalize tables",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "userId": 1,
  "createdAt": "2024-06-17T10:05:00",
  "updatedAt": "2024-06-17T10:05:00",
  "dueDate": "2024-12-31T17:00:00",
  "completedAt": null
}
```

### Tasks Page Response
```json
{
  "content": [
    {
      "id": 1,
      "title": "Task 1",
      "description": "Description 1",
      "status": "PENDING",
      "priority": "HIGH",
      "userId": 1,
      "createdAt": "2024-06-17T10:05:00",
      "updatedAt": "2024-06-17T10:05:00",
      "dueDate": "2024-12-31T17:00:00",
      "completedAt": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### Analytics Response
```json
{
  "userId": 1,
  "userName": "Alice Johnson",
  "totalTasks": 10,
  "completedTasks": 7,
  "pendingTasks": 2,
  "inProgressTasks": 1,
  "highPriorityTasks": 5,
  "mediumPriorityTasks": 3,
  "lowPriorityTasks": 2,
  "completionPercentage": 70.0
}
```

## Pagination & Sorting Parameters

### Pagination
- `page`: Page number (0-indexed, default: 0)
- `size`: Number of items per page (default: 10)

### Sorting
- `sortBy`: Field to sort by (default: createdAt)
  - Supported fields: id, title, status, priority, createdAt, updatedAt, dueDate
- `sortDirection`: Sort direction (default: desc)
  - Values: asc, desc

### Example
```bash
curl "http://localhost:8080/api/tasks/user/1?page=1&size=5&sortBy=priority&sortDirection=asc"
```

## Error Responses

### 404 - Not Found
```json
{
  "message": "User not found with id: 999"
}
```

### 400 - Bad Request (Validation Error)
```json
{
  "email": "Email should be valid",
  "firstName": "First name is required"
}
```

### 400 - Bad Request (Business Logic)
```json
{
  "message": "User with email alice@example.com already exists"
}
```