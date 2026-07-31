# Stock Broker Dashboard

The **Stock Broker Dashboard** is a full-stack web application that allows users to register, log in securely, and subscribe to live stock updates.  
It demonstrates real-time dashboard synchronization between multiple users using **WebSockets**, along with secure **JWT-based authentication** for user sessions.

---

## 🚀 Features

- User Registration and Login with JWT Authentication  
- Role-based Access Control (User/Admin)  
- Subscribe to and Unsubscribe from Stocks  
- Real-Time Stock Updates using WebSocket  
- Multiple Users Can See Live Changes Simultaneously  
- Responsive and Modern UI using Tailwind CSS  
- RESTful APIs for Backend Communication  

---

## 🧩 Tech Stack

**Frontend:** React.js, Tailwind CSS, Axios, WebSocket  
**Backend:** Spring Boot, Spring Security, JWT, WebSocket, Maven  
**Database:** MySQL (or H2 for testing)  
**Tools:** Eclipse, VS Code, Postman, Git, GitHub  

---

## ⚙️ Setup and Run Instructions

### 🔹 Backend (Spring Boot)

1. Navigate to the backend folder:
   ```bash
   cd StockBrokerBackend
2. Configure the database in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/stockbroker
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
3. Run the backend. The backend will start on http://localhost:8080
4. Navigate to the frontend folder:
   ```bash
   cd StockBrokerFrontend
5. Install dependencies and start
6. The frontend will run on http://localhost:5173


## Author
Anshit Pradhan
Full Stack Developer (React.js + Spring Boot)
📧 Email: anshit20029@gmail.com