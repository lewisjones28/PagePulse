graph TD
A[DocumentScanTask Start] --> B[Create ConfluencePageParams]
B --> C[apiService.collectPages]
C --> D[Stream of Documents]
D --> E[ruleEngine.evaluate]
E --> F{Rule Evaluation}
F -->|Has Violations| G[raiseAlert]
F -->|No Violations| H[Continue to Next Document]
G --> H
H --> I{More Documents?}
I -->|Yes| E
I -->|No| J[Task Complete]

    subgraph "Dependencies"
        K[ConfluenceApiService]
        L[DocumentRuleEngine]
        M[DocumentRule]
    end
    
    C -.-> K
    E -.-> L
    L -.-> M
    
    classDef task fill:#e8f5e8
    classDef service fill:#fff2cc
    classDef decision fill:#ffe6cc
    
    class A,J task
    class C,E,G service
    class F,I decision