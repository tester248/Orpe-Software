# Remediation Changelog

Tracks changes made after the testing report commit `b6f24b0` (`docs: Add comprehensive testing and code review report`).

## Baseline (Since Report Was Added)

These commits were already present on `dev` after the report commit:

- `3232301` - Feat: SB Wise DBK Calculation
- `0cfbc5b` - Complete Project
- `488113d` - Final Draft
- `ed1d502` - Merge upstream: SB-wise DBK calc, DB backup, export Excel download, new templates + add README

## Imported From Upstream Main

- `6710563` - Last Commit
  - Cherry-picked from `upstream/main` commit `a96341a`
  - Included changes in:
    - `src/main/java/com/orpe/consultants/controller/NavigationController.java`
    - `src/main/java/com/orpe/consultants/service/impl/ImportDataServiceImpl.java`
    - `src/main/resources/META-INF/additional-spring-configuration-metadata.json`
    - `src/main/resources/application.properties`
    - `src/main/resources/templates/importDataList.html`

## Applied Fixes (Priority Order)

### Priority 0 - Critical Bug Fixes

- `P0-1` Worksheet delete data-corruption bug fixed
  - File: `src/main/java/com/orpe/consultants/service/impl/WorksheetServiceImpl.java`
  - Change: `deleteById(Long bomId)` now deletes from `worksheetRepository` instead of `bomDataRepository`
  - Report mapping: Critical Bugs -> Wrong repository in `deleteById`

- `P0-2` Draft worksheet delete implemented
  - File: `src/main/java/com/orpe/consultants/service/impl/DraftWorksheetServiceImpl.java`
  - Change: implemented `deleteById(Long id)` using `draftWorksheetRepository.deleteById(id)` with null-id validation
  - Report mapping: Critical Bugs -> Empty `deleteById` implementation in DraftWorksheetServiceImpl

- `P0-3` Hardcoded fallback ID removed from worksheet export-model handling
  - File: `src/main/java/com/orpe/consultants/service/impl/WorksheetServiceImpl.java`
  - Change: replaced fallback-to-`1L` logic with strict validation via `requireValidBomExportModelId(...)`
  - Behavior: null or zero `bomExportModelId` now fails fast with clear `IllegalArgumentException`
  - Report mapping: Critical Bugs -> Hardcoded fallback ID (invalid data reference)

### Priority 1 - Critical Security Fixes

- `P1-1` Converted delete operations from GET to POST
  - Controller mappings updated:
    - `src/main/java/com/orpe/consultants/controller/UserController.java`
    - `src/main/java/com/orpe/consultants/controller/ImportDataController.java`
    - `src/main/java/com/orpe/consultants/controller/ExportDataController.java`
    - `src/main/java/com/orpe/consultants/controller/BomDataController.java`
    - `src/main/java/com/orpe/consultants/controller/BomClaimController.java`
  - Template actions updated from link-based delete to POST forms:
    - `src/main/resources/templates/userList.html`
    - `src/main/resources/templates/importDataList.html`
    - `src/main/resources/templates/exportDataList.html`
    - `src/main/resources/templates/bomClaimList.html`
  - Report mapping: Security Issues -> DELETE operations using GET requests (CSRF risk)

- `P1-2` Added session authentication checks to bulk API endpoints
  - Updated endpoints:
    - `POST /importdata/bulk-save` in `src/main/java/com/orpe/consultants/controller/ImportDataController.java`
    - `POST /exportdata/bulk-save` in `src/main/java/com/orpe/consultants/controller/ExportDataController.java`
    - `POST /draftworksheet/updateBulk` in `src/main/java/com/orpe/consultants/controller/WorksheetController.java`
  - Change: endpoints now reject unauthenticated calls with HTTP 401
  - Report mapping: Security Issues -> Missing Authentication on API Endpoints

- `6710563` - Last Commit
  - Cherry-picked from `upstream/main` commit `a96341a`
  - Included changes in:
    - `src/main/java/com/orpe/consultants/controller/NavigationController.java`
    - `src/main/java/com/orpe/consultants/service/impl/ImportDataServiceImpl.java`
    - `src/main/resources/META-INF/additional-spring-configuration-metadata.json`
    - `src/main/resources/application.properties`
    - `src/main/resources/templates/importDataList.html`

## In Progress: Priority Fixes

Next fixes are being applied in report priority order, with one commit per fix and each entry recorded here.
