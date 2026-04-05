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

## Wiki

This section explains the software in everyday language.

### What This Software Does

Think of this system as a digital checklist plus calculator for export claims.

If your company exports goods, you may be allowed to claim back some duties/taxes paid on imported materials. This software helps you:

- upload import and export records,
- link materials to products,
- calculate claim amounts,
- review drafts,
- finalize claim calculations,
- keep an audit trail.

### Full Process in Simple Steps

1. Upload Import Data
- Add Bill of Entry records (what was imported, when, how much duty was paid).

2. Upload Export Data
- Add Shipping Bill records (what was exported, when, value, quantity).

3. Upload BOM Data
- BOM means Bill of Materials.
- This is the recipe of materials required for each export item.

4. Create BOM Claims
- Link import-side materials to specific export claims.

5. Build Worksheet
- The worksheet calculates how much quantity was used and what duty amount can be claimed.

6. Save Drafts
- Keep work-in-progress before finalizing.

7. Run DBK Calculation
- DBK means Drawback.
- This gives the final claim numbers (claim-wise and shipping-bill-wise views).

### Glossary

- BOE: Bill of Entry
Meaning: Import customs document.

- SB: Shipping Bill
Meaning: Export customs document.

- BOM: Bill of Materials
Meaning: List of input materials used to make export products.

- DBK: Drawback
Meaning: Refund/credit of certain duties on exported goods.

- Claim Ref No
Meaning: Your internal/customs reference number for a claim set.

- Claim Year
Meaning: Year bucket of the claim.

- BE No / BE Date
Meaning: Bill of Entry number/date.

- SB No / SB Date
Meaning: Shipping Bill number/date.

- Qty Opening Balance
Meaning: Available material quantity before current usage.

- Qty Used
Meaning: Quantity consumed for the current worksheet calculation.

- Closing Balance
Meaning: Remaining quantity after usage.

- Stock Wise Eligibility (OPEN/CLOSED)
Meaning:
- OPEN: Record can still be used.
- CLOSED: Record is exhausted or should not be used further.

- Duty Claimed Amount
Meaning: Amount being claimed for refund/drawback.

- SBR
Meaning: Drawback rate reference used in calculation outputs.

- ARO / BRC
Meaning: Trade/compliance references captured with export records.

### Main Screens (What They Mean)

- Dashboard
High-level metrics and navigation.

- Import Data List
All imported-material records with filtering and cleanup options.

- Export Data List
All shipping bill/export records.

- BOM Data List
Material mapping master.

- BOM Claim List
Claim-level linkage between BOM and trade records.

- Worksheet List
Calculation workspace; where claim math is prepared.

- Worksheet Draft List
Saved but not finalized calculations.

- DBK Calculation
Final claim numbers and summaries.

- User Management
Manage users and roles (Admin only).

### Roles 

- Admin
Can manage users and full data access.

- Manager
Operational access across business flows (depending on page permissions).

- User
Regular data entry and processing role.

- Viewer
Read-only or minimal-change role.

### Common Questions

Q: Why do I need both Import and Export data?
A: Claim calculation depends on both what you imported and what you exported.

Q: Why is a row not usable in worksheet?
A: It may be marked CLOSED in stock eligibility or have no usable balance.

Q: Why save draft first?
A: Draft lets you review and correct calculations before final claim submission.

Q: Can I download data?
A: Yes. Some modules provide Excel download options for reporting and checks.

### Tips for Non-Technical Users

- Start with small files first to validate column quality.
- Keep date formats consistent.
- Check claim reference and year carefully before saving.
- Review closing balance before finalizing.
- Use filters to isolate one claim/user/year at a time.

### Suggested Team Workflow

- Data Entry Team: Upload import/export/BOM files.
- Claims Team: Build worksheets and drafts.
- Reviewer: Validate figures and stock balances.
- Admin: Final approval and user governance.

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
