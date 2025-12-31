# PagePulse Orchestrator

## Database Connectivity
- relies on MySQL; set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- local profile (`local`) reads from `application-local.yml`
- run MySQL locally (Docker): `docker run --name page-pulse-mysql -e MYSQL_DATABASE=page_pulse -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8`
- enable profile when running: `SPRING_PROFILES_ACTIVE=local mvn spring-boot:run`

## Overview
The **PagePulse Orchestrator** is the core module responsible for coordinating all business logic, auditing workflows, rule evaluation, and documentDto processing. It serves as the main application entry point and analyzes Confluence content for staleness, compliance, and overall documentation health.

It is designed to be modular and extensible, enabling developers to add new rules, API integrations, and custom workflows.

---

## 🎯 Purpose

The orchestrator module provides:

- **Document Auditing** – Analyze Confluence pages for staleness & compliance
- **Rule Engine** – Execute configurable business rules
- **Workflow Coordination** – Manage end-to-end audit flows
- **API Endpoints** – REST APIs for external integrations
- **Scheduled Tasks** – Automated audits on configurable schedules

> **Extending:** New functionality can be added by implementing additional rules, services, mappers, or endpoints.

---

## 📦 Architecture & Components

![Orchestrator Architecture](./docs/flows/images/page-pulse-orchestrator-flow.png)

The module follows a layered architecture:

```

REST Controllers → Services → Rule Engine → Mappers → External Clients

````

### Services Layer

#### `ConfluenceApiService`
Handles fetching and transforming Confluence content.  
- **Responsibilities:** Retrieve pages, filter by space, map raw API JSON to internal `Document` objects.  
- **Extending:** Add new API methods for additional Confluence endpoints (e.g., spaces, attachments) or integrate with other external systems.

---

### Rule Engine

#### `DocumentRuleEngine`

Applies a configurable set of rules to documents.

```java
@Service
public class DocumentRuleEngine {
    public RuleEvaluation evaluateDocument(Document documentDto, List<DocumentRule> rules) { }
}
```

#### `DocumentRule` Interface

All rules implement this interface.

```java
public interface DocumentRule {
    String name();
    RuleResult evaluate(Document documentDto);
}
```

> **Extending:** Implement a new rule by creating a Spring Component that implements `DocumentRule`. Include it in your rule pipeline and ensure proper unit testing.

**Example:** Custom rule to flag documents missing required labels:

```java
@Component
public class MissingLabelRule implements DocumentRule {
    public String name() { return "missing-label-check"; }

    public RuleResult evaluate(Document documentDto) {
        boolean hasLabel = documentDto.getLabels().contains("required-label");
        return hasLabel ? RuleResult.passed() : RuleResult.violation("Missing required label");
    }
}
```

---

### Mappers

#### `ConfluenceDocumentMapper`

Maps raw Confluence JSON to internal `Document` objects.

```java
@Component
public class ConfluenceDocumentMapper implements BaseDocumentMapper {
    public Document mapToDocument(JsonNode confluencePageJson) { }
}
```

> **Extending:** Update mapper when adding new fields, metadata, or integrating with additional APIs.

---

### Scheduled Tasks

#### `DocumentScanTask`

Runs automated audits on a schedule.

```java
@Component
public class DocumentScanTask {
    @Scheduled(fixedRateString = "${documentDto.scan.interval:3600000}")
    public void scanDocuments() { }
}
```

> **Extending:** Add new scheduled jobs by creating Spring Components with `@Scheduled` or custom task executors.

---

## 🔧 Configuration

### `application.yml`

```yaml
confluence:
  base-url: ${CONFLUENCE_BASE_URL}
  username: ${CONFLUENCE_USERNAME}
  api-token: ${CONFLUENCE_API_TOKEN}

rules:
  staleness-check:
    threshold: 10
```

> **Extending:** Add new configuration options for custom rules, integrations, or tasks.

---

## 🌍 Environment Variables

```bash
export CONFLUENCE_BASE_URL=https://your-domain.atlassian.net
export CONFLUENCE_USERNAME=your-email@company.com
export CONFLUENCE_API_TOKEN=your-api-token
```

---

## 🚀 Running the Application

### Development

```bash
mvn spring-boot:run
```

Or via IDE:

* Main class: `PagePulseServiceApplication`
* VM Option: `-Dspring.profiles.active=local`

### Docker

## Running the Application

To run the `pp-orchestrator` service locally using Docker, follow these steps:

1. Build the Docker image:
   ```bash
   docker-compose build
   ```

2. Run the service:
   ```bash
   docker-compose up
   ```

3. Access the application at `http://localhost:8080`.

### Environment Variables

The following environment variables need to be set in the `docker-compose.yml` file:

- `CONFLUENCE_API_TOKEN`: Your Confluence API token.
- `CONFLUENCE_BASE_URL`: The base URL of your Confluence instance.
- `CONFLUENCE_USERNAME`: Your Confluence username.

Replace the placeholders in the `docker-compose.yml` file with your actual values before running the application.

---

## 🧪 Testing

* **Unit tests:** `mvn test`
* **Integration tests:** `mvn test -Dtest=**/*IntegrationTest`

> **Extending:** Add tests when creating new rules, services, mappers, or API endpoints. Use mocks for external clients (Feign) and WireMock for integration testing.

---

## 🔍 Business Rules

* **Built-in:** Staleness check, label validation, owner verification
* **Custom:** Implement `DocumentRule` and register as Spring Component
* **Pipeline:** Rules are evaluated sequentially by `DocumentRuleEngine`

> **Extending:** New rules automatically participate in the orchestration once registered. Use configuration to enable/disable them dynamically.

---

## 📊 Monitoring & Observability

* Spring Boot Actuator endpoints: `/health`, `/metrics`, `/audit`
* Rule execution metrics
* Document processing stats
* External API call metrics

> **Extending:** Add custom health indicators, metrics, or log aggregations for new integrations.

---

## 🤝 Contributing & Extending

1. Follow Checkstyle and code conventions
2. Ensure >80% test coverage
3. Document public APIs and rules
4. Add new rules, endpoints, or scheduled tasks via Spring Components
5. Keep configuration externalized and environment-variable friendly
6. Maintain consistent logging and error handling

---

## 📋 Dependencies

* Spring Boot
* Spring Cloud OpenFeign
* MapStruct
* Jackson
* Actuator
* JUnit 5, Mockito, WireMock
* Maven, Checkstyle, JaCoCo

---

## 🔗 Related Documentation

* [PagePulse Main README](../README.md)
* [Confluence Feign Client Docs](../pp-confluence-feign/README.md)
* [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [MapStruct Documentation](https://mapstruct.org/documentation/stable/reference/html/)
* [Confluence REST API v2](https://developer.atlassian.com/cloud/confluence/rest/v2/intro/)
