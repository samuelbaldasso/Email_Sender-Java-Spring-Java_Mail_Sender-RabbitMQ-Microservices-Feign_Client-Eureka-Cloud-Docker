# Email Sender Microservices Project

## 🚀 Project Overview
This project is a **Java / Spring Boot** microservices-based application for sending emails. It integrates with **RabbitMQ** for message brokering, **Eureka** for service discovery, **Feign Client** for inter-service communication, and is containerized using **Docker**.

---

## 🎯 Features
- **Microservices Architecture**
- **Email Sending Service** using `JavaMailSender`
- **Message Queue** with **RabbitMQ** for asynchronous email processing
- **Service Discovery** with **Eureka Server**
- **Feign Client** for REST communication between services
- **Dockerized** for easy deployment

---

## 🛠️ Tech Stack
- **Java 17+**
- **Spring Boot** (Web, Mail, RabbitMQ, Feign, Cloud Discovery)
- **RabbitMQ**
- **Eureka Server & Client**
- **Feign Client**
- **Docker**

---

## 🔧 Setup & Run

### 1️⃣ Clone the repository
```bash
git clone https://github.com/samuelbaldasso/Email_Sender-Java-Spring-Java_Mail_Sender-RabbitMQ-Microservices-Feign_Client-Eureka-Cloud-Docker.git
cd Email_Sender-Java-Spring-Java_Mail_Sender-RabbitMQ-Microservices-Feign_Client-Eureka-Cloud-Docker
```

### 2️⃣ RabbitMQ Setup
Make sure RabbitMQ is running locally:
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```
Access RabbitMQ dashboard at `http://localhost:15672` (default user/pass: guest/guest)

### 3️⃣ Build the services
```bash
mvn clean install
```

### 4️⃣ Run Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Access Eureka Dashboard at `http://localhost:8761`

### 5️⃣ Run the microservice
```bash
mvn spring-boot:run
```

### 6️⃣ Dockerize the project
Build Docker images:
```bash
docker build -t email-sender-service ./email-sender-service
```

Run containers:
```bash
docker-compose up
```

---

## 🧠 How It Works
1. **Notification Service** receives a request to send an email.
2. **Notification Service** pushes a message to **RabbitMQ**.
3. **Email Sender Service** consumes the message and sends the email using `JavaMailSender`.
4. **Eureka** handles service discovery, allowing services to communicate via **Feign Client**.

---

## 🔥 Future Enhancements
- Implement **Circuit Breaker** (Resilience4j) for fault tolerance
- Add **OAuth2** or **JWT** for security
- Improve error handling & retries for RabbitMQ
- Add Kubernetes deployment manifests

---

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!

1. Fork the repo
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Create a pull request

---

## 📄 License
This project is licensed under the **MIT License** — feel free to modify and distribute.

---

## ⭐ Acknowledgements
- **Spring Boot** — for making microservices easy
- **RabbitMQ** — for reliable message brokering
- **Feign Client** — for simplifying HTTP calls between services
- **Docker** — for containerizing everything

If you like this project, ⭐ **star the repo** to show your support!

Happy coding! 🚀
