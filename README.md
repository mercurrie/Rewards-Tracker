# Rewards Tracker API

A comprehensive Spring Boot REST API for managing transaction data and tracking rewards. This application provides CRUD operations for financial transactions with auto-generated IDs, timestamps, MySQL database persistence, and robust error handling.

## Features

- **Complete Transaction Management**: Create, read, update, and delete transactions
- **Auto-Generated IDs**: Transaction IDs are automatically generated and incremented (format: `txn_XXXXXX`)
- **Auto-Generated Timestamps**: Transaction dates are automatically set to current date
- **MySQL Database Integration**: Persistent data storage with JPA/Hibernate
- **CSV Data Loading**: Optional CSV file loading on startup (configurable)
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **Comprehensive Testing**: Unit tests, integration tests, and repository tests
- **Advanced Querying**: Find transactions by user ID, category, or transaction ID
- **Logging**: Structured logging with Log4j2 for debugging and monitoring
- **Error Handling**: Graceful error handling with detailed logging
- **Postman Collection**: Ready-to-use Postman collection for API testing.

## Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.5.3** - Main framework
- **Spring Data JPA** - Database persistence
- **MySQL** - Primary database
- **H2 Database** - In-memory database for testing
- **OpenCSV 5.7.1** - CSV file processing
- **Log4j2** - Logging framework
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework for tests
- **Gradle** - Build tool and dependency management

## Project Structure

```
src/
├── main/
│   ├── java/com/javaproject/demo/
│   │   ├── DemoApplication.java               # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── TransactionController.java     # REST API controller
│   │   ├── model/
│   │   │   ├── Transaction.java               # JPA entity model
│   │   │   └── TransactionCsv.java           # CSV data model
│   │   ├── repository/
│   │   │   └── TransactionRepository.java     # JPA repository interface
│   │   ├── service/
│   │   │   └── DataLoader.java               # CSV loading service
│   │   └── config/
│   │       └── LocalDateConverter.java       # Date converter for CSV
│   └── resources/
│       ├── application.properties            # App configuration
│       ├── log4j2.xml                       # Logging configuration
│       └── transactions.csv                # Sample transaction data
└── test/
    ├── java/com/javaproject/demo/
    │   ├── controller/
    │   │   └── TransactionControllerTest.java # Controller unit tests
    │   ├── repository/
    │   │   └── TransactionRepositoryTest.java # Repository tests
    │   ├── integration/
    │   │   └── TransactionIntegrationTest.java # Integration tests
    │   ├── model/
    │   │   ├── TransactionTest.java           # Model tests
    │   │   └── TransactionCsvTest.java        # CSV model tests
    │   └── DemoApplicationTests.java          # Basic application tests
    └── resources/
        ├── application-test.properties        # Test configuration
        ├── log4j2-test.xml                   # Test logging config
        └── test-transactions.csv             # Test data
```

## Getting Started

### Prerequisites

- **Java 17** or later
- **MySQL 8.0** or later
- **Gradle** (wrapper included)
- **Git** for cloning the repository

### Database Setup

1. **Install MySQL** (if not already installed)
   ```bash
   # macOS with Homebrew
   brew install mysql
   
   # Ubuntu/Debian
   sudo apt install mysql-server
   
   # Windows: Download from https://dev.mysql.com/downloads/mysql/
   ```

2. **Create database and user**
   ```sql
   CREATE DATABASE mydb;
   CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON mydb.* TO 'user'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Installation and Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Rewards-Tracker
   ```

2. **Configure database connection** (Optional)
   - Edit `src/main/resources/application.properties` if you need different database credentials
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=user
   spring.datasource.password=password
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

5. **Stop the application**
   You can stop the running application in several ways:
   - **Ctrl+C** in the terminal where it's running
   - **Find and kill the process** (if running in background):
     ```bash
     # Find the process
     ps aux | grep "com.javaproject.demo.DemoApplication" | grep -v grep
     
     # Kill by process ID
     kill <PID>
     
     # Or kill all Java processes (use with caution)
     pkill -f "com.javaproject.demo.DemoApplication"
     ```
   - **Using Gradle** (if you need to clean build artifacts):
     ```bash
     ./gradlew clean
     ```

The application will start on `http://localhost:8080`

**Note**: The application will automatically create the necessary database tables on first run using JPA/Hibernate DDL auto-generation.

## API Endpoints

### Base URL
```
http://localhost:8080/api/transactions
```

### Available Endpoints

| Method | Endpoint | Description | Required Fields | Response |
|--------|----------|-------------|----------------|----------|
| GET    | `/api/transactions` | Get all transactions from database | None | JSON array of transactions |
| GET    | `/api/transactions/{id}` | Get transaction by ID | None | JSON transaction object or null |
| GET    | `/api/transactions/user/{userId}` | Get all transactions for a user | None | JSON array of transactions |
| GET    | `/api/transactions/category/{category}` | Get all transactions by category | None | JSON array of transactions |
| POST   | `/api/transactions` | Create new transaction | userId, amount, category | JSON transaction object |
| PUT    | `/api/transactions/{id}` | Update existing transaction | amount, category | JSON transaction object or null |
| DELETE | `/api/transactions/{id}` | Delete transaction | None | Success/failure message |

### Request/Response Examples

#### Get All Transactions
```http
GET /api/transactions
```

**Response:**
```json
[
  {
    "transactionId": "txn_100000",
    "userId": "user_40",
    "amount": 111.26,
    "category": "travel",
    "transactionDate": "2025-04-20"
  },
  {
    "transactionId": "txn_100001",
    "userId": "user_74",
    "amount": 364.43,
    "category": "electronics",
    "transactionDate": "2025-06-06"
  }
]
```

#### Get Transaction by ID
```http
GET /api/transactions/txn_100000
```

**Response:**
```json
{
  "transactionId": "txn_100000",
  "userId": "user_40",
  "amount": 111.26,
  "category": "travel",
  "transactionDate": "2025-04-20"
}
```

#### Get Transactions by User ID
```http
GET /api/transactions/user/user_40
```

**Response:** Array of all transactions for the specified user.

#### Get Transactions by Category
```http
GET /api/transactions/category/travel
```

**Response:** Array of all transactions in the specified category.

#### Create New Transaction
```http
POST /api/transactions
Content-Type: application/json

{
  "userId": "user_99",
  "amount": 123.45,
  "category": "travel"
}
```

**Response:**
```json
{
  "transactionId": "txn_100002",
  "userId": "user_99",
  "amount": 123.45,
  "category": "travel",
  "transactionDate": "2025-07-29"
}
```

**Note:** Transaction ID and date are automatically generated.

#### Update Transaction
```http
PUT /api/transactions/txn_100000
Content-Type: application/json

{
  "amount": 200.00,
  "category": "travel"
}
```

**Response:**
```json
{
  "transactionId": "txn_100000",
  "userId": "user_40",
  "amount": 200.00,
  "category": "travel",
  "transactionDate": "2025-04-20"
}
```

**Note:** Only amount and category can be updated. ID, userId, and date are preserved.

#### Delete Transaction
```http
DELETE /api/transactions/txn_100000
```

**Response:**
```
Transaction with ID txn_100000 deleted successfully.
```

Or if not found:
```
Transaction with ID txn_100000 not found.
```

## Transaction Categories

The system supports the following transaction categories:
- `travel`
- `electronics`
- `dining`
- `utilities`
- `groceries`
- `fuel`
- `clothing`
- `entertainment`

## Data Model

### Transaction (JPA Entity)
```java
{
  "transactionId": "string",    // Primary Key, Auto-generated (txn_XXXXXX)
  "userId": "string",          // Required, max 20 characters
  "amount": "number",          // Required, BigDecimal with 2 decimal places
  "category": "string",        // Required, max 50 characters
  "transactionDate": "date"    // Auto-generated on creation (YYYY-MM-DD)
}
```

### TransactionCsv (DTO for CSV operations)
Similar structure to Transaction entity but used for CSV parsing and API request/response bodies.

## Database Schema

The application automatically creates the following MySQL table:

```sql
CREATE TABLE transactions (
    transaction_id VARCHAR(20) PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL
);
```

## Testing with Postman

A complete Postman collection is included in the project:

1. **Import the collection**
   - Open Postman
   - Click "Import"
   - Select `Rewards_Tracker_API.postman_collection.json`

2. **Available requests:**
   - Get All Transactions
   - Get Transaction by ID
   - Create New Transaction (multiple examples)
   - Update Transaction
   - Delete Transaction

## Configuration

### Application Properties
The application can be configured via `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# CSV file path (optional)
transactions.file.path=src/main/resources/transactions.csv

# Server configuration
server.port=8080
```

### Environment-Specific Configuration
- **Development**: Uses `application.properties` with MySQL
- **Testing**: Uses `application-test.properties` with H2 in-memory database

### CSV Data Loading
The application can optionally load initial transaction data from `transactions.csv` on startup. This feature is currently disabled by default but can be enabled by uncommenting the `@Component` annotation in `DataLoader.java`.

The CSV file should have the following format:
```csv
transaction_id,user_id,amount,category,transaction_date
txn_100000,user_40,111.26,travel,2025-04-20
txn_100001,user_74,364.43,electronics,2025-06-06
```

### Logging Configuration
The application uses Log4j2 for logging with configuration in `log4j2.xml`:
- **Console logging**: INFO level and above
- **File logging**: DEBUG level to `logs/rewards-tracker.log`
- **Rolling file**: Daily rotation with compression

## Development

### Building
```bash
./gradlew build
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests TransactionControllerTest

# Run tests with verbose output
./gradlew test --info
```

### Running in Development Mode
```bash
# Standard run
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run with debug logging
./gradlew bootRun --args='--logging.level.com.javaproject.demo=DEBUG'
```

### Code Quality and Testing

The project includes comprehensive testing:
- **Unit Tests**: Controller, Repository, and Model tests
- **Integration Tests**: Full application context tests
- **Test Coverage**: High test coverage across all layers

### Development Tools
- **H2 Console**: Available in test mode at `http://localhost:8080/h2-console`
- **SQL Logging**: Enabled by default to see generated queries
- **Log Files**: Available in `logs/` directory for debugging

### Hot Reload
For development convenience, you can use Spring Boot DevTools by adding it to your dependencies for automatic application restart on code changes.

## Error Handling

The application provides comprehensive error handling:

- **200 OK**: Successful operations (including when requested resource is not found, returns null)
- **400 Bad Request**: Invalid request data or format
- **500 Internal Server Error**: Unexpected server errors (check logs for details)

### Common Error Scenarios
- **Transaction Not Found**: Returns `null` in response body with 200 status
- **Invalid Transaction Data**: Validation errors logged with details
- **Database Connection Issues**: Logged with full stack trace
- **CSV Loading Errors**: Application continues with empty transaction list

### Logging Levels
- **ERROR**: Critical errors requiring attention
- **WARN**: Warning conditions (e.g., transaction not found)
- **INFO**: General application flow information
- **DEBUG**: Detailed debugging information (method entry/exit, SQL queries)

## Troubleshooting

### Common Issues

1. **Application won't start - Port 8080 already in use**
   ```bash
   # Find what's using the port
   lsof -i :8080
   
   # Kill the process
   kill <PID>
   
   # Or change the port in application.properties
   server.port=8081
   ```

2. **Database connection failed**
   - Verify MySQL is running: `brew services start mysql` (macOS)
   - Check database credentials in `application.properties`
   - Ensure database `mydb` exists and user has proper permissions

3. **Tests failing**
   - Ensure H2 test database dependencies are included
   - Check test-specific configuration in `application-test.properties`

4. **CSV loading issues**
   - Verify CSV file path in `application.properties`
   - Check CSV format matches expected headers
   - Review logs for specific parsing errors

### Performance Considerations
- **Database Indexing**: Consider adding indexes on frequently queried columns (userId, category)
- **Pagination**: For large datasets, implement pagination on GET endpoints
- **Connection Pooling**: HikariCP is configured for optimal database connections

## Testing the Application

### Quick Test with cURL

Once the application is running, you can quickly test it:

```bash
# Get all transactions
curl http://localhost:8080/api/transactions

# Get a specific transaction (replace with actual ID)
curl http://localhost:8080/api/transactions/txn_100000

# Create a new transaction
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"userId":"test_user","amount":50.00,"category":"dining"}'

# Get transactions by user
curl http://localhost:8080/api/transactions/user/test_user

# Get transactions by category
curl http://localhost:8080/api/transactions/category/dining
```

### Monitoring Application Logs

To monitor the application logs in real-time:

```bash
# View latest logs
tail -f logs/rewards-tracker.log

# Search for specific log entries
grep "CSV" logs/rewards-tracker.log
grep "ERROR" logs/rewards-tracker.log
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions or issues, please create an issue in the repository or contact the development team.
