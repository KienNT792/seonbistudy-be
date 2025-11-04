# Docker Setup for Seonbistudy Backend

This guide explains how to run the Seonbistudy backend application using Docker.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose installed (usually comes with Docker Desktop)

## Quick Start

### 1. Build and Start Services

Run the following command in the project root directory:

```bash
docker-compose up --build
```

This command will:
- Build the Spring Boot application Docker image
- Start MySQL database container
- Start the Spring Boot application container
- The application will be available at `http://localhost:8080`
- MySQL will be available at `localhost:3307`

### 2. Run in Detached Mode

To run containers in the background:

```bash
docker-compose up -d --build
```

### 3. View Logs

To view application logs:

```bash
# All services
docker-compose logs -f

# Only application logs
docker-compose logs -f app

# Only MySQL logs
docker-compose logs -f mysql
```

### 4. Stop Services

```bash
docker-compose down
```

To stop and remove volumes (this will delete all database data):

```bash
docker-compose down -v
```

## Configuration

### Environment Variables

All environment variables are configured in `docker-compose.yml`. For production use, you should:

1. Copy `.env.example` to `.env`:
   ```bash
   copy .env.example .env
   ```

2. Update the values in `.env` file with your actual configuration

3. Modify `docker-compose.yml` to use the `.env` file:
   ```yaml
   env_file:
     - .env
   ```

### Important: Change these values for production:
- `JWT_SECRET`: Use a strong, random secret key (minimum 256 bits)
- `MYSQL_ROOT_PASSWORD`: Use a strong password
- `MYSQL_PASSWORD`: Use a strong password
- `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET`: Add your actual Google OAuth credentials

## Services

### Spring Boot Application
- **Container Name**: `seonbistudy-app`
- **Port**: 8080
- **Access**: http://localhost:8080

### MySQL Database
- **Container Name**: `seonbistudy-mysql`
- **Port**: 3307 (mapped from container's 3306)
- **Database**: seonbistudy_db
- **Username**: seonbi_user
- **Password**: seonbi_password
- **Root Password**: root_password

## Useful Commands

### Rebuild specific service
```bash
docker-compose up -d --build app
```

### Access MySQL database
```bash
docker exec -it seonbistudy-mysql mysql -u seonbi_user -p
# Enter password: seonbi_password
```

### Access application container shell
```bash
docker exec -it seonbistudy-app sh
```

### Remove all containers and volumes
```bash
docker-compose down -v
docker system prune -a
```

## Troubleshooting

### Application fails to connect to MySQL
- Wait a few seconds for MySQL to fully initialize
- Check MySQL health status: `docker-compose ps`
- View MySQL logs: `docker-compose logs mysql`

### Port already in use
If port 8080 or 3307 is already in use, modify the port mapping in `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Change host port to 8081
```

### Build fails
- Ensure Maven wrapper has execute permissions
- Check if you have sufficient disk space
- Try cleaning Docker cache: `docker system prune -a`

## Development Workflow

1. Make code changes
2. Rebuild and restart application:
   ```bash
   docker-compose up -d --build app
   ```
3. View logs to verify changes:
   ```bash
   docker-compose logs -f app
   ```

## Production Considerations

1. Use environment variables from secure sources (e.g., AWS Secrets Manager, Azure Key Vault)
2. Use Docker secrets for sensitive data
3. Configure proper logging and monitoring
4. Use production-grade database with backups
5. Configure health checks and restart policies
6. Use reverse proxy (e.g., Nginx) for SSL/TLS termination
7. Implement proper network security and firewall rules
# Use Eclipse Temurin JDK 21 as the base image for building
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Use Eclipse Temurin JRE 21 for the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set default environment variables (can be overridden by docker-compose)
ENV SPRING_PROFILES_ACTIVE=default

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

