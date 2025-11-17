graph LR
subgraph "Interface"
A[ConfluenceApiClient]
end

    subgraph "API Methods"
        B["getPages()"]
        C["getPages(queryMap)"]
        D["getPage(pageId, queryMap)"]
    end
    
    subgraph "Config"
        E[ConfluenceFeignConfig]
    end
    
    subgraph "Confluence API Endpoints"
        F[GET /pages]
        G[GET /pages?params]
        H["GET /pages/{pageId}"]
    end
    
    subgraph "External System"
        I[Confluence Server]
    end
    
    A --> B
    A --> C
    A --> D
    A -.-> E
    
    B --> F
    C --> G
    D --> H
    
    F --> I
    G --> I
    H --> I
    
    I --> J[JsonNode Response]
    
    classDef client fill:#e3f2fd
    classDef methods fill:#f1f8e9
    classDef config fill:#fce4ec
    classDef endpoints fill:#fff8e1
    classDef external fill:#efebe9
    
    class A client
    class B,C,D methods
    class E config
    class F,G,H endpoints
    class I,J external