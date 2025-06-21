# Reddit Clone: A Microservices-Based Application

This project is a backend application that emulates the core functionalities of Reddit, built upon a distributed microservices architecture. It handles features like user authentication, communities, threads, moderation, and notifications, with each business domain encapsulated within its own service.

## Table of Contents
- [System Architecture](#system-architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Authentication Flow](#authentication-flow)
- [Design Patterns Used](#design-patterns-used)
- [Microservices Communication](#microservices-communication)
- [CI/CD Pipeline](#cicd-pipeline)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Resources](#resources)

## System Architecture

The application is designed using a microservices pattern, where each service is an independent component with its own database and business logic. This promotes scalability, resilience, and maintainability. The primary services are orchestrated using Kubernetes, with an API Gateway serving as the single entry point for all client requests.

### Microservices Overview

| Microservice        | Responsibilities & Functionality                                                                        | Database     |
| :------------------ | :------------------------------------------------------------------------------------------------------ | :----------- |
| **User App** | • User Authentication (Login, Signup)<br>• Email Verification<br>• Block/Unblock other users<br>• User search functionality | **PostgreSQL** |
| **Communities App** | • Add users to a community<br>• Sort communities by date or member count                                      | **MongoDB** |
| **Thread App** | • View trending threads based on upvotes<br>• Search logs by action type<br>• Recommend threads based on upvoted topics | **MongoDB** |
| **Moderator App** | • View reports<br>• Ban/unban users from communities<br>• Remove comments from threads                          | **PostgreSQL** |
| **Notification App**| • Send different types of notifications (Email, Push)<br>• Manage read/unread status<br>• Filter notifications     | **MongoDB** |
| **API Gateway** | • Centralized request routing<br>• Handles authentication and security                                     | N/A          |

## Features

### User & Authentication
-   **Secure Sign-up & Login:** Users can register with email verification and log in.
-   **JWT-Based Security:** Implements a robust authentication flow with short-lived access tokens and refresh tokens.
-   **User Interaction:** Users can search for and block other users.

### Communities & Content
-   **Community Management:** Users can join communities. Communities can be sorted by creation date, top status, or member count.
-   **Thread Interaction:** View trending threads, see recommendations based on upvotes, and view action logs.
-   **Dynamic Action Logging:** Utilizes reflection to dynamically log various user actions within threads.
-   **Performance Caching:** Top threads are cached using Redis to improve load times and reduce database load.
-   **Content Structure:** Communities contain Topics, and Threads contain embedded Comments.

### Moderation
-   **Content & User Moderation:** Moderators can view reports, ban or unban users, and remove comments from threads.

### Notifications
-   **Multi-channel Notifications:** Delivers both email and push notifications.
-   **Notification Management:** Users can manage read/unread status and filter notifications.

## Technology Stack

| Category        | Technology        | Description                                                                                 |
| :-------------- | :---------------- | :------------------------------------------------------------------------------------------ |
| **Databases** | **PostgreSQL** (SQL) | Used for services requiring strong transactional consistency like User and Moderator data.    |
|                 | **MongoDB** (NoSQL)  | Used for flexible, scalable data like Communities, Threads, and Notifications.            |
| **Caching** | **Redis** | In-memory data store used for caching frequently accessed data, like top threads.           |
| **Orchestration**| **Kubernetes** | Manages containerized services, providing load balancing, scaling, and resilience.          |
| **Communication**| **RabbitMQ** | Message broker used for reliable asynchronous communication between microservices.      |
|                 | **OpenFeign** | Declarative REST client for simplified, type-safe synchronous inter-service communication. |
| **CI/CD** | **GitHub Actions** | Automates the build, test, and integration pipeline.                                        |

## Prerequisites

Before you begin, ensure you have the following tools installed on your local machine:
* **Java Development Kit (JDK)** (e.g., version 17 or later)
* **Apache Maven**
* **Docker & Docker Compose**
* **A local Kubernetes cluster** (e.g., Minikube, k3s, or the cluster included with Docker Desktop)
* **`kubectl`** command-line tool configured to interact with your cluster.

## Getting Started

To start the project on your local Kubernetes cluster, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/OMAR-AHMED-SAAD/Reddit-Clone.git](https://github.com/OMAR-AHMED-SAAD/Reddit-Clone.git)
    cd Reddit-Clone
    ```
2.  **(Optional) Adjust Replicas:** The deployment files in the `k8s` directory are configured with a default number of replicas. For local development, you may want to edit the `replicas` field in each `deployment-....yaml` file to `1` to conserve resources.

3.  **Deploy to Kubernetes:** Navigate to the `k8s` directory and apply all the manifest files. This will deploy all the microservices and their backing services to your cluster.
    ```bash
    cd k8s
    kubectl apply -f .
    ```

## Authentication Flow

Authentication is handled centrally at the API Gateway to secure the microservices ecosystem.

1.  **Sign-up:** A new user signs up with their credentials. An email is dispatched for verification to ensure the validity of the user's email address.
2.  **Login:** Upon successful login, the **User Service** generates a JSON Web Token (JWT).
3.  **Token Strategy:** The system employs a secure, modern token strategy:
    * **Short-Lived Access Tokens:** These tokens are sent with each request to the API Gateway to access protected resources. Their short lifespan minimizes the risk of token hijacking.
    * **Refresh Tokens:** A long-lived refresh token is used to obtain a new access token without requiring the user to log in again.
4.  **Gateway Authentication:** The **API Gateway** acts as the central entry point. It intercepts all incoming requests and validates the JWT access token before forwarding the request to the appropriate downstream microservice.

## Design Patterns Used

The project leverages several key software design patterns to ensure clean, efficient, and maintainable code.

| Service Area        | Design Pattern      | Purpose                                                                             |
| :------------------ | :------------------ | :---------------------------------------------------------------------------------- |
| **User App** | **Singleton** | Used for managing the user session.                                                 |
|                     | **Strategy** | Implemented for the search functionality to allow different search algorithms.      |
| **Thread App** | **Factory** | Used for creating different types of logs (e.g., upvote log, view log).             |
|                     | **Builder** | Implemented for constructing complex Thread objects.                                |
|                     | **Reflection** | Used with the Factory pattern to dynamically instantiate different log type classes at runtime. |
| **Communities App** | **Observer** | Used to notify components when community data changes.                              |
|                     | **Builder** | Implemented for constructing complex Community objects.                             |
| **Moderator App** | **Command** | Encapsulates moderation actions (e.g., BanUser, RemoveComment) as objects.        |
| **Notification App**| **Factory** | Used for creating different types of notifications (EmailNotification, PushNotification). |
|                     | **Strategy** | Implemented for different notification sending mechanisms.                            |

## Microservices Communication

Services communicate with each other through a mix of synchronous and asynchronous patterns.

### Synchronous Communication
-   **`Community -> Thread`**: Direct, synchronous REST calls are made using **OpenFeign** when a user within the Community service needs to fetch thread data immediately.

### Asynchronous Communication
Asynchronous communication is managed via **RabbitMQ** to decouple services and enhance resilience.
-   **`Moderator -> Thread`**: When a moderator deletes a thread, an asynchronous event is sent.
-   **`Thread -> Moderator`**: When a user reports a thread, an event is published for the Moderator service to consume.
-   **`Community -> Notification`**: When a significant event occurs in a community (e.g., a new top thread), an event is sent to the Notification service to alert users.

## CI/CD Pipeline

The project uses **GitHub Actions** for a multi-stage CI/CD pipeline, defined in the `.github/workflows` directory. This pipeline automates the process of integrating code changes and preparing them for deployment.

### Stage 1: Continuous Integration (Build & Test)
This workflow triggers on every push to the `main` branch and is responsible for ensuring code quality and stability.
1.  **Checkout:** Checks out the latest source code from the repository.
2.  **Set up JDK:** Configures the appropriate Java Development Kit version.
3.  **Build with Maven:** Compiles the source code, resolves dependencies, and packages each microservice into a `.jar` file using the Maven wrapper (`mvnw`).
4.  **Run Tests:** Executes all unit and integration tests to validate the code and prevent regressions.

### Stage 2: Continuous Delivery (Dockerization)
Following a successful integration stage, this workflow handles the packaging of the services into portable container images.
1.  **Login to Docker Hub:** Securely logs into the Docker container registry.
2.  **Build Docker Image:** For each microservice, it builds a Docker image based on its `Dockerfile`.
3.  **Push to Docker Hub:** Tags the newly created image and pushes it to the Docker repository, making the service artifact available for deployment into the Kubernetes environment.

## Kubernetes Deployment

The `k8s` directory contains all the Kubernetes manifest files required to deploy the entire application stack, including the microservices and their backing services (databases, message broker, etc.).

The deployment is structured using standard Kubernetes resources:

* **Deployments:** Each microservice and stateful service (e.g., PostgreSQL, MongoDB) has a corresponding `Deployment` file. This manages the application's pods, ensuring the desired number of replicas are running and handling rolling updates.
* **Services:** To enable communication between microservices, each deployment is exposed via a `Service`.
    * **ClusterIP:** The default service type used for internal communication between the API Gateway and the backend microservices.
    * **LoadBalancer/NodePort:** A `LoadBalancer` or `NodePort` service is used for the API Gateway to expose the application to external traffic.
* **ConfigMaps & Secrets:** Application configuration, such as environment variables, database URLs, and credentials, is externalized using `ConfigMaps` and `Secrets`. This separates configuration from the container images, allowing for greater flexibility and security.
* **PersistentVolumeClaims (PVCs):** For stateful services like PostgreSQL, MongoDB, and RabbitMQ, `PersistentVolumeClaims` are used to request persistent storage. This ensures that data is not lost if a pod is restarted or rescheduled to a different node.

## Resources
- **Course Content:** Massively Scalable Applications (CSEN1002) @ The German University in Cairo (GUC)
- **Design Patterns:** [Refactoring.Guru - Design Patterns](https://refactoring.guru/design-patterns/factory-method)
- **Kubernetes:** [TechWorld with Nana - Kubernetes Tutorial for Beginners](https://youtu.be/s_o8dwzRlu4?si=lTI1Wa9gYxrpLw-M)
- **Spring Boot:** [Official Spring Boot Documentation](https://docs.spring.io/spring-boot/documentation.html)
- **Authentication:** [Spring Authorization Server Project](https://spring.io/projects/spring-authorization-server)
- **Mail Service:** [SendGrid Documentation](https://sendgrid.com/en-us)
