# PagePulse Confluence Feign Client

A dedicated Feign client module for seamless integration with the Atlassian Confluence REST API v2. This module provides type-safe, declarative HTTP clients for interacting with Confluence pages, spaces, and content.

## 🎯 Purpose

This module serves as the external API integration layer for PagePulse, handling all communication with Confluence instances. It abstracts the complexity of HTTP requests and provides a clean, Java-based interface for Confluence operations.

## 🏗️ Architecture

![Confluence Feign Architecture](./docs/flows/images/page-pulse-confluence-feign-flow.png)

The module follows a clean architecture pattern with clear separation of concerns:

- **Client Interface**: Declarative Feign client definitions
- **Configuration**: Authentication and request/response handling
- **Constants**: Centralized parameter and response field definitions
- **DTOs**: Type-safe data transfer objects (when needed)

## 📦 Key Components

### ConfluenceApiClient
The main Feign client interface providing access to Confluence API endpoints:

```java
@FeignClient(name = "confluenceApi", url = "${confluence.base-url}/wiki/api/v2",
             configuration = ConfluenceFeignConfig.class)
public interface ConfluenceApiClient {
    
    @RequestLine("GET /pages")
    JsonNode getPages();
    
    @RequestLine("GET /pages")
    JsonNode getPages(@QueryMap Map<String, Object> queryMap);
    
    @RequestLine("GET /pages/{pageId}")
    JsonNode getPage(@Param("pageId") String pageId, @QueryMap Map<String, Object> queryMap);
}
```

## ConfluenceFeignConfig

### Overview
The `ConfluenceFeignConfig` class handles authentication and Feign client configuration for communicating with the Confluence API.

### Key Responsibilities

#### 🔐 Basic Authentication
- Automatically injects the `Authorization` header for all Confluence API requests.
- Uses username + API token encoded in Base64.

#### 📘 Contract Configuration
- Uses Feign’s native annotations for maximum flexibility and clear API contracts.

#### 🧩 Request Interceptors
- Adds authentication headers to every request.
- Central place to apply shared request logic.

---

## 🧷 Constants Classes

Centralized definitions for request parameters and response field names:

### `ConfluencePageParamConstants`
- Defines query parameter names and values required for Confluence API calls.

### `ConfluencePageResponseConstants`
- Provides JSON field names used when parsing Confluence API responses.
- Ensures consistent parsing across services.

---

## 🔧 Required Configuration

Add the following properties to your `application.yml`:

```yaml
confluence:
  base-url: ${CONFLUENCE_BASE_URL}
  username: ${CONFLUENCE_USERNAME}     # Your Confluence username/email
  api-token: ${CONFLUENCE_API_TOKEN}   # Your Confluence API token
```

### 🔐 Authentication

This module uses Confluence API Tokens for authentication.

Steps:
1. Generate an API token from your Atlassian account: Atlassian Account Settings → Security → Create API Token
2. Set your Confluence username and API token in environment variables or application.yml.

The Feign client will automatically generate the proper Basic Authentication header for all API calls.