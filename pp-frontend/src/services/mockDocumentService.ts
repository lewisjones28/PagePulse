import { DocumentApiDto, PagedDocumentApiDto } from '../types/api';

// Mock data for development and testing
const mockDocuments: DocumentApiDto[] = [
  {
    id: 1,
    externalId: "CONF-12345",
    title: "API Documentation Guidelines",
    status: "active",
    tags: ["documentation", "api", "guidelines"],
    documentLastCreatedAt: "2023-12-01T10:00:00Z",
    documentLastUpdatedAt: "2023-12-15T14:30:00Z"
  },
  {
    id: 2,
    externalId: "CONF-12346",
    title: "Database Migration Strategy",
    status: "draft",
    tags: ["database", "migration", "strategy"],
    documentLastCreatedAt: "2023-11-15T09:00:00Z",
    documentLastUpdatedAt: "2023-11-20T16:45:00Z"
  },
  {
    id: 3,
    externalId: "CONF-12347",
    title: "Security Best Practices",
    status: "active",
    tags: ["security", "best-practices", "authentication"],
    documentLastCreatedAt: "2023-10-01T08:00:00Z",
    documentLastUpdatedAt: "2024-01-02T11:20:00Z"
  },
  {
    id: 4,
    externalId: "CONF-12348",
    title: "Legacy System Integration",
    status: "inactive",
    tags: ["legacy", "integration", "deprecated"],
    documentLastCreatedAt: "2023-06-01T10:00:00Z",
    documentLastUpdatedAt: "2023-08-15T13:00:00Z"
  },
  {
    id: 5,
    externalId: "CONF-12349",
    title: "Frontend Architecture Overview",
    status: "active",
    tags: ["frontend", "architecture", "react"],
    documentLastCreatedAt: "2023-11-01T09:30:00Z",
    documentLastUpdatedAt: "2023-12-20T15:15:00Z"
  },
  {
    id: 6,
    externalId: "CONF-12350",
    title: "Testing Strategy Document",
    status: "active",
    tags: ["testing", "strategy", "qa"],
    documentLastCreatedAt: "2023-10-15T11:00:00Z",
    documentLastUpdatedAt: "2023-12-10T09:45:00Z"
  }
];

export class MockDocumentService {
  static async getDocuments(params?: any): Promise<PagedDocumentApiDto> {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 500));

    const page = params?.page || 0;
    const size = params?.size || 10;
    const start = page * size;
    const end = start + size;

    // Apply sorting if provided
    let sortedDocuments = [...mockDocuments];
    if (params?.sort && params.sort.length > 0) {
      const [property, direction] = params.sort[0].split(',');
      sortedDocuments.sort((a: any, b: any) => {
        if (direction === 'desc') {
          return b[property] > a[property] ? 1 : -1;
        }
        return a[property] > b[property] ? 1 : -1;
      });
    }

    const content = sortedDocuments.slice(start, end);

    return {
      content,
      pageInfo: {
        page,
        pages: Math.ceil(mockDocuments.length / size),
        elements: mockDocuments.length
      }
    };
  }

  static async getDocumentById(id: number): Promise<DocumentApiDto> {
    await new Promise(resolve => setTimeout(resolve, 300));

    const document = mockDocuments.find(doc => doc.id === id);
    if (!document) {
      throw new Error(`Document with id ${id} not found`);
    }

    return document;
  }

  static async healthCheck(): Promise<boolean> {
    return true; // Mock always returns healthy
  }
}

export default MockDocumentService;
