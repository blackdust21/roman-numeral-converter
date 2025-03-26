# Roman Numeral Converter

This is a full-stack Roman Numeral Converter I built using a Spring Boot backend and a React frontend. Everything runs in Docker containers and includes logging, metrics, and tracing for observability.

## Project Overview

The app converts numbers (like 42) into Roman numerals (like XLII). It includes:

- A backend API built with Spring Boot
- A frontend built using React and Adobe’s React Spectrum
- Observability tools: logs, metrics, and traces
- A Docker setup that runs the entire app and its dependencies with one command

## Technologies Used and Why

### Backend – Spring Boot

I chose Spring Boot because it makes backend development super simple with built-in support for REST APIs, dependency injection, and monitoring. Here’s what I used:

- **Spring Boot Web** – For building REST endpoints
- **Spring Boot Actuator** – Adds endpoints for monitoring and health
- **Micrometer** – Helps collect metrics and integrates easily with Prometheus
- **OpenTelemetry with Zipkin** – For tracing incoming requests and visualizing them in Zipkin
- **SLF4J with Logback** – For logging, which Spring Boot supports by default

### Frontend – React + React Spectrum

I used React for the frontend because it's fast and flexible, and React Spectrum helped me quickly build an accessible and consistent UI.

- **React** – For building a reactive, component-based UI
- **React Spectrum** – For design consistency and accessibility
- **Axios** – For making HTTP requests to the backend
- **TypeScript** – For better developer experience and catching errors early

### Testing

- **JUnit 5** – Works well with Spring Boot and supports both unit and integration tests
- **Mockito** – For mocking out dependencies like metrics and tracing
- **Spring Boot Test** – Makes it easy to write realistic integration tests with a running app context

### Observability

I made sure the app has proper observability by setting up:

- **Logging** – Logs each request, success, and error using SLF4J and Logback
- **Metrics** – Exposed at `/actuator/prometheus` and scraped by Prometheus
- **Traces** – Collected with OpenTelemetry and sent to Zipkin so I can see how requests flow through the system

### Docker and Docker Compose

I containerized everything using Docker and connected the services with Docker Compose:

- Consistent setup between dev and prod
- Quick startup for all components
- Easy for others to try out the project

To launch everything:
``` clone repo and build backend jar file uwing maven command and bash
docker-compose up --build
```

## How to Run

1. Clone this repo and build the backend jar file first using the maven command.
2. Run `docker-compose up --build`
3. Go to [http://localhost:3000](http://localhost:3000) for the UI
4. Call the API at [http://localhost:8080/romannumeral?query=42](http://localhost:8080/romannumeral?query=42)
5. Prometheus is at [http://localhost:9090](http://localhost:9090)
6. Zipkin is at [http://localhost:9411](http://localhost:9411)

## Final Thoughts

This was a production-ready way to  build a full-stack app with proper tracing, monitoring and observability. I picked tools that are easy to use and well-supported. 
