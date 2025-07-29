# Copilot Instructions for Rewards Tracker Project

## Overview
This document provides instructions for GitHub Copilot to maintain consistency across project files when changes are made to the codebase.

## Auto-Update Instructions

### When TransactionController.java is Modified

#### 1. Update README.md
- **Trigger**: Any changes to API endpoints, request/response formats, or new functionality in `TransactionController.java`
- **Actions Required**:
  - Update API endpoint documentation with correct URLs, HTTP methods, and descriptions
  - Update request body examples to reflect current required fields
  - Update response examples to match actual controller responses
  - Add documentation for new endpoints or modify existing ones
  - Update any setup or usage instructions if the API behavior changes
  - Ensure all example transaction IDs reference valid data from `transactions.csv`

#### 2. Update Postman Collection (Rewards_Tracker_API.postman_collection.json)
- **Trigger**: Any changes to API endpoints, request/response formats, or new functionality in `TransactionController.java`
- **Actions Required**:
  - Add new requests for any new endpoints created
  - Update existing request URLs if endpoint paths change
  - Update request body examples to reflect current required fields (e.g., only userId, amount, category for POST)
  - Update request descriptions to match current functionality
  - Add or remove request headers as needed
  - Update example values to use realistic data
  - Ensure all transaction IDs in examples exist in the CSV file
  - Update variable definitions if base URLs or common parameters change

#### 3. Update JUnit Tests
- **Trigger**: Any changes to API endpoints, business logic, or new functionality in `TransactionController.java`
- **Actions Required**:
  - Create or update test methods for new endpoints
  - Modify existing test assertions to match new response formats
  - Update test data to reflect current request/response structures
  - Add edge case tests for new validation rules
  - Update mock data and expectations
  - Ensure test method names clearly describe what is being tested
  - Add integration tests for database operations if applicable
  - Update @Test annotations and expected exceptions if error handling changes

### When Logging is Added or Modified

#### 1. Update Application Configuration
- **Trigger**: Addition of logging statements, log level changes, or new logging frameworks
- **Actions Required**:
  - Update `application.properties` with appropriate logging configuration
  - Add logging dependencies to `build.gradle` if new frameworks are used
  - Configure log file output locations and rotation policies
  - Set appropriate log levels for different packages (DEBUG, INFO, WARN, ERROR)
  - Add logging profiles for different environments (dev, test, prod)

#### 2. Update README.md
- **Trigger**: New logging features or configuration changes
- **Actions Required**:
  - Document how to enable/disable different log levels
  - Explain log file locations and how to access them
  - Add troubleshooting section with common log patterns
  - Document any custom logging configuration
  - Include examples of important log messages for debugging

### When JUnit Tests are Added or Modified

#### 1. Update Test Documentation
- **Trigger**: New test classes, test methods, or testing strategies
- **Actions Required**:
  - Update README.md with testing instructions and coverage information
  - Document how to run specific test suites
  - Add examples of test execution commands
  - Document any test data setup requirements
  - Include information about integration vs unit tests

#### 2. Maintain Test Consistency
- **Trigger**: Changes to any test files in `src/test/java/`
- **Actions Required**:
  - Ensure test naming conventions are consistent across all test classes
  - Update shared test utilities or base classes
  - Maintain consistent mock data across related tests
  - Update test profiles in `application.properties` if needed
  - Ensure all tests use the same assertion libraries and patterns

### When Model Classes are Modified (Transaction.java, TransactionCsv.java)

#### 1. Update Related Components
- **Trigger**: Changes to entity fields, validation annotations, or data structure
- **Actions Required**:
  - Update `TransactionController.java` if new fields need API exposure
  - Update `TransactionRepository.java` if new query methods are needed
  - Update CSV file structure if new fields are added to TransactionCsv
  - Update Postman collection with new request/response field examples
  - Update README.md with new data model documentation

#### 2. Database Schema Updates
- **Actions Required**:
  - Update JPA annotations if database schema changes
  - Consider migration scripts for production databases
  - Update test data to match new model structure

### When Repository is Modified (TransactionRepository.java)

#### 1. Update Controller Usage
- **Trigger**: New query methods or repository interface changes
- **Actions Required**:
  - Update `TransactionController.java` to use new repository methods
  - Add corresponding API endpoints if new queries should be exposed
  - Update service classes that use the repository
  - Add tests for new repository methods

### When Configuration Changes (LocalDateConverter.java, application.properties)

#### 1. Update Documentation
- **Trigger**: Configuration changes or new converters
- **Actions Required**:
  - Update README.md with new configuration instructions
  - Update docker-compose.yml if database configuration changes
  - Update test configuration in application-test.properties if needed

### When Service Classes are Modified (DataLoader.java)

#### 1. Update Startup Behavior
- **Trigger**: Changes to data loading logic or service initialization
- **Actions Required**:
  - Update README.md if startup behavior changes
  - Update environment-specific instructions
  - Consider impact on existing data and migrations

### Specific Scenarios to Watch For

1. **New Endpoint Added**: Create corresponding Postman request with proper method, URL, headers, and body
2. **Endpoint URL Changed**: Update all affected Postman requests and README documentation
3. **Request Body Format Changed**: Update Postman request bodies and README examples
4. **Response Format Changed**: Update README response examples and Postman request descriptions
5. **Authentication Added**: Update all Postman requests with auth headers and README setup instructions
6. **Query Parameters Added**: Update Postman URLs and README documentation
7. **Error Handling Changed**: Update README with new error response examples

### File Locations
- **Source Controller**: `src/main/java/com/javaproject/demo/controller/TransactionController.java`
- **Models**: `src/main/java/com/javaproject/demo/model/` (Transaction.java, TransactionCsv.java)
- **Repository**: `src/main/java/com/javaproject/demo/repository/TransactionRepository.java`
- **Service**: `src/main/java/com/javaproject/demo/service/DataLoader.java` (currently disabled)
- **Configuration**: `src/main/java/com/javaproject/demo/config/LocalDateConverter.java`
- **README**: `README.md` (create if doesn't exist)
- **Postman Collection**: `Rewards_Tracker_API.postman_collection.json`
- **CSV Data**: `src/main/resources/transactions.csv`

### Consistency Rules
1. **Transaction IDs**: Always use valid transaction IDs that exist in the CSV file for examples
2. **Request Bodies**: Ensure POST request examples only include required fields (userId, amount, category)
3. **URLs**: Maintain consistent base URL structure (`http://localhost:8080/api/transactions`)
4. **Descriptions**: Keep descriptions clear and reflect actual functionality
5. **Error Cases**: Include examples of both successful and error responses where applicable

### When Logging is Added or Modified

#### 1. Update Application Dependencies
- **Trigger**: Addition of new logging frameworks or modification of logging configuration
- **Actions Required**:
  - Update `build.gradle` if new logging dependencies are added
  - Update `application.properties` with appropriate logging levels and configurations
  - Document logging configuration in README.md under setup instructions

#### 2. Maintain Logging Consistency
- **Actions Required**:
  - Ensure all controllers use consistent logging patterns (INFO for successful operations, ERROR for exceptions, DEBUG for detailed flow)
  - Use meaningful log messages that include relevant transaction IDs, user IDs, or operation details
  - Follow standard logging format: `[Operation] - [Details] - [Result/Error]`
  - Add logging to new endpoints following existing patterns in `HelloController.java`

### When JUnit Tests are Added or Modified

#### 1. Update Test Documentation
- **Trigger**: Addition of new test classes or modification of existing test methods
- **Actions Required**:
  - Update README.md with testing instructions and how to run tests
  - Document test coverage and what each test class validates
  - Include gradle test commands and expected output examples

#### 2. Maintain Test Consistency
- **Actions Required**:
  - Follow naming convention: `[MethodName][Scenario]Test` (e.g., `createTransactionValidInputTest`)
  - Use `@Test`, `@BeforeEach`, `@AfterEach` annotations consistently
  - Mock external dependencies (database, repositories) appropriately
  - Test both success and failure scenarios for each endpoint
  - Include validation tests for auto-generated fields (ID, date)
  - Test data integrity and business logic validation

#### 3. Test File Structure
- **Location**: `src/test/java/com/javaproject/demo/`
- **Required Test Classes**:
  - `TransactionControllerTest.java` - Controller endpoint testing
  - `TransactionRepositoryTest.java` - Database operations testing
  - `DataLoaderTest.java` - CSV loading functionality testing (in service package)
  - `TransactionTest.java` - Entity model testing
  - `TransactionCsvTest.java` - CSV model testing

#### 4. Test Coverage Requirements
- **CRUD Operations**: Test all GET, POST, PUT, DELETE endpoints
- **Validation**: Test required field validation and data format validation
- **Auto-Generation**: Test that IDs and dates are properly generated
- **Error Handling**: Test 404, 400, and 500 error scenarios
- **Database Integration**: Test repository methods and data persistence

### Quality Checks
Before finalizing updates, ensure:
- [ ] All URLs in Postman collection are correct and consistent
- [ ] README examples match actual API behavior
- [ ] Request body examples include only required fields for POST operations
- [ ] All example data uses realistic values
- [ ] Descriptions accurately reflect current functionality
- [ ] New endpoints have complete documentation in both files
- [ ] Logging statements are added to new methods with appropriate levels
- [ ] JUnit tests cover new functionality with both positive and negative test cases
- [ ] Test documentation is updated in README.md
- [ ] All tests pass and maintain existing coverage standards

## Notes
- This project uses auto-generated transaction IDs and dates for POST requests
- Only userId, amount, and category are required fields for creating transactions
- The CSV file contains sample transaction data for testing
- The API runs on `http://localhost:8080` by default
- Use SLF4J logging framework with appropriate log levels (DEBUG, INFO, WARN, ERROR)
- JUnit 5 is the preferred testing framework with Mockito for mocking dependencies
- Maintain minimum 80% test coverage for all controller and service methods

## Package Structure
- **controller/**: REST API endpoints and request handling
- **model/**: JPA entities and data transfer objects (DTOs)
- **repository/**: Data access layer with JPA repositories
- **service/**: Business logic and data processing services
- **config/**: Configuration classes and converters

## Import Guidelines
When adding new files or modifying existing ones, ensure proper imports:
- Controllers import from `com.javaproject.demo.model.*` and `com.javaproject.demo.repository.*`
- Services import from `com.javaproject.demo.model.*` and `com.javaproject.demo.repository.*`
- Models should be independent but may reference config classes for converters
- Repository interfaces should only import model classes they work with
