# Roman Numeral Converter

This project implements a full-stack Roman Numeral Converter using a Spring Boot backend and a React frontend. The solution is containerized with Docker and includes complete observability using metrics, logs, and tracing.

## Project Overview

This application converts decimal numbers into Roman numerals. It features:

- A RESTful API built with Spring Boot.
- A responsive frontend built using React and Adobe's React Spectrum.
- Observability stack covering logs, metrics, and traces.
- Dockerized infrastructure to run the whole solution with a single command.

## Technologies Used and Why

### Backend - Spring Boot
We chose Spring Boot because it significantly simplifies backend development with built-in support for web servers, dependency injection, and observability tools. Here are the major backend components:

- **Spring Boot Web**: Makes it easy to expose REST endpoints.
- **Spring Boot Actuator**: Provides production-ready features like health checks and metrics.
- **Micrometer**: A metrics facade that integrates seamlessly with Spring Boot and supports multiple monitoring systems. We used the Prometheus registry for collecting and exposing metrics.
- **OpenTelemetry SDK with Zipkin Exporter**: Used for tracing requests across the application. We chose OpenTelemetry because it's vendor-neutral and supported by a wide range of backends including Zipkin and Jaeger.
- **SLF4J and Logback**: These libraries handle logging. Logback offers a performant and flexible logging configuration out of the box with Spring Boot.

### Frontend - React + React Spectrum
The frontend is a single-page React app styled using Adobe’s React Spectrum. This combination was picked to quickly build a UI that is both accesible and user-friendly.

- **React**: A declarative JavaScript framework that simplifies state management and DOM rendering.
- **React Spectrum**: Adobe’s component library ensures design consistency and accessibility compliance.
- **Axios**: Used to handle HTTP requests.
- **TypeScript**: Helps catch bugs during development by enforcing static typing.

### Testing
- **JUnit 5**: The default testing framework with strong Spring Boot integration.
- **Mockito**: Used for mocking dependencies like MeterRegistry and OpenTelemetry.
- **Spring Boot Test**: Enables realistic integration testing with auto-configured test servers.

### Observability Stack
The observability requirement is fulfilled through these 3 pillars:

- **Logs**: Managed using SLF4J with Logback. Each request and response is logged along with errors and exceptions.
- **Metrics**: Exposed on `/actuator/prometheus` via Micrometer and scraped by Prometheus. This includes counters, timers, and JVM metrics.
- **Traces**: Each incoming request is traced using OpenTelemetry and visualized through Zipkin. The traces can help debug performance issues and monitor call flows.

### Docker and Docker Compose
I used Docker to containerize both the frontend and backend, along with Prometheus and Zipkin services. Docker Compose ties them together for seamless orchestration.

The benefits of using Docker are:
- Consistent environment between development and production.
- Easy to deploy and test the full stack locally.
- Simplifies onboarding for other developers.

You can bring everything up with one command:
```bash
docker-compose up --build
```

## How to Run
1. Clone this repo.
2. Run `docker-compose up --build`.
3. Access the app at [http://localhost:3000](http://localhost:3000).
4. API server runs on [http://localhost:8080](http://localhost:8080).
5. Prometheus is available at [http://localhost:9090](http://localhost:9090).
6. Zipkin UI is accessible at [http://localhost:9411](http://localhost:9411).

This project shows how I built a full-stack app Java and React using Docker along with proper logging, metrics, and tracing. I chose tools that are simple, well-supported, and easy to extend in the future.


Frontend: http://localhost:3000

Backend API: http://localhost:8080/romannumeral?query=42

Prometheus: http://localhost:9090

Zipkin: http://localhost:9411
