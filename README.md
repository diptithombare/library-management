Library Management System - Design Documentation
1. Project Overview :
This Library Management System is a web-based application where two types of users, Librarians and Members, can perform various library-related tasks. The system handles book borrowing, returning, user authentication, and user management using Spring Boot for the backend and MySQL  as the database. The frontend uses simple HTML, CSS, and JavaScript.

The project is structured with a JWT-based authentication mechanism for securing API endpoints and follows RESTful principles for designing the API.
2. Design Approach :
2.1 Architecture
- The application is based on a three-tier architecture:
•	Presentation Layer: Simple HTML pages with JavaScript (hosted via GitHub Pages or any static web hosting service).
•	Business Logic Layer: Backend API developed using Spring Boot.
•	Data Layer: MySQL database to store user information, book details, and borrowing history.

2.2 Backend Technology Stack
•	Spring Boot: Chosen for its ease of use, minimal configuration, and built-in support for creating RESTful services.
•	Spring Security: Provides authentication and authorization capabilities. JWT (JSON Web Token) is used for stateless authentication.
•	Spring Data JPA: Provides an abstraction over the database interaction and uses Hibernate as the ORM framework.
•	MySQL: Used as the relational database management system for data persistence.

 3. Key Design Choices:

3.1 Security & Authentication:
- JWT-based Authentication: We use JWT (JSON Web Tokens) for securing the application. When a user logs in, the system generates a token that is then used to authenticate requests to secured endpoints.
  - Why JWT?: JWT is stateless, making it easier to scale the system horizontally without managing sessions on the server. The token itself contains encoded information about the user, reducing the need for extra database lookups.
  - SecurityConfig.java: Configures security settings such as login endpoints and JWT token validation filter.

3.2 Role-based Access Control (RBAC):
- Two roles are used in the system: LIBRARIAN and MEMBER.
  - Librarian Role: Can add, update, and remove books, view all members, manage members, and view all book transactions (borrowing and returning history).
  - Member Role: Can view available books, borrow/return books, and delete their own accounts.
  - Role-based authorization is handled using Spring Security, where each role is given access to specific REST API endpoints.

 3.3 Separation of Concerns
- The project structure follows a clear separation of concerns:
	Controllers: Handle HTTP requests and responses, interacting with services.
	Services: Contain the business logic.
	Repositories: Handle database access.
	Models: Represent the data entities (Book, User, BorrowHistory).
  This separation ensures that the code is modular and easily testable.


4. Code Structure

 library-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── library/
│   │   │           ├── LibraryManagementApplication.java  # Main Application class
│   │   │           ├── config/
│   │   │           │   ├── JwtAuthenticationFilter.java  # JWT Token Filter
│   │   │           │   ├── JwtUtil.java  # Utility class for JWT
│   │   │           │   └── SecurityConfig.java  # Security Configurations
│   │   │           ├── controllers/
│   │   │           │   ├── AuthController.java  # Authentication endpoints
│   │   │           │   ├── BookController.java  # Book management endpoints
│   │   │           │   ├── BorrowHistoryController.java  # Borrow history endpoints
│   │   │           │   └── MemberController.java  # Member management endpoints
│   │   │           ├── models/
│   │   │           │   ├── Book.java  # Book entity
│   │   │           │   ├── BorrowHistory.java  # BorrowHistory entity
│   │   │           │   └── User.java  # User entity
│   │   │           ├── repositories/
│   │   │           │   ├── BookRepository.java  # Repository for Book
│   │   │           │   ├── BorrowHistoryRepository.java  # Repository for BorrowHistory
│   │   │           │   └── UserRepository.java  # Repository for User
│   │   │           ├── services/
│   │   │           │   ├── BookService.java  # Service for Book operations
│   │   │           │   ├── BorrowHistoryService.java  # Service for Borrow history operations
│   │   │           │   └── UserService.java  # Service for User operations
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   │   └── style.css  # CSS styles
│       │   ├── js/
│       │   │   ├── index.html  # HTML for main page
│       │   │   ├── librarian.html  # HTML for librarian interface
│       │   │   └── member.html  # HTML for member interface
│       └── templates/
│           └── application.properties  # Properties file for configuration
├── src/test/java/ # Test cases
├── pom.xml  # Maven dependencies and build

5. Frontend Approach
The frontend is a simple, lightweight static site built with HTML, CSS, and JavaScript (no frameworks). It uses AJAX calls to interact with the Spring Boot API.

- index.html: Contains forms for login, viewing available books, and managing users. Different views are rendered based on the user’s role (Librarian or Member).
- app.js: Contains the JavaScript logic for making API requests, handling JWT tokens, and updating the UI dynamically based on the responses.

---

6. Database Design
 6.1 Tables
- User Table: Stores user details including roles (Librarian or Member).
- Book Table: Stores information about the books, including availability status.
- BorrowHistory Table: Stores information about when books were borrowed and returned.

6.2 Relationships
- User-Book Relationship: Users can borrow books, which is tracked in the `BorrowHistory` table.
- Book Status: Each book has a status field indicating whether it's AVAILABLE or BORROWED.

 7. Challenges and Considerations
 7.1 JWT Expiration and Refresh
- JWT tokens are configured with an expiration time to enhance security. Once expired, users must log in again to obtain a new token.
 7.2 Database Performance
- The database has been normalized to avoid redundant data. Indexing was used in key columns like `user_id` and `book_id` to speed up queries.

7.3 Error Handling
- The API uses **exception handlers** to provide informative error messages to the frontend, such as invalid credentials or unauthorized access.

8.Future Enhancements:
- Book Reservation Feature: Members could reserve books that are currently borrowed.
- Notifications: Email notifications for overdue books.
- Fine System: Implementing a fine system for late returns.

9. Conclusion:
This system provides a robust foundation for a library management application, balancing ease of use with security and modularity. Further improvements can be made by adding more features such as advanced search and fine calculation.

How to Run the Project
1. Clone the Repository: 
   ```bash
   git clone https://github.com/your-repository-url.git
   ```
   
2. Build and Run Backend:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Open Frontend:
   - Navigate to `src/main/resources/static/index.html` and open it in your browser.

4. Access API:
   - API runs at `http://localhost:8080`.

