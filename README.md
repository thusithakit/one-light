# OneLight Backend

Backend services for **OneLight** - a connected smart light and notification device that allows users to send messages to another user's physical light.

OneLight combines a modern **microservices backend**, real-time communication, and IoT hardware to create a physical notification experience. The device normally displays the current time and switches to a notification state when a message is received.

## ✨ Overview

OneLight is designed around a simple idea:

> **Send something digitally → receive it physically.**

Users can register, authenticate, connect with other users, and send notifications to their OneLight device.

The backend provides the APIs and infrastructure required to:

* 👤 Manage users
* 🔐 Authenticate users
* 🔄 Manage refresh tokens and sessions
* 💬 Send notifications/messages
* 📡 Communicate with IoT devices
* 🧩 Route requests through an API Gateway
* 🔗 Communicate between services using gRPC
* 🗄️ Persist application data in PostgreSQL
* 🐳 Run services using Docker
* ☁️ Prepare the system for cloud deployment

---

## 🏗️ Architecture

OneLight follows a **microservices architecture** where individual services are responsible for specific business capabilities.

```text
                         ┌───────────────────┐
                         │   OneLight Client │
                         │ Web / Mobile / IoT│
                         └─────────┬─────────┘
                                   │
                                   ▼
                         ┌───────────────────┐
                         │    API Gateway    │
                         │    Spring Cloud   │
                         └─────────┬─────────┘
                                   │
                  ┌────────────────┼────────────────┐
                  │                │                │
                  ▼                ▼                ▼
          ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
          │ Auth Service │ │ User Service │ │Notification  │
          │              │ │              │ │   Service    │
          └──────┬───────┘ └──────┬───────┘ └──────┬───────┘
                 │                │                │
                 │                │                │
                 ▼                ▼                ▼
          ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
          │ Auth DB      │ │ User DB      │ │Notification  │
          │ PostgreSQL   │ │ PostgreSQL   │ │ DB           │
          └──────────────┘ └──────────────┘ └──────────────┘
                                  │
                                  │ gRPC
                                  ▼
                         ┌───────────────────┐
                         │  Internal Service │
                         │   Communication   │
                         └───────────────────┘
```

The architecture is designed so that services remain independently deployable while using lightweight protocols for internal communication.

---

# 📦 Services

## API Gateway

The API Gateway acts as the single entry point for external clients.

Responsibilities include:

* Routing requests to backend services
* Centralizing cross-cutting concerns
* Authentication/authorization integration
* Service discovery/routing
* Request filtering
* CORS configuration

Default port:

```text
8080
```

---

## Auth Service

Responsible for authentication and session management.

Responsibilities:

* User authentication
* Password validation
* Access token generation
* Refresh token generation
* Refresh token rotation
* Token validation
* Logout/session invalidation

The Auth Service communicates with the User Service through **gRPC** when user information needs to be created or retrieved.

Default port:

```text
4001
```

---

## User Service

Responsible for user management and user-related data.

Responsibilities:

* User registration data
* User profile management
* User lookup
* User creation
* Persistent user storage

The service uses PostgreSQL for persistence.

Default port:

```text
4000
```

---

## Notification Service

Responsible for OneLight notifications and messages.

Potential responsibilities include:

* Creating notifications
* Delivering messages to connected devices
* Notification history
* Device notification state
* Notification acknowledgement

Default port:

```text
4002
```

---

# 🔗 Service Communication

OneLight uses different communication mechanisms depending on the requirement.

### External communication

REST APIs are exposed through the API Gateway.

```text
Client → API Gateway → Service
```

### Internal communication

Services use **gRPC** for synchronous service-to-service communication where low latency and strongly typed contracts are useful.

Example:

```text
Auth Service
     │
     │ gRPC
     ▼
User Service
     │
     ▼
PostgreSQL
```

Protocol definitions are maintained separately to keep service contracts explicit and versionable.

---

# 🗄️ Database

OneLight uses **PostgreSQL**.

Each service is designed around its own database/schema to maintain service boundaries.

Example:

```text
PostgreSQL
│
├── user_db
│
├── auth_db
│
└── notification_db
```

This approach allows individual services to evolve independently without tightly coupling their persistence layers.

---

# 🐳 Docker

All backend services are containerized using Docker.

Typical development environment:

```text
Docker
│
├── api-gateway
├── auth-service
├── user-service
├── notification-service
│
├── user-service-db
├── auth-service-db
└── notification-service-db
```

Build an individual service:

```bash
docker build -t onelight-user-service .
```

Run a container:

```bash
docker run -p 4000:4000 onelight-user-service
```

For running the complete backend stack, Docker Compose can be used where applicable.

---

# ⚙️ Technology Stack

| Technology           | Purpose                        |
| -------------------- | ------------------------------ |
| Java                 | Backend development            |
| Spring Boot          | Microservices framework        |
| Spring Cloud Gateway | API Gateway                    |
| Spring Data JPA      | Database access                |
| Hibernate            | ORM                            |
| PostgreSQL           | Relational database            |
| gRPC                 | Internal service communication |
| Protocol Buffers     | Service contracts              |
| Maven                | Build & dependency management  |
| Docker               | Containerization               |
| JWT                  | Authentication                 |
| Git                  | Version control                |

---

# 📁 Project Structure

The backend repository is organized as a multi-service project.

```text
one-light/
│
├── api-gateway/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── auth-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── user-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── notification-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── grpc-contracts/
│   ├── src/
│   └── pom.xml
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

> The exact service list may evolve as the OneLight platform grows.

---

# 🔐 Authentication Flow

A typical authentication flow looks like this:

```text
                    ┌──────────┐
                    │  Client  │
                    └────┬─────┘
                         │
                         │ Register
                         ▼
                  ┌─────────────┐
                  │ API Gateway │
                  └──────┬──────┘
                         │
                         ▼
                  ┌─────────────┐
                  │ Auth Service│
                  └──────┬──────┘
                         │
                         │ gRPC
                         ▼
                  ┌─────────────┐
                  │ User Service│
                  └──────┬──────┘
                         │
                         ▼
                    ┌─────────┐
                    │Postgres │
                    └─────────┘
```

After successful authentication:

```text
Client
  │
  │ Login
  ▼
Auth Service
  │
  ├── Access Token
  │
  └── Refresh Token
```

The access token is then used to access protected API endpoints.

---

# 🔄 Refresh Token Flow

OneLight uses short-lived access tokens together with refresh tokens.

```text
Client
   │
   │ Access Token expired
   ▼
Auth Service
   │
   │ Refresh Token
   ▼
Validate Refresh Token
   │
   ├── Invalid → Reject
   │
   └── Valid
        │
        ▼
 Generate new Access Token
        │
        ▼
      Client
```

Refresh tokens should be stored securely and invalidated when a session is terminated.

---

# 📡 IoT Communication

The OneLight backend is designed to communicate with physical OneLight devices.

A simplified notification flow is:

```text
User A
  │
  │ Send message
  ▼
API Gateway
  │
  ▼
Notification Service
  │
  ▼
OneLight Device
  │
  ▼
Display notification
```

The device normally displays the current time.

When a notification arrives:

```text
12:45
```

can temporarily change to something such as:

```text
❤️
You have a
new message
```

After the notification is handled, the device can return to its normal clock state.

---

# 📨 Kafka

Kafka can be introduced for asynchronous, event-driven operations where loose coupling is beneficial.

Potential events include:

```text
USER_CREATED
NOTIFICATION_CREATED
DEVICE_CONNECTED
MESSAGE_SENT
MESSAGE_DELIVERED
MESSAGE_READ
```

Example:

```text
User Service
     │
     │ USER_CREATED
     ▼
   Kafka
     │
     ├──────────────► Notification Service
     │
     └──────────────► Other Consumers
```

Synchronous operations that require an immediate response can continue to use REST/gRPC while Kafka handles asynchronous events.

---

# 🌐 API

External clients communicate with OneLight through the API Gateway.

Example:

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
```

User endpoints:

```http
GET    /api/users/me
GET    /api/users/{id}
PUT    /api/users/me
```

Notification endpoints:

```http
POST   /api/notifications
GET    /api/notifications
GET    /api/notifications/{id}
```

> API routes may change as the backend evolves.

---

# ⚙️ Environment Variables

Sensitive configuration should be provided through environment variables rather than committed to Git.

Example:

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=user_db
DB_USERNAME=user_service
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret

# gRPC
USER_SERVICE_GRPC_HOST=localhost
USER_SERVICE_GRPC_PORT=9090

# Service
SERVER_PORT=4000
```

For Docker:

```env
DB_HOST=user-service-db
DB_PORT=5432
```

Never commit real passwords, private keys, JWT secrets, API keys, or production credentials to the repository.

---

# 🚀 Getting Started

## Prerequisites

Make sure the following are installed:

* Java 25+
* Maven
* Docker
* Docker Compose
* PostgreSQL (if running databases locally)

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

Verify Docker:

```bash
docker --version
```

---

## Clone the Repository

```bash
git clone https://github.com/thusithakit/one-light.git
cd one-light
```

---

## Build the Project

```bash
mvn clean install
```

To skip tests:

```bash
mvn clean install -DskipTests
```

---

## Run a Service Locally

Navigate to the required service:

```bash
cd user-service
```

Run:

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/user-service.jar
```

---

# 🐳 Running with Docker

Build the services:

```bash
docker compose build
```

Start the backend:

```bash
docker compose up
```

Run in detached mode:

```bash
docker compose up -d
```

View logs:

```bash
docker compose logs -f
```

Stop the services:

```bash
docker compose down
```

---

# 🧪 Testing

Run all tests:

```bash
mvn test
```

Run tests for a specific service:

```bash
cd user-service
mvn test
```

The backend is intended to include:

* Unit tests
* Integration tests
* Repository tests
* Controller/API tests
* gRPC communication tests

---

# 🔍 Health Checks

Spring Boot Actuator can be used to expose service health information.

Example:

```http
GET /actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

Health endpoints are useful for Docker, Kubernetes, load balancers, and cloud deployments.

---

# ☁️ Deployment

The backend is designed with cloud deployment in mind.

Potential deployment architecture:

```text
                         Internet
                            │
                            ▼
                     Load Balancer
                            │
                            ▼
                      API Gateway
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
          Auth Service  User Service  Notification
              │             │             │
              └─────────────┼─────────────┘
                            │
                     PostgreSQL
```

The services can be deployed using container-based infrastructure such as:

* AWS ECS
* AWS EKS
* EC2
* Kubernetes
* Other container platforms

Infrastructure choices may evolve as the project develops.

---

# 🔒 Security

Security is an important part of the OneLight backend.

The backend should follow the following principles:

* Passwords must never be stored in plain text.
* Passwords should be securely hashed.
* JWT secrets must not be committed to Git.
* Refresh tokens should be securely managed.
* Protected endpoints require authentication.
* Input validation should be applied to API requests.
* Sensitive configuration should use environment variables or a secret manager.
* Database credentials should never be hard-coded.
* Production services should use HTTPS/TLS.

---

# 📊 Observability

The backend can be extended with:

* Spring Boot Actuator
* Structured logging
* Prometheus
* Grafana
* OpenTelemetry
* Distributed tracing

This becomes particularly useful as the number of microservices increases.

---

# 🛣️ Roadmap

### Backend

* [x] Spring Boot microservices foundation
* [x] PostgreSQL integration
* [x] API Gateway
* [x] gRPC service communication
* [x] User Service
* [x] Authentication Service
* [ ] Refresh-token persistence
* [ ] Notification Service
* [ ] Device management
* [ ] IoT device authentication
* [ ] Real-time notification delivery
* [ ] Kafka event processing
* [ ] Automated integration testing
* [ ] Production observability

### Cloud

* [ ] Container registry
* [ ] Cloud deployment
* [ ] Managed PostgreSQL
* [ ] CI/CD pipeline
* [ ] Monitoring and alerting
* [ ] Infrastructure as Code

### IoT

* [ ] Device registration
* [ ] Device authentication
* [ ] Device heartbeat
* [ ] Online/offline status
* [ ] Remote configuration
* [ ] Notification delivery
* [ ] Device firmware updates

---

# 🤝 Development

Contributions, suggestions, and improvements are welcome.

For development:

```bash
git checkout -b feature/your-feature
```

Make your changes, test them, then commit:

```bash
git add .
git commit -m "feat: add notification service"
```

Push the branch:

```bash
git push origin feature/your-feature
```

Then open a pull request.

---

# 📄 License

This project is currently developed as a personal project.

License information will be added as the project approaches public/commercial release.

---

# 👨‍💻 Author

**Thusitha Kithuldora**

Frontend-Focused Full-Stack Software Engineer

* Portfolio: [thusithakit.com](https://thusithakit.com)
* GitHub: [github.com/thusithakit](https://github.com/thusithakit)
* LinkedIn: [linkedin.com/in/thusitha-kithuldora](https://www.linkedin.com/in/thusitha-kithuldora)

---

## 💡 OneLight

**A small light for the moments that matter.**

Built with ❤️ using Java, Spring Boot, PostgreSQL, gRPC, Docker and IoT.
