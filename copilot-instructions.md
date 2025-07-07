# Copilot Instructions for Rewards Tracker Project

## Overview
This document provides instructions for GitHub Copilot to maintain consistency across project files when changes are made to the codebase.

## Auto-Update Instructions

### When HelloController.java is Modified

#### 1. Update README.md
- **Trigger**: Any changes to API endpoints, request/response formats, or new functionality in `HelloController.java`
- **Actions Required**:
  - Update API endpoint documentation with correct URLs, HTTP methods, and descriptions
  - Update request body examples to reflect current required fields
  - Update response examples to match actual controller responses
  - Add documentation for new endpoints or modify existing ones
  - Update any setup or usage instructions if the API behavior changes
  - Ensure all example transaction IDs reference valid data from `transactions.csv`

#### 2. Update Postman Collection (Rewards_Tracker_API.postman_collection.json)
- **Trigger**: Any changes to API endpoints, request/response formats, or new functionality in `HelloController.java`
- **Actions Required**:
  - Add new requests for any new endpoints created
  - Update existing request URLs if endpoint paths change
  - Update request body examples to reflect current required fields (e.g., only userId, amount, category for POST)
  - Update request descriptions to match current functionality
  - Add or remove request headers as needed
  - Update example values to use realistic data
  - Ensure all transaction IDs in examples exist in the CSV file
  - Update variable definitions if base URLs or common parameters change

### Specific Scenarios to Watch For

1. **New Endpoint Added**: Create corresponding Postman request with proper method, URL, headers, and body
2. **Endpoint URL Changed**: Update all affected Postman requests and README documentation
3. **Request Body Format Changed**: Update Postman request bodies and README examples
4. **Response Format Changed**: Update README response examples and Postman request descriptions
5. **Authentication Added**: Update all Postman requests with auth headers and README setup instructions
6. **Query Parameters Added**: Update Postman URLs and README documentation
7. **Error Handling Changed**: Update README with new error response examples

### File Locations
- **Source Controller**: `src/main/java/com/javaproject/demo/HelloController.java`
- **README**: `README.md` (create if doesn't exist)
- **Postman Collection**: `Rewards_Tracker_API.postman_collection.json`
- **CSV Data**: `src/main/resources/transactions.csv`

### Consistency Rules
1. **Transaction IDs**: Always use valid transaction IDs that exist in the CSV file for examples
2. **Request Bodies**: Ensure POST request examples only include required fields (userId, amount, category)
3. **URLs**: Maintain consistent base URL structure (`http://localhost:8080/api/transactions`)
4. **Descriptions**: Keep descriptions clear and reflect actual functionality
5. **Error Cases**: Include examples of both successful and error responses where applicable

### Quality Checks
Before finalizing updates, ensure:
- [ ] All URLs in Postman collection are correct and consistent
- [ ] README examples match actual API behavior
- [ ] Request body examples include only required fields for POST operations
- [ ] All example data uses realistic values
- [ ] Descriptions accurately reflect current functionality
- [ ] New endpoints have complete documentation in both files

## Notes
- This project uses auto-generated transaction IDs and dates for POST requests
- Only userId, amount, and category are required fields for creating transactions
- The CSV file contains sample transaction data for testing
- The API runs on `http://localhost:8080` by default
