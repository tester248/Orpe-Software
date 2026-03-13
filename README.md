# ORPE Consultants — Duty Drawback Management System

A Spring Boot web application for managing duty drawback claims end-to-end: from importing Bill of Entry and Shipping Bill data through BOM matching, worksheet calculations, and final DBK amount computation.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.x, Java 17 |
| Frontend | Thymeleaf, Bootstrap |
| Database | MySQL 8.0 |
| File Processing | Apache POI (Excel), PDFBox / Tabula (PDF) |
| Session | Spring Session JDBC |
| Build | Maven |

---

## Modules

| Module | Description |
|---|---|
| **Import Data** | Bill of Entry (BOE) records — upload, parse, and manage |
| **Export Data** | Shipping Bill records — upload, parse, and Excel download |
| **BOM Data** | Bill of Materials — link materials to export shipments |
| **BOM Claims** | Claim-level BOM associations |
| **Worksheet** | Duty drawback claim calculations per shipment |
| **Worksheet Draft** | Save and edit claim drafts before finalisation |
| **SB Qty Consumption** | Material consumption tracking per Shipping Bill |
| **DBK Calculation** | Final drawback amount calculation (claim-wise & SB-wise) |
| **User Management** | Role-based access control (Admin / Manager / User / Viewer) |
| **Database Backup** | On-demand MySQL backup download |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0

---

## Local Setup

### 1. Database

```sql
CREATE DATABASE db_orpe_consultants;
```

### 2. Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_orpe_consultants?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

# For the database backup feature (Windows path example):
mysql.dump.path=C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe
backup.path=C:/db-backups
```

### 3. Build & Run

```bash
./mvnw spring-boot:run
```

Application starts at: `http://localhost:8083`

---

## Docker Setup

See [DOCKER_SETUP.md](DOCKER_SETUP.md) for full instructions.

```bash
docker-compose up --build
```

---

## Default Login

On first run, register an account via `/register`. An Admin can then manage roles from the User Management page.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/orpe/consultants/
│   │   ├── controller/     # MVC controllers (one per module)
│   │   ├── service/        # Business logic interfaces + implementations
│   │   ├── repository/     # Spring Data JPA repositories
│   │   ├── model/          # JPA entities
│   │   ├── dto/            # Data transfer objects
│   │   ├── utils/          # Utilities (e.g. DatabaseBackupUtil)
│   │   └── config/         # App configuration beans
│   └── resources/
│       ├── application.properties
│       └── templates/      # Thymeleaf HTML templates
└── test/
```

---

## Key Endpoints

| URL | Description |
|---|---|
| `/` | Dashboard |
| `/login` | Login page |
| `/register` | Register new user |
| `/importdata/list` | BOE records |
| `/exportdata/list` | Shipping Bill records |
| `/worksheet/list` | Worksheets |
| `/dbkcalculation/dataselect` | SB-wise DBK calculation |
| `/users/list` | User management (Admin) |
| `/backup/download` | Download DB backup |
