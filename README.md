# Human Resource Management System (HRMS) API

A Spring Boot REST API for managing employees, departments, attendance, and leave requests. Four roles (`SUPER_ADMIN`, `HR`, `MANAGER`, `EMPLOYEE`) control who can read or change each resource, enforced with stateless JWT authentication and Spring Security.

I built this during an 8-week backend internship at Bluechip Technologies (Application Services) and deployed it to AWS EC2. A React dashboard consumes the API (see [Frontend](#frontend)).

## Features

- Register users and sign in with JSON Web Token (JWT) authentication
- Role-based access control (RBAC) with per-resource permissions (`hr:read`, `hr:update`, and so on)
- Employee and department management (create, read, update, delete)
- Clock in and clock out, with a nightly job that closes forgotten timesheets
- Leave requests: employees submit and track their own; HR reviews and approves or rejects
- Interactive API documentation with Swagger UI

## Tech stack

| Layer | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.1, Spring Web MVC |
| Security | Spring Security, JJWT 0.12, BCrypt (strength 12) |
| Persistence | Spring Data JPA (Hibernate), PostgreSQL |
| API docs | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Hosting | AWS EC2 (Security Groups, SSH key authentication) |

## Architecture

The project follows a layered design. Each layer talks only to the one below it.

```text
controller   HTTP endpoints, request validation, @PreAuthorize rules
service      business logic (leave workflow, attendance, current-user lookup)
repository   Spring Data JPA interfaces
model        JPA entities, enums, and Data Transfer Objects (DTOs)
config       SecurityFilterChain, JWT filter, OpenAPI setup
```

Design decisions:

- **DTOs at the boundary.** Controllers accept and return DTOs (`EmployeeInputDto`, `LeaveRequestOutputDto`, and others), so database entities never leak into client contracts.
- **Stateless sessions.** A `JwtFilter` runs before `UsernamePasswordAuthenticationFilter`, validates the token, and populates the security context. The server stores no session state.
- **Identity from the security context.** Leave and attendance services read the signed-in user from `SecurityContextHolder` instead of re-parsing the token. This keeps an employee from creating or viewing another employee's leave requests.
- **Permissions as authorities.** Each role maps to a set of permission strings, and Spring Security receives them as granted authorities alongside a `ROLE_` entry.

## Data model

```text
Users 1 --- 1 Employee N --- 1 Department
                 |
                 +-- 1 --- N LeaveRequest   (type, status, start/end date, reason)
                 +-- 1 --- N AttendanceRecord (date, clock-in/out, status)
```

Enums: `Role`, `LeaveType` (`ANNUAL`, `SICK`, `UNPAID`, `MATERNITY`), `LeaveStatus` (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`), `AttendanceStatus` (`PRESENT`, `ABSENT`, `LEAVE`, `SYSTEM_CLOSED`), `WorkStatus` (`ACTIVE`, `ON_LEAVE`, `TERMINATED`).

## API overview

All routes sit under `/api`. Everything except login and the Swagger paths requires a `Bearer` token.

| Area | Method and path | Who can call it |
| --- | --- | --- |
| Users | `POST /users/login` | Public |
| Users | `POST /users/register` | `SUPER_ADMIN`, `HR` |
| Users | `DELETE /users/delete/{id}` | `SUPER_ADMIN`, `HR` |
| Employees | `GET /employees/all`, `/id/{id}`, `/department`, `/department/id` | `SUPER_ADMIN`, `HR` |
| Employees | `POST /employees/add`, `PATCH /employees/update/{id}`, `DELETE /employees/delete/{id}` | `SUPER_ADMIN`, `HR` |
| Departments | `GET /departments/all`, `/{id}` | `SUPER_ADMIN`, `HR` |
| Departments | `POST /departments/add/{name}`, `PATCH /departments/update`, `DELETE /departments/delete/{id}` | `SUPER_ADMIN`, `HR` |
| Attendance | `POST /attendance/clock-in`, `PATCH /attendance/clock-out`, `GET /attendance` | Any signed-in role with the matching permission |
| Leave | `POST /leave-requests`, `GET /leave-requests/my-leaves` | Any signed-in role with the matching permission |
| Leave | `GET /leave-requests` | `HR` permission (`hr:read`) |
| Leave | `PATCH /leave-requests/{id}/status` | `HR` permission (`hr:update`) |

Open Swagger UI for request and response schemas.

## Scheduled job

`AttendanceScheduler` runs every day at 19:00. It finds attendance records for the current date with no clock-out time, sets the clock-out to 17:00, and marks them `SYSTEM_CLOSED`.

## Run it locally

You need JDK 25, Maven (or the included wrapper), and PostgreSQL.

1. Create a PostgreSQL database named `HRMS`.
2. Set your database credentials as environment variables (Spring Boot reads these over the values in `application.yml`):

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/HRMS"
   $env:SPRING_DATASOURCE_USERNAME = "your_database_user_here"
   $env:SPRING_DATASOURCE_PASSWORD = "your_database_password_here"
   ```

3. Start the API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Open Swagger UI at `http://localhost:8080/swagger-ui.html`.

Hibernate creates and updates the tables on startup (`ddl-auto: update`).

## Frontend

The dashboard lives in a separate React 19, TypeScript, and Vite project. It uses React Router for navigation, React Context for authentication state, and Axios for API calls. Pages: login, dashboard home, my leaves, employee directory, departments, and leave approvals. The API allows CORS from `localhost:3000`, `5173`, and `5174`.

## Testing

I tested endpoints manually through Postman and Swagger UI while building the API. The repository contains no automated tests beyond the default Spring context test.

## Author

Fawaz Salimanu: [GitHub](https://github.com/Faweezee) | [LinkedIn](https://www.linkedin.com/in/fawaz-salimanu-12a147386)
