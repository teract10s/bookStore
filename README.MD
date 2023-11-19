# **Online Book Store Project**

Welcome to the Online Book Store project! This project is a Java-based web application built using Spring Boot, Hibernate, Liquibase, and other technologies to create a robust online bookstore.

## **Table of Contents**
1. ### Introduction 
2. ### Technologies Used
3. ### Features
4. ### Getting Started
5. ### API Documentation
6. ### Security
7. ### Roles
8. ### Contributing

**Introduction**

This project is an online bookstore that provides a platform for users to explore and purchase books. It is built using Java with the Spring Boot framework, utilizing technologies like Hibernate for database interaction, Liquibase for database versioning, Swagger for API documentation, and JWT for secure user authentication.

Technologies Used
Java
Spring Boot (Web, JPA, Security)
Hibernate
Liquibase
Swagger
Pagination
Features
User Features
Shopping Cart:
Add books to the shopping cart
Create cart items
Update shopping cart contents
Orders:
Create orders from the shopping cart
View and track order status
Book Listings:
Get a list of all books
Filter books by categories
Admin Features
Book Management:
Create new books
Update existing books
Category Management:
Create new categories
Update existing categories
Order Management:
Update user order statuses
Getting Started
To run the project locally, follow these steps:

Clone the repository: git clone [repository-url]
Navigate to the project directory: cd online-book-store
Build the project: mvn clean install
Run the application: java -jar target/online-book-store.jar
The application will be accessible at http://localhost:8080.

API Documentation
API documentation is available using Swagger. After starting the application, visit http://localhost:8080/swagger-ui.html to explore and interact with the API endpoints.

Security
The project uses JWT tokens for secure user authentication. Make sure to include the token in the headers of your requests for authorization.

Roles
The system has two roles:

User:

Can interact with the shopping cart
Can create and view orders
Can access a list of all books and categories
Admin:

Has full control over book and category management
Can update user order statuses
Contributing
Feel free to contribute to the project. Fork the repository, create a branch, make your changes, and submit a pull request.

License
This project is licensed under the MIT License.

Feel free to customize this README file according to your project's specific details and add any additional sections that might be relevant.