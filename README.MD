# **📚Online Book Store Project**

Welcome to the Online Book Store project! This project is a Java-based web application built using Spring Boot,
Hibernate, Liquibase, and other technologies to create a robust online bookstore.

## **📝1. Introduction**

This project is an online bookstore that provides a platform for users to browse and purchase books. 
It is built using Java with a Spring Boot infrastructure, using technologies such as Hibernate for 
database interaction, Liquibase for database versioning, Swagger for API documentation, and JWT for 
secure user authentication. There can also be 2 types of users, USER and ADMIN. Accordingly, the 
USER can create an order, place a shopping cart, view existing resources, and his history. At that 
time, ADIMIN can do everything that USER can, as well as change USER's order status, update and 
delete resources.
## **💻2. Technologies Used**

* Java 17
* Spring v3.1.4 (Boot, Web, JPA, Security)
* Hibernate
* Liquibase v4.20.0
* Docker 3.8
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

## **📹6. Video demo**
Link for demonstration of API: https://www.loom.com/share/ce182b95f1df41c9b405e121f4ce977b

## **🏁7. Getting Started**
If you want locally run this project follow these steps:
- Clone the repository from GitHub: `git clone https://github.com/teract10s/bookStore.git`
- Create a .env file and fill it with the necessary variables (an example of all the necessary variables in the .env.sample file)
- Run `mvn clean package` command
- Install and setup Docker: https://www.docker.com/products/docker-desktop/
- Run `docker-compose build && docker-compose up` command to build and start the Docker containers
- The project will be launched at the following link: http://localhost:8081
- Also you can use swagger for testing at the following link: http://localhost:8081/api/swagger-ui/index.html
---
Here you can test book store API without any additional steps: http://ec2-3-88-176-243.compute-1.amazonaws.com/api/swagger-ui/index.html#/

If you want access `ADMIN` role you can use next credentials:
- email: `admin@gmail.com`
- password: `12345678`