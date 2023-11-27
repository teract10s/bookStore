# **📚Online Book Store Project**

Welcome to the Online Book Store project! This project is a Java-based web application built using Spring Boot,
Hibernate, Liquibase, and other technologies to create a robust online bookstore.

## **Table of Contents**

1. ### [📝Introduction](#1-introduction)
2. ### [💻Technologies Used](#2-technologies-used)
3. ### [💾Database structure](#3-database-structure)
4. ### [📃API Documentation](#4-api-documentation)
5. ### [🪪Security](#5-security)
6. ### [🏁Getting Started](#6-getting-started)

## **📝1. Introduction**

This project is an online bookstore that provides a platform for users to explore and purchase books. It is built using
Java with the Spring Boot framework, utilizing technologies like Hibernate for database interaction, Liquibase for
database versioning, Swagger for API documentation, and JWT for secure user authentication.

## **💻2. Technologies Used**

* Java 17
* Spring (Boot, Web, JPA, Security)
* Hibernate
* Liquibase
* Docker
* AWS
* JUnit 5
* Mockito
* Swagger

## **💾3. Database structure**

![img.png](img.png)

## **📃4. API Documentation**

### **Authentication Controller:**

| **HTTP method** |     **Endpoint**     | **Role** | **Function**                            |
|:---------------:|:--------------------:|:--------:|:----------------------------------------|
|      POST       | `/api/auth/register` |   ALL    | Allows users to register a new account. |
|      POST       |  `/api/auth/login`   |   ALL    | Get JWT tokens for authentication.      |

---

### **Book Controller:** _Managing books_

| **HTTP method** |    **Endpoint**     | **Role** | **Function**                            |
|:---------------:|:-------------------:|:--------:|:----------------------------------------|
|       GET       |    `/api/books`     |   USER   | Get all books from database.            |
|       GET       |  `/api/books/{id}`  |   USER   | Get specific book by id.                |
|       GET       | `/api/books/search` |   USER   | Find books by titles or/and authors.    |
|      POST       |   ` /api/books `    |  ADMIN   | Create new book.                        |
|       PUT       |  `/api/books/{id}`  |  ADMIN   | Update existing book by id in database. |
|     DELETE      |  `/api/books/{id}`  |  ADMIN   | Delete existing book by id in database. |

---

### **Category Controller:** _Managing categories_

| **HTTP method** |             **Endpoint**             | **Role** | **Function**                                |
|:---------------:|:------------------------------------:|:--------:|:--------------------------------------------|
|       GET       |          `/api/categories`           |   USER   | Get all categories from database.           |
|       GET       |        `/api/categories/{id}`        |   USER   | Get specific category by id.                |
|       GET       | `/api/categories/{categoryId}/books` |   USER   | Get books by category.                      |
|      POST       |          `/api/categories`           |  ADMIN   | Create new category.                        |
|       PUT       |        `/api/categories/{id}`        |  ADMIN   | Update existing category by id in database. |
|     DELETE      |        `/api/categories/{id}`        |  ADMIN   | Delete existing category by id in database. |

---

### **Order Controller:** _Managing orders_

| **HTTP method** |              **Endpoint**              | **Role** | **Function**                            |
|:---------------:|:--------------------------------------:|:--------:|:----------------------------------------|
|       GET       |             `/api/orders`              |   USER   | Get all user's orders.                  |
|       GET       |     `/api/orders/{orderId}/items`      |   USER   | Get all items for a specific order.     |
|       GET       | `/api/orders/{orderId}/items/{itemId}` |   USER   | Get specific item for a specific order. |
|      POST       |             `/api/orders`              |   USER   | Create new order.                       |
|      PATCH      |           `/api/orders/{id}`           |  ADMIN   | Update order's status.                  |

---

### **Shopping cart Controller:** _Managing shopping carts_

| **HTTP method** |             **Endpoint**             | **Role** | **Function**                                          |
|:---------------:|:------------------------------------:|:--------:|:------------------------------------------------------|
|       GET       |             `/api/cart`              |   USER   | Get user's shopping cart.                             |
|      POST       |             `/api/cart`              |   USER   | Add cart item to user's shopping cart.                |
|       PUT       | `/api/cart//cart-items/{cartItemId}` |   USER   | Update quantity of cart item in user's shopping cart. |
|     DELETE      | `/api/cart/cart-items/{cartItemId}`  |   USER   | Delete cart item in user's shopping cart.             |

---

## **🪪5. Security**

### Overview
The security module in this project relies on JSON Web Tokens (JWT) for user authentication and authorization. JWT is a compact, URL-safe means of representing claims to be transferred between two parties. In this context, it is used to securely manage user sessions and authorize access to protected resources.

### Token Generation
JWT tokens are generated upon successful user authentication. The following steps are involved in token generation:

1. <b>User Authentication:</b>
   - Users must authenticate using their credentials (e.g., username and password).
   - Successful authentication triggers the generation of a JWT token.
2. <b>Token Payload:</b>
   - The token payload contains relevant information about the user, such as user ID, roles, and expiration time.
3. <b>Token Signing:</b>
   - The payload is then signed using a secret key known only to the server.
   - The signature ensures the integrity of the token and prevents tampering.
4. <b>Token Issuance:</b>
   - The signed JWT token is issued to the authenticated user.

## **🏁6. Getting Started**

Here you can test book store API:HERE_WILL_URL

If you want access `ADMIN` role you can use next credentials:
- email: `admin@gmail.com`
- password: `12345678`