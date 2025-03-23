# Prerequisites

In order to be able to submit your assignement, you should have the following installed:
* JDK >= 17
* Maven
* Postman / any other tool that allows you to hit the application's endpoints
* Any versioning tool
* Any IDE that allows you to run the application


# How to

#### Run the application
The application should be run as a SpringBootApplication. Below is a quick guide on how to do that via IntelliJ:
* Edit Configuration 
   * Add New Configuration (Spring Boot)
     * Change the **Main class** to **ing.assessment.INGAssessment**
       * Run the app.

#### Connect to the H2 database
Access the following url: **http://localhost:8080/h2-console/**
 * **Driver Class**: _**org.h2.Driver**_
 * **JDBC URL**: _**jdbc:h2:mem:testdb**_
 * **User Name**: _**sa**_
 * **Password**: **_leave empty_**



MY READ ME:


# Order Management System

This is a basic order management system built with Spring Boot, providing an API to manage orders and products in a store. The system uses an in-memory H2 database for development and testing purposes.

## Features

- **Order CRUD Operations**:
    - Create an order
    - Update an existing order
    - Retrieve orders by ID
    - Delete orders

- **Product CRUD Operations**:
    - Retrieve all products
    - Retrieve products by ID

- **Validation**:
    - Order must contain at least one product
    - Product quantity must be greater than zero

- **Cost Calculations**:
    - Delivery cost is free for orders above 500
    - Discount is applied for orders above 1000

- **Exception Handling**:
    - Handles invalid orders and out-of-stock products with custom exceptions.

## Technologies

- **Spring Boot**: For building the REST API
- **H2 Database**: In-memory database for development
- **Jakarta Validation**: For input validation
- **JUnit 5**: For unit testing

## Setup

1. Clone the repository:
   ```bash
   git clone <repository_url>
   ```

2. Navigate into the project directory:
   ```bash
   cd order-management-system
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access the H2 Console (optional):
    - URL: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: (leave empty)

## API Endpoints

### Orders
- **GET /orders**: Get all orders
- **GET /orders/{id}**: Get an order by ID
- **POST /orders**: Create a new order
- **PUT /orders/{id}**: Update an existing order
- **DELETE /orders/{id}**: Delete an order

### Products
- **GET /products**: Get all products
- **GET /products/{id}**: Get products by ID

## Example

### Create Order
```bash
POST /orders
{
  "timestamp": "2025-03-23T21:27:31.660+00:00",
  "orderCost": 500.0,
  "orderProducts": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### Error Handling
If an invalid order is attempted (e.g., product quantity is zero or the order is empty), an appropriate error message will be returned.

## Development

### Testing

JUnit tests are provided for unit testing service layers.

Run the tests using:
```bash
./mvnw test
```

