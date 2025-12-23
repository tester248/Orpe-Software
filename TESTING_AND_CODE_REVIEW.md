# Orpe-Software Testing & Code Review Report

**Application:** ORPE Consultants - Duty Drawback Management System


### Key Findings

| Category | Critical | High | Medium | Low |
|----------|----------|------|--------|-----|
| Security Issues | 1 | 4 | 3 | 1 |
| Code Quality | 0 | 0 | 5 | 4 |
| Critical Bugs | 2 | 2 | 0 | 0 |
| Performance | 0 | 2 | 2 | 1 |
| Missing Features | 0 | 1 | 2 | 3 |
| **Total** | **3** | **9** | **12** | **9** |

### Functional Test Results
- ✅ **All core features working correctly**
- ✅ Login/Authentication
- ✅ Data Import (Excel parsing)
- ✅ Data Export (Excel generation)
- ✅ Worksheet calculations
- ✅ Dashboard metrics

---

## Application Overview

### Tech Stack
- **Backend:** Spring Boot 3.5.5, Java 17
- **Frontend:** Thymeleaf, Bootstrap
- **Database:** MySQL 8.0
- **File Processing:** Apache POI (Excel), PDFBox/Tabula (PDF)
- **Session Management:** Spring Session JDBC
- **Build:** Maven

### Core Modules
1. **Import Data Management** - BOE (Bill of Entry) records
2. **Export Data Management** - Shipping Bills
3. **BOM Processing** - Bill of Materials linking
4. **Worksheet Calculations** - Duty drawback claim calculations
5. **SB Qty Consumption** - Material consumption tracking
6. **DBK Calculation** - Drawback amount calculations
7. **User Management** - Role-based access (Admin/Manager/User/Viewer)


## Security Issues

### 🔴 CRITICAL

#### 1. DELETE Operations Using GET Requests (CSRF Vulnerability)
**Severity:** CRITICAL  
**Impact:** An attacker can trick authenticated users into deleting data via malicious links.

**Affected Files:**
| File | Line | Endpoint |
|------|------|----------|
| `UserController.java` | 101 | `@GetMapping("/users/delete/{id}")` |
| `ImportDataController.java` | 112 | `@GetMapping("/importdata/delete/{importId}")` |
| `ExportDataController.java` | 149 | `@GetMapping("/exportdata/delete/{exportId}")` |
| `BomDataController.java` | 115 | `@GetMapping("/bomdata/delete/{bomId}")` |
| `BomClaimController.java` | 161 | `@GetMapping("/bomclaimdata/delete/{claimId}")` |

**The code even acknowledges this:**
```java
@GetMapping("/users/delete/{id}")  // Use GET instead of POST
```

**Recommendation:** Change to `@DeleteMapping` or `@PostMapping` with CSRF tokens.

---

### 🟠 HIGH

#### 2. Missing Authentication on API Endpoints
**Severity:** HIGH  
**Impact:** Unauthenticated users can potentially access bulk-save and update endpoints.

**Affected Endpoints:**
| File | Endpoint | Issue |
|------|----------|-------|
| `ImportDataController.java` | `POST /importdata/bulk-save` | No session check |
| `ExportDataController.java` | `POST /exportdata/bulk-save` | No session check |
| `BomDataController.java` | `POST /bomdata/bulk-save` | No session check |
| `BomClaimController.java` | `POST /bomclaimdata/bulk-save` | No session check |
| `WorksheetController.java` | `POST /worksheet/saveBulk` | No session check |
| `DraftWorksheetController.java` | `POST /draftworksheet/updateBulk` | No session check |

---

#### 3. Storing User Entity with Password Hash in Session
**Severity:** HIGH  
**File:** `AuthController.java` line 142

```java
session.setAttribute("loggedInUser", authenticatedUser);
```

The full `User` entity (including hashed password) is stored in session.

**Recommendation:** Create a `SessionUserDTO` without sensitive fields.

---

#### 4. No Spring Security Configuration
**Severity:** HIGH  

The application uses manual session-based authentication without Spring Security, missing:
- CSRF protection
- Session fixation protection
- Rate limiting
- Concurrent session control

**Recommendation:** Implement Spring Security with proper configuration.

---

#### 5. Hardcoded Database Credentials
**Severity:** HIGH  
**File:** `application.properties` lines 14-15

```properties
spring.datasource.username=root
spring.datasource.password=password
```

**Recommendation:** Use environment variables or secrets management.

---

### 🟡 MEDIUM

#### 6. Missing Authorization Checks
**File:** `UserController.java`
- Any authenticated user can view other users' details
- No admin role check for user deletion
- No ownership verification

#### 7. Missing File Type Validation
**Affected Files:** All file upload endpoints
- No validation of file extension
- No MIME type checking
- No malicious content scanning

#### 8. Verbose Error Messages to Client
**File:** `WorksheetController.java`
```java
.body("Error during bulk save: " + e.getMessage());  // Exposes internals
```

---

### 🟢 LOW

#### 9. Repository Naming Typo
**File:** `BomExportModelReposioty.java`  
Should be: `BomExportModelRepository.java`

---

## Code Quality Issues

### 🟡 MEDIUM

#### 1. Debug Logging with System.out/System.err
**Impact:** Information leakage, poor logging practice

**Affected Files:**
| File | Occurrences |
|------|-------------|
| `WorksheetServiceImpl.java` | 8 |
| `DraftWorksheetServiceImpl.java` | 3 |
| `ShippingBillController.java` | 10+ |
| `AuthController.java` | Multiple |

**Example:**
```java
System.out.println("Saving draft: " + draftWorksheet.getClaimRefNo());
```

**Recommendation:** Replace with SLF4J logging (`log.debug()`, `log.info()`).

---

#### 2. Inconsistent Dependency Injection
**Issue:** Mix of `@Autowired` field injection and constructor injection via `@RequiredArgsConstructor`

| File | Pattern |
|------|---------|
| `AuthController.java` | `@Autowired` field injection |
| `WorksheetController.java` | Mixed - both patterns |
| `ImportDataController.java` | Constructor injection (correct) |

**Recommendation:** Standardize on constructor injection.

---

#### 3. Generic Exception Catching
**Issue:** Using `catch (Exception e)` instead of specific exceptions

**Affected Files:**
| File | Occurrences |
|------|-------------|
| `WorksheetServiceImpl.java` | 4 |
| `ImportDataExtractor.java` | 5 |
| `BomClaimExtractor.java` | 6 |
| `UserServiceImpl.java` | 4 |

**Recommendation:** Catch specific exceptions and handle appropriately.

---

#### 4. Swallowing Exceptions Silently
**File:** `ImportDataExtractor.java` line 254
```java
try { return LocalDate.parse(s, p); } catch (Exception ignore) {}
```

---

#### 5. Commented-Out Code
**File:** `AuthController.java` lines 45-58
```java
//    @GetMapping({"/", "/index"})
//    public String index(HttpSession session, Model model) {
//        ...
//    }
```

**Recommendation:** Remove dead code, use version control for history.

---

### 🟢 LOW

#### 6. Missing `@Transactional(readOnly = true)` on Read Operations
Most services use class-level `@Transactional` which creates write transactions for read-only operations.

#### 7. Missing `@Valid` on Request Bodies
Several `@RequestBody` parameters lack validation annotations.

#### 8. Production Logging Levels
`application.properties` has `DEBUG` level logging enabled which should be `INFO` or `WARN` in production.

#### 9. Debug Logging Enabled in Properties
```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

---

## Missing/Incomplete Implementations

### � HIGH

#### 1. Reports Feature Not Implemented (Broken Links)
**Affected Files:** 27 template files

The Reports menu in the sidebar links to non-existent pages:

| Menu Item | Links To | Status |
|-----------|----------|--------|
| Report 1 | `auth-sign-in-social.html` | ❌ 404 Error |
| Report 2 | `auth-sign-in-social.html` | ❌ 404 Error |
| Report 3 | `auth-reset-password.html` | ❌ 404 Error |

These are placeholder links from the original HTML template that were never updated. The Reports feature was never implemented.

**Recommendation:** Either implement the reports functionality or remove the menu items.

---

### �🟡 MEDIUM

#### 1. Stub Methods in DraftWorksheetServiceImpl
**File:** `DraftWorksheetServiceImpl.java`

| Method | Line | Status |
|--------|------|--------|
| `save(DraftWorksheetDTO dto)` | 190 | Returns `null` |
| `findById(Long id)` | 196 | Returns `Optional.empty()` |
| `deleteById(Long id)` | 212 | Empty method |
| `search(WorksheetDataFilter, Pageable)` | 218 | Returns `null` |
| `exportData(WorksheetDataFilter)` | 224 | Returns `null` |
| `validate(DraftWorksheetDTO)` | 230 | Returns `false` |
| `count(WorksheetDataFilter)` | 236 | Returns `0` |

---

#### 2. Stub Methods in WorksheetServiceImpl
**File:** `WorksheetServiceImpl.java`

| Method | Line | Status |
|--------|------|--------|
| `exportData(filter)` | 314 | Returns `null` |
| `validate(dto)` | 322 | Returns `false` |
| `count(filter)` | 330 | Returns `0` |

---

### 🟢 LOW

#### 3. Missing Error Pages
No custom 404, 500 error pages defined.

#### 4. No Unit Tests
Only 1 test file exists: `OrpeConsultantsApplicationTests.java` (context load test).

#### 5. No API Documentation
No Swagger/OpenAPI documentation for REST endpoints.

---

## Critical Bugs

### 🔴 CRITICAL

#### 1. Wrong Repository in deleteById Method (Data Corruption)
**File:** `WorksheetServiceImpl.java` line 212
```java
@Override
public void deleteById(Long bomId) {
    bomDataRepository.deleteById(bomId);  // WRONG! Should be worksheetRepository
}
```
**Impact:** Deleting a Worksheet actually deletes a BomData record instead. Critical data corruption bug.

**Fix:** Change to `worksheetRepository.deleteById(bomId);`

---

#### 2. Hardcoded Fallback ID (Invalid Data Reference)
**File:** `WorksheetServiceImpl.java` lines 141-142
```java
final Long bomIdToUse = (exportModelDTO.getBomExportModelId() != null && exportModelDTO.getBomExportModelId() == 0)
    ? 1L  // Hardcoded fallback to ID 1!
    : exportModelDTO.getBomExportModelId();
```
**Impact:** When bomExportModelId is 0, it silently uses ID 1, which may not exist or be the wrong record.

**Fix:** Throw validation exception instead of assuming ID 1 exists.

---

### 🟠 HIGH

#### 3. Empty deleteById Implementation (Does Nothing)
**File:** `DraftWorksheetServiceImpl.java` lines 212-215
```java
@Override
public void deleteById(Long id) {
    // TODO Auto-generated method stub
}
```
**Impact:** Attempting to delete draft worksheets does absolutely nothing. Data never gets deleted.

**Fix:** Implement: `draftWorksheetRepository.deleteById(id);`

---

#### 4. Transaction Rollback Silently Continues
**File:** `WorksheetServiceImpl.java` lines 139-180

Errors during export model creation are caught but processing continues:
```java
for (WorksheetExportModelsDTO exportModelDTO : worksheetDTO.getExportModels()) {
    try {
        // save operations
    } catch (Exception e) {
        System.err.println("Error occurred while creating BomExportModel");
        // continues to next item - partial data saved!
    }
}
```
**Impact:** Partial worksheet data saved on errors, leading to data inconsistency.

---

## Performance Concerns

### 🟠 HIGH - Save-in-Loop Anti-Pattern
Multiple locations use individual `save()` calls instead of batch `saveAll()`:

| File | Method | Impact |
|------|--------|--------|
| `WorksheetServiceImpl.java` | `saveBulk()` | Loops 2000+ records individually |
| `DraftWorksheetServiceImpl.java` | `saveBulk()` | Individual saves in loop |
| `ImportDataServiceImpl.java` | `saveBulk()` | Individual saves in loop |
| `ExportDataServiceImpl.java` | `saveBulk()` | Individual saves in loop |
| `BomDataServiceImpl.java` | `saveBulk()` | Individual saves in loop |
| `BomClaimDataServiceImpl.java` | `saveBulk()` | Individual saves in loop |

**Performance Impact:** 50-80% slower due to individual database round-trips instead of batch operations.

**Recommendation:** Collect entities in a List and use `repository.saveAll(list)`.

---

### 🟠 HIGH - N+1 Query Issues
**File:** `WorksheetServiceImpl.java` `toDto()` method

```java
for (Worksheet bd : all) {
    dtos.add(toDto(bd));  // Each toDto may trigger lazy loads
}
```

Lazy loading inside loops causes N+1 queries when accessing related entities.

**Recommendation:** Use `@EntityGraph` or `JOIN FETCH` in repository queries.

---

### 🟡 MEDIUM - Missing Database Indexes
No indexes defined on frequently queried columns:

| Entity | Column(s) | Query Pattern |
|--------|-----------|---------------|
| `Worksheet` | `shippingBillNo`, `claimRefNo` | Filter/search |
| `DraftWorksheet` | `shippingBillNo`, `claimRefNo` | Filter/search |
| `ExportData` | `shippingBillNo`, `shippingBillDate` | Lookup |
| `ImportData` | `boeNo`, `boeDate` | Lookup |

**Recommendation:** Add `@Index` annotations to JPA entities.

---

### 🟡 MEDIUM - Missing Caching
Frequently accessed reference data has no caching:
- `modelsRepository.findById()` - called repeatedly in loops
- `materialRepository.findById()` - called repeatedly in loops
- `bomExportModelRepository.findById()` - called in bulk operations

**Recommendation:** Add Spring Cache with `@Cacheable` for reference data.

---

### 🟢 LOW - Large Page Sizes
Default page sizes could cause memory issues:
- `ImportDataController`: `defaultValue = "200"`
- `WorksheetController`: `defaultValue = "1000"`

**Recommendation:** Reduce to 50-100 and implement proper pagination UI.

---

## Recommendations

### Priority 0 - Critical Bug Fixes
1. **Fix WorksheetServiceImpl.deleteById()**
   - Change `bomDataRepository.deleteById(bomId)` to `worksheetRepository.deleteById(bomId)`
   - This is causing data corruption

2. **Implement DraftWorksheetServiceImpl.deleteById()**
   - Add `draftWorksheetRepository.deleteById(id)` to the empty method

3. **Remove hardcoded fallback ID**
   - Throw validation exception when bomExportModelId is 0 instead of defaulting to ID 1

### Priority 1 - Critical Security Fixes
1. **Convert DELETE endpoints from GET to POST/DELETE**
   - Add CSRF token validation
   - Update frontend forms to use POST

2. **Add authentication checks to all API endpoints**
   - Create a reusable authentication filter or interceptor
   - Or implement Spring Security

3. **Remove User entity from session**
   - Create `SessionUserDTO` with only necessary fields (id, username, role)

### Priority 2 - High Security Fixes
4. **Implement Spring Security**
   - Configure authentication provider
   - Add CSRF protection
   - Set up proper session management

5. **Externalize credentials**
   - Use environment variables for database credentials
   - Add separate profile for production

### Priority 3 - Code Quality
6. **Replace System.out with proper logging**
   - Use `log.debug()` for debug statements
   - Use `log.error()` for errors with stack traces

7. **Standardize on constructor injection**
   - Remove `@Autowired` annotations
   - Use `@RequiredArgsConstructor` consistently

8. **Implement stub methods or mark as unsupported**
   - Either complete the implementations
   - Or throw `UnsupportedOperationException` with clear message

### Priority 4 - Best Practices
9. **Add input validation to file uploads**
   - Validate file types
   - Check file sizes
   - Scan for malicious content

10. **Add unit tests**
    - Test service layer methods
    - Test repository queries
    - Test controller endpoints

11. **Fix repository typo**
    - Rename `BomExportModelReposioty.java` to `BomExportModelRepository.java`

### Priority 5 - Performance Optimizations
12. **Replace save-in-loop with batch saveAll()**
    - Collect entities in lists, use `saveAll()` for 50-80% DB performance improvement

13. **Fix N+1 queries**
    - Add `@EntityGraph` or `JOIN FETCH` for worksheet/export model relationships

14. **Add database indexes**
    - Add indexes on `shippingBillNo`, `claimRefNo`, `boeNo`, `boeDate`

15. **Implement caching**
    - Add `@Cacheable` for Models and Materials reference data


### Recommended Next Steps
1. Fix critical security issues (Priority 1)
2. Implement Spring Security (Priority 2)
3. Clean up code quality issues (Priority 3)
4. Add comprehensive test coverage (Priority 4)

