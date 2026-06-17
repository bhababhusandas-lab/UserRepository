# DEPLOYMENT GUIDE

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional)
- MySQL 8.0 or PostgreSQL 14+
- Cloud provider account (AWS, Azure, GCP, Heroku, etc.)

## Local Development

### 1. Database Setup

#### MySQL
```sql
CREATE DATABASE task_management_db;
USE task_management_db;
```

#### PostgreSQL
```sql
CREATE DATABASE task_management_db;
```

### 2. Configuration

Update `application.properties` with your database credentials:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/task_management_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/task_management_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
```

### 3. Build and Run

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

## Docker Deployment

### 1. Create Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/task-management-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Create docker-compose.yml

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: task_management_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/task_management_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql

volumes:
  mysql_data:
```

### 3. Build and Run with Docker

```bash
# Build the JAR
mvn clean package

# Build Docker image
docker build -t task-management-api:1.0.0 .

# Run with docker-compose
docker-compose up
```

## Cloud Deployment

### AWS EC2

1. **Launch EC2 Instance**
   - Choose Ubuntu 22.04 LTS
   - Configure security groups to allow HTTP/HTTPS (80, 443, 8080)

2. **Install Java**
   ```bash
   sudo apt update
   sudo apt install openjdk-17-jdk
   ```

3. **Install MySQL**
   ```bash
   sudo apt install mysql-server
   sudo mysql_secure_installation
   ```

4. **Deploy Application**
   ```bash
   # Copy JAR to instance
   scp -i your-key.pem target/task-management-api-1.0.0.jar ubuntu@your-instance:/home/ubuntu/

   # SSH into instance and run
   ssh -i your-key.pem ubuntu@your-instance
   java -jar task-management-api-1.0.0.jar
   ```

### Heroku

1. **Install Heroku CLI**
   ```bash
   curl https://cli-assets.heroku.com/install.sh | sh
   ```

2. **Login to Heroku**
   ```bash
   heroku login
   ```

3. **Create Application**
   ```bash
   heroku create your-app-name
   ```

4. **Add PostgreSQL Database**
   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

5. **Deploy**
   ```bash
   git push heroku main
   ```

6. **View Logs**
   ```bash
   heroku logs --tail
   ```

### AWS Elastic Beanstalk

1. **Install EB CLI**
   ```bash
   pip install awsebcli --upgrade --user
   ```

2. **Initialize Application**
   ```bash
   eb init -p java-17 task-management-api
   ```

3. **Create Environment**
   ```bash
   eb create task-management-env
   ```

4. **Deploy**
   ```bash
   eb deploy
   ```

## Production Configuration

Create `application-prod.properties`:

```properties
spring.profiles.active=prod

# Database
spring.datasource.url=jdbc:mysql://prod-db-host:3306/task_management_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Server
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=WARN
logging.level.com.taskmanagement=INFO

# Security
spring.datasource.hikari.maximum-pool-size=20
```

## Health Check Endpoint

Add a health check endpoint by creating a new controller:

```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Application is running");
    }
}
```

## Monitoring and Logging

### Add Spring Boot Actuator

Add dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Add to `application.properties`:
```properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=always
```

## Environment Variables

Set these environment variables in your deployment environment:

```bash
DB_URL=jdbc:mysql://host:3306/task_management_db
DB_USERNAME=root
DB_PASSWORD=your_secure_password
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

## Troubleshooting

### Database Connection Issues
- Verify database URL and credentials
- Check firewall/security group rules
- Ensure database service is running

### Port Already in Use
```bash
# Kill process using port 8080
lsof -ti:8080 | xargs kill -9
```

### Application Won't Start
- Check logs: `tail -f logs/application.log`
- Verify Java version: `java -version`
- Ensure all dependencies are downloaded: `mvn dependency:resolve`

## Backup and Recovery

### Database Backup (MySQL)
```bash
mysqldump -u root -p task_management_db > backup.sql
```

### Database Restore
```bash
mysql -u root -p task_management_db < backup.sql
```

## SSL/HTTPS Configuration

Add to `application.properties`:
```properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
```

## Load Balancing

For high-availability deployments, use:
- AWS Elastic Load Balancer
- NGINX
- HAProxy

## CI/CD Pipeline

### GitHub Actions Example

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn clean package
      - name: Deploy to Heroku
        run: |
          git remote add heroku https://git.heroku.com/${{ secrets.HEROKU_APP_NAME }}.git
          git push heroku main
```

## Support

For issues or questions, refer to:
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- MySQL Documentation: https://dev.mysql.com/doc/
- PostgreSQL Documentation: https://www.postgresql.org/docs/