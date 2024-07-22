# Order Management System

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
5. [Usage](#usage)
6. [API Documentation](#api-documentation)
7. [Contributing](#contributing)
8. [License](#license)

## Introduction
The Order Management System is a Spring Boot application designed to manage orders and products. It provides RESTful APIs to create, retrieve, and update orders and products.

## Features
- Create new orders
- Update product quantities
- Retrieve all orders
- Validate order data
- Handle transactions and rollback on failure

## Technologies Used
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Liquibase
- MapStruct
- JUnit
- Mockito
- Swagger/OpenAPI
- Docker

## Getting Started

### Prerequisites
- Java 17
- Maven
- Docker (for containerization)

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/MohamedELsisi/order-management-system.git
    cd order-management-system
    ```

2. Install the dependencies:
    ```sh
    mvn clean install
    ```

### Running the Application
1. Run the application using Maven:
    ```sh
    mvn spring-boot:run
    ```

## Usage
After running the application, you can access the API at `http://localhost:8080/api/v1/orders`.

### Example Endpoints:
- `POST /api/v1/orders` - Create a new order
- `GET /api/v1/orders` - Get all orders

## API Documentation
Swagger UI is integrated for API documentation. After starting the application, you can access it at `http://localhost:8080/orderManagement/swagger-ui/index.html`.
[Download Swagger File](https://github.com/MohamedELsisi/order-management-system/blob/master/src/main/resources/swagger.yaml)
## Postman Collection
To help you test the API, a Postman collection is available. You can download and import it into Postman using the link below:

[Download Postman Collection](https://github.com/MohamedELsisi/order-management-system/blob/master/src/main/resources/Orders-Collection.postman_collection.json)