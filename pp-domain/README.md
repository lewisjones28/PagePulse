# pp-domain

## Overview
`pp-domain` contains the shared JPA entities used across the PagePulse platform. The module centralizes persistence models so features like the orchestrator and future services can reuse a single canonical representation.

## Contents
- `Document` – core Confluence-derived document entity used during audits
- `Auditable` base class – reusable auditing metadata (created/updated timestamps and users)

## Building & Testing
This module is part of the multi-module Maven build. To compile and run its unit tests in isolation:

```bash
cd /Users/.../git/PagePulse
mvn -pl pp-domain test
```

## Usage
Add the module as a dependency in other PagePulse components:

```xml
<dependency>
  <groupId>com.page.pulse</groupId>
  <artifactId>pp-domain</artifactId>
  <version>${page-pulse.version}</version>
</dependency>
```

## Lombok & JPA
Entities rely on Lombok for boilerplate reduction (`@Getter`, `@EqualsAndHashCode`). Ensure your IDE has Lombok support enabled. Spring Data JPA integrations require a proper datasource configuration supplied by the consuming module (e.g., `pp-orchestrator`).

