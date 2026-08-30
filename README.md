# FeatureFlag Project

A Spring Boot + Kotlin project that exposes a small feature-flag management API with an in-memory repository.

## Overview

This project demonstrates a simple Domain-Driven design structure for feature flags:

- Domain: feature flag entity and repository contract
- Infrastructure: in-memory implementation using `ConcurrentHashMap`
- Service: business logic and validation
- Web: REST endpoints and DTOs
- Application: Spring Boot bootstrap

The application keeps the state in memory only, which makes it useful for local development, demos, and learning. It is not meant for production persistence.

## Core technologies

### Kotlin
Kotlin is the language used throughout the project. It is concise, expressive, and integrates very well with Spring Boot and Java interoperability.

Why it is used here:
- modern syntax for data classes and value objects
- null-safe type system
- excellent Spring Boot interoperability
- easy to define DTOs and domain models

Where it is used:
- `src/main/kotlin/com/featureflag/domain/*`
- `src/main/kotlin/com/featureflag/service/*`
- `src/main/kotlin/com/featureflag/web/*`

### Spring Boot
Spring Boot provides the application runtime, dependency injection, and REST API support.

Why it is used here:
- quick startup for web services
- auto-configuration for MVC and server setup
- clean dependency injection for repositories and services
- easy endpoint exposure with annotations

Where it is used:
- `src/main/kotlin/com/featureflag/FeatureFlagApplication.kt`
- `src/main/kotlin/com/featureflag/web/FeatureFlagController.kt`

### Spring Web MVC
Spring MVC powers the HTTP layer, allowing endpoints such as `GET /flags`, `POST /flags`, `PUT /flags/{name}`, and the patch operations `PATCH /flags/{name}/enable` and `PATCH /flags/{name}/disable`.

Why it is used here:
- standard REST API handling
- request mapping annotations
- JSON serialization/deserialization

Where it is used:
- controllers and DTO classes under `src/main/kotlin/com/featureflag/web`

### In-memory repository
The repository is implemented with `ConcurrentHashMap`, which gives thread-safe in-memory storage for local use.

Why it is used here:
- simple state storage without external dependencies
- fast local testing and demos
- demonstrates repository abstraction clearly

Where it is used:
- `src/main/kotlin/com/featureflag/infrastructure/InMemoryFeatureFlagRepository.kt`

### JUnit 5
JUnit 5 is used for automated testing of the service layer and application behavior.

Why it is used here:
- common standard for Kotlin/Java testing
- easy to write small business-logic validations
- supports clean assertions and test organization

Where it is used:
- `src/test/kotlin/com/featureflag/*`

## Main flows

### 1. Create a flag
The service validates the name and checks whether the feature flag already exists before saving it.

Flow:
- controller receives request
- service normalizes and validates the name
- repository saves the object into memory
- response returns created flag data

Example:
```bash
curl -X POST http://localhost:8080/flags \
  -H "Content-Type: application/json" \
  -d '{
    "name": "new-dashboard",
    "enabled": true,
    "description": "Enable the new dashboard experience"
  }'
```

### 2. List all flags
The repository returns all saved feature flags, sorted by name.

Example:
```bash
curl http://localhost:8080/flags
```

### 3. Get a flag by name
The service looks up the flag by normalized name and throws an exception if it does not exist.

Example:
```bash
curl http://localhost:8080/flags/new-dashboard
```

### 4. Update a flag
The service loads the existing flag, applies the new values, and saves the updated object.

Example:
```bash
curl -X PUT http://localhost:8080/flags/new-dashboard \
  -H "Content-Type: application/json" \
  -d '{
    "enabled": false,
    "description": "Disabled for now"
  }'
```

### 5. Enable a flag
This convenience endpoint flips the flag state to `true` without needing a full update payload.

Example:
```bash
curl -X PATCH http://localhost:8080/flags/new-dashboard/enable
```

### 6. Disable a flag
This convenience endpoint flips the flag state to `false` without needing a full update payload.

Example:
```bash
curl -X PATCH http://localhost:8080/flags/new-dashboard/disable
```

### 7. Delete a flag
The repository removes the feature flag by name.

Example:
```bash
curl -X DELETE http://localhost:8080/flags/new-dashboard
```

## Project structure

```text
featureflag/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── README.md
├── src/
│   ├── main/
│   │   └── kotlin/
│   │       └── com/featureflag/
│   │           ├── FeatureFlagApplication.kt
│   │           ├── domain/
│   │           │   ├── FeatureFlag.kt
│   │           │   └── FeatureFlagRepository.kt
│   │           ├── infrastructure/
│   │           │   └── InMemoryFeatureFlagRepository.kt
│   │           ├── service/
│   │           │   └── FeatureFlagService.kt
│   │           └── web/
│   │               ├── FeatureFlagController.kt
│   │               ├── dto/
│   │               │   ├── CreateFlagRequest.kt
│   │               │   ├── ModifyFlagRequest.kt
│   │               │   └── FlagResponse.kt
│   └── test/
│       └── kotlin/
│           └── com/featureflag/
│               ├── FeatureFlagServiceTest.kt
│               └── FeatureFlagControllerIntegrationTest.kt
```

## How to run

From the project root:

```bash
./gradlew bootRun
```

Then access the app on:

```text
http://localhost:8080
```

## Useful commands

Compile the project:
```bash
./gradlew compileKotlin
```

Run tests:
```bash
./gradlew test
```

Run the app:
```bash
./gradlew bootRun
```

## Any assumptions you made
- Used flag name as ID
- Applied ConcurrentHashMap for future threads

## Tradeoffs you intentionally chose
- In Memory Database to solve persistence quickly

## If you had another day, what would you improve?
 - repository persistence
 - validation + error handling
 - OpenTelemetry + structured logging
 - more tests 
 - Swagger/OpenAPI
 - Threading 

## Notes
- Feature flag names are normalized by trimming whitespace.
- Duplicate feature names are rejected to maintain uniqueness.
