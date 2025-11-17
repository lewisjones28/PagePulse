graph TB
subgraph "PagePulse System"
subgraph "pp-orchestrator"
A[DocumentScanTask] --> B[ConfluenceApiService]
B --> C[DocumentRuleEngine]
C --> D[RuleEvaluation Results]
D --> E[Alert System]
end

        subgraph "pp-confluence-feign"
            F[ConfluenceApiClient] --> G[Confluence API v2<br/>/wiki/api/v2]
        end
        
        subgraph "External Systems"
            H[Confluence Server]
            I[Alert Recipients<br/>Teams/Users]
        end
    end
    
    B --> F
    F --> H
    E --> I
    
    classDef orchestrator fill:#e1f5fe
    classDef feign fill:#f3e5f5
    classDef external fill:#fff3e0
    
    class A,B,C,D,E orchestrator
    class F,G feign
    class H,I external