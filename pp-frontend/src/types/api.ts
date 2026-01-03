/**
 * TypeScript type definitions for API DTOs (Data Transfer Objects).
 * These interfaces match the OpenAPI specification from the backend API.
 */

/**
 * Represents a single document from the API.
 * Contains all metadata and content information for a document.
 */
export interface DocumentApiDto {
  /** Unique internal identifier for the document */
  id: number;
  /** External identifier (e.g., Confluence page ID) */
  externalId: string;
  /** Document title */
  title: string;
  /** Current status of the document (e.g., 'active', 'draft', 'inactive') */
  status: string;
  /** Array of tags associated with the document */
  tags: string[];
  /** ISO datetime string indicating when the document was originally created */
  documentLastCreatedAt: string;
  /** ISO datetime string indicating when the document was last updated */
  documentLastUpdatedAt: string;
}

/**
 * Pagination information for paged API responses.
 */
export interface PageInfo {
  /** Current page number (0-indexed) */
  page: number;
  /** Total number of pages available */
  pages: number;
  /** Total number of elements across all pages */
  elements: number;
}

/**
 * Paginated response containing documents and pagination metadata.
 */
export interface PagedDocumentApiDto {
  /** Array of documents on the current page */
  content: DocumentApiDto[];
  /** Pagination metadata */
  pageInfo: PageInfo;
}

/**
 * Parameters for pagination and sorting API requests.
 */
export interface PaginationParams {
  /** Page number to retrieve (0-indexed, optional) */
  page?: number;
  /** Number of items per page (optional) */
  size?: number;
  /** Array of sort specifications (e.g., ['title,asc', 'date,desc'], optional) */
  sort?: string[];
}

/**
 * Filtering options for document queries.
 */
export interface DocumentFilters {
  /** Filter by document status (optional) */
  status?: string;
  /** Filter by tags (optional) */
  tags?: string[];
  /** Filter by document title (optional) */
  title?: string;
  /** Filter documents created after this date (ISO string, optional) */
  createdAfter?: string;
  /** Filter documents updated after this date (ISO string, optional) */
  updatedAfter?: string;
}
