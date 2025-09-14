## Cultural Festival Management System

Multi-module Spring Boot project:
- `festival-app`: Main MVC + REST app (JWT, JPA, Mongo logs, Thymeleaf)
- `schedule-service`: Scheduling microservice

### Prerequisites
- Java JDK 17
- Maven 3.9+
- MySQL (default: user `root`, pass `root`)
- MongoDB (default: `mongodb://localhost:27017`)

Windows install tips:
- JDK 17: `winget install EclipseAdoptium.Temurin.17.JDK`
- Maven: `winget install Apache.Maven` (or `choco install maven -y`)
- MySQL: `winget install Oracle.MySQL` (or use XAMPP/WAMP)
- MongoDB: `winget install MongoDB.Server`

Ensure `java -version` shows 17 and `mvn -version` works.

### Database Configuration
`festival-app/src/main/resources/application.yml` contains defaults:
- MySQL URL: `jdbc:mysql://localhost:3306/festivaldb` (auto-create)
- MongoDB: `mongodb://localhost:27017/festivallogs`
- JWT secret: update `jwt.secret` to a long random string

`schedule-service/src/main/resources/application.yml`:
- MySQL URL: `jdbc:mysql://localhost:3306/scheduledb`

### Build
From the project root:
```
mvn -DskipTests clean install
```

### Run
Run microservice first:
```
mvn -pl schedule-service -am spring-boot:run
```
Then in a new terminal run main app:
```
mvn -pl festival-app -am spring-boot:run
```

### App URLs
- Festival Swagger: http://localhost:8080/swagger-ui.html
- Schedule Swagger: http://localhost:8081/swagger-ui.html
- Events page (Thymeleaf): http://localhost:8080/events
- Login page: http://localhost:8080/auth/login

### Auth
- Register: `POST /auth/register` with JSON `{ "name":"Alice", "email":"a@x.com", "password":"pw", "role":"STUDENT" }`
- Login: `POST /auth/login` with JSON `{ "email":"...", "password":"..." }` → returns `{ token, role }`
- Use `Authorization: Bearer <token>` for protected endpoints.

### Sample Data
`festival-app/src/main/resources/data.sql` seeds:
- Organizer user `org1@example.com` (password is a bcrypt hash, set your own)
- One sample event `Battle of Bands`

### Notes
- Update passwords and JWT secret before production use.
- Adjust DB usernames/passwords in `application.yml` as needed.


