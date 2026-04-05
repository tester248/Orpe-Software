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
