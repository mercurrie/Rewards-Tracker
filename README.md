# Rewards Tracker API

A Spring Boot REST API for managing transaction data and tracking rewards. This application provides CRUD operations for financial transactions with auto-generated IDs and timestamps.

## Features

- **Transaction Management**: Create, read, update, and delete transactions
- **Auto-Generated IDs**: Transaction IDs are automatically generated and incremented
- **Auto-Generated Timestamps**: Transaction dates are automatically set to current date
- **CSV Data Loading**: Loads initial transaction data from CSV file on startup
- **RESTful API**: Clean REST endpoints for all operations
- **Postman Collection**: Ready-to-use Postman collection for API testing

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **OpenCSV 5.7.1** - For CSV file processing
- **Gradle** - Build tool

## Project Structure

```
src/
├── main/
│   ├── java/com/javaproject/demo/
│   │   ├── DemoApplication.java          # Main Spring Boot application
│   │   ├── HelloController.java          # REST API controller
│   │   ├── TransactionCsv.java          # Transaction data model
│   │   └── LocalDateConverter.java       # Date converter for CSV
│   └── resources/
│       ├── application.properties        # App configuration
│       └── transactions.csv             # Sample transaction data
└── test/
    └── java/com/javaproject/demo/
        └── DemoApplicationTests.java    # Basic application tests
```

## Getting Started

### Prerequisites

- Java 17 or later
- Gradle (wrapper included)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Rewards-Tracker
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Base URL
```
http://localhost:8080/api/transactions
```

### Available Endpoints

| Method | Endpoint | Description | Required Fields |
|--------|----------|-------------|----------------|
| GET    | `/api/transactions` | Get all transactions | None |
| GET    | `/api/transactions/{id}` | Get transaction by ID | None |
| POST   | `/api/transactions` | Create new transaction | userId, amount, category |
| PUT    | `/api/transactions/{id}` | Update transaction | amount, category |
| DELETE | `/api/transactions/{id}` | Delete transaction | None |

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
```
Transaction created: TransactionCsv{transactionId='txn_102500', userId='user_99', amount=123.45, category='travel', transactionDate=2025-07-07}
```

**Note:** Transaction ID and date are automatically generated.

#### Update Transaction
```http
PUT /api/transactions/txn_100000
Content-Type: application/json

{
  "transactionId": "txn_100000",
  "userId": "user_40",
  "amount": 200.00,
  "category": "travel",
  "transactionDate": "2025-04-20"
}
```

**Response:**
```
Transaction updated: TransactionCsv{transactionId='txn_100000', userId='user_40', amount=200.0, category='travel', transactionDate=2025-04-20}
```

#### Delete Transaction
```http
DELETE /api/transactions/txn_100000
```

**Response:**
```
Transaction deleted with ID: txn_100000
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

### TransactionCsv
```java
{
  "transactionId": "string",    // Auto-generated (txn_XXXXXX)
  "userId": "string",          // Required
  "amount": "number",          // Required (BigDecimal)
  "category": "string",        // Required
  "transactionDate": "date"    // Auto-generated (YYYY-MM-DD)
}
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
# Server configuration
server.port=8080

# CSV file path (optional)
transactions.file.path=src/main/resources/transactions.csv
```

### CSV Data
The application loads initial transaction data from `transactions.csv` on startup. The CSV file should have the following format:

```csv
transaction_id,user_id,amount,category,transaction_date
txn_100000,user_40,111.26,travel,2025-04-20
txn_100001,user_74,364.43,electronics,2025-06-06
```

## Development

### Building
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Running in Development Mode
```bash
./gradlew bootRun
```

## Error Handling

- **404 Not Found**: When requesting a transaction that doesn't exist, returns `null`
- **500 Internal Server Error**: Check application logs for detailed error information

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
