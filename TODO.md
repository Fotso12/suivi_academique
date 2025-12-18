# Logging Configuration Tasks

- [x] Modify JwtAuthenticationFilter to set MDC with user ID (codePersonnel) and IP address
- [x] Update logback-spring.xml to include user ID and IP in log patterns
- [x] Add MDC clearing mechanism after request processing
- [ ] Test logging configuration

# CI/CD Pipeline Setup

- [x] Create GitHub Actions workflow for CI/CD
- [x] Configure CI: code check, compilation, tests, JAR production
- [x] Configure CD: Docker image build, publish to Docker Hub, deployment placeholder
- [x] Update docker-compose.yml to use published image
- [x] Create .env.example for environment variables
- [ ] Test CI/CD pipeline locally and on GitHub
