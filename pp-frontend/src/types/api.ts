// TypeScript interfaces generated from OpenAPI specification

export interface DocumentApiDto {
  id: number;
  externalId: string;
  title: string;
  status: string;
  tags: string[];
  documentLastCreatedAt: string; // ISO datetime string
  documentLastUpdatedAt: string; // ISO datetime string
}

export interface PageInfo {
  page: number;
  pages: number;
  elements: number;
}

export interface PagedDocumentApiDto {
  content: DocumentApiDto[];
  pageInfo: PageInfo;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string[];
}

export interface DocumentFilters {
  status?: string;
  tags?: string[];
  title?: string;
  createdAfter?: string;
  updatedAfter?: string;
}
