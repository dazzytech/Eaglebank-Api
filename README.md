# Eaglebank-Api
Spring boot application which is the backend api for a fictional bank

# Running the application
Using Maven

```mvn spring-boot:run```

The API will start on:

```http://localhost:8080```

Access Swagger using the openAPI doc on:

```http://localhost:8080/swagger.html```

# Technical Stack

- Java 26 
- Springboot 
- H2
- JWT

# Endpoints

Users

    GET /v1/users/{id}

    PUT /v1/users/{id}

Accounts

    GET /v1/accounts

    GET /v1/accounts/{accountId}

Transactions

    GET /v1/accounts/{accountId}/transactions

    GET /v1/accounts/{accountId}/transactions/{transactionId}

Full details available in Swagger UI.

# Potential Improvements 

- redis caching
- rate limiting
- adding payee mandates
- adding docker capability
- running in AWS
- add mySql DB or Postgres