/**
 * Document Service
 * Provides methods for interacting with the PagePulse backend API.
 * Handles all document-related API calls, including fetching, pagination, and health checks.
 */
import axios from 'axios';
import type { AxiosResponse } from 'axios';
import { DocumentApiDto, PagedDocumentApiDto, PaginationParams } from '../types/api';

// API base URL from environment variable or default to localhost
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8089';

// Configure axios client with base settings
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000, // 10 second timeout for all requests
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging and debugging
// Logs all outgoing requests to help with debugging API calls
apiClient.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method?.toUpperCase()} request to ${config.url}`);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for centralized error handling
// Provides user-friendly error messages based on response status
apiClient.interceptors.response.use(
  (response) => {
    console.log('API Response received:', response.status);
    return response;
  },
  (error) => {
    console.error('Response error:', error);
    // Handle specific error cases
    if (error.response?.status === 404) {
      console.error('API endpoint not found - check if pp-syndication-api is running on port 8089');
    } else if (error.response?.status >= 500) {
      console.error('Server error occurred');
    } else if (error.code === 'ECONNREFUSED') {
      console.error('Connection refused - API server is not running');
    }
    return Promise.reject(error);
  }
);

/**
 * Service class for document-related API operations.
 * All methods are static and handle API communication with proper error handling.
 */
export class DocumentService {
  /**
   * Fetch documents with pagination and sorting.
   * Builds query parameters and retrieves a paginated list of documents from the API.
   * @param params - Optional pagination parameters (page, size, sort)
   * @returns Promise resolving to paginated document data
   * @throws Error if the API request fails
   */
  static async getDocuments(params?: PaginationParams): Promise<PagedDocumentApiDto> {
    const searchParams = new URLSearchParams();

    // Set page number (default to 0 if not provided)
    if (params?.page !== undefined) {
      searchParams.append('page', params.page.toString());
    } else {
      searchParams.append('page', '0');
    }

    // Set page size (default to 10 if not provided)
    if (params?.size !== undefined) {
      searchParams.append('size', params.size.toString());
    } else {
      searchParams.append('size', '10');
    }

    // Add sort parameters (can have multiple sort fields)
    if (params?.sort && params.sort.length > 0) {
      params.sort.forEach(sortParam => {
        searchParams.append('sort', sortParam);
      });
    } else {
      // Default sort by last updated descending
      searchParams.append('sort', 'documentLastUpdatedAt,desc');
    }

    console.log('Fetching documents with params:', searchParams.toString());

    const response: AxiosResponse<PagedDocumentApiDto> = await apiClient.get(
      `/documents?${searchParams.toString()}`
    );

    console.log('Documents received:', response.data);
    return response.data;
  }

  /**
   * Get a single document by its ID.
   * @param id - Unique identifier of the document
   * @returns Promise resolving to the document data
   * @throws Error if the document is not found or the API request fails
   */
  static async getDocumentById(id: number): Promise<DocumentApiDto> {
    const response: AxiosResponse<DocumentApiDto> = await apiClient.get(
      `/documents/${id}`
    );

    return response.data;
  }

  /**
   * Check if the API server is healthy and responsive.
   * Uses the Spring Boot Actuator health endpoint.
   * @returns Promise resolving to true if healthy, false otherwise
   */
  static async healthCheck(): Promise<boolean> {
    try {
      const response = await apiClient.get('/actuator/health');
      console.log('Health check successful:', response.status);
      return true;
    } catch (error) {
      console.warn('Health check failed:', error);
      return false;
    }
  }

  /**
   * Test the API connection by making a minimal request.
   * Attempts to fetch a single document to verify connectivity.
   * @returns Promise resolving to an object with success status and optional error message
   */
  static async testConnection(): Promise<{ success: boolean; error?: string }> {
    try {
      await apiClient.get('/documents?page=0&size=1');
      return { success: true };
    } catch (error: unknown) {
      let errorMessage = 'Unknown error occurred';

      if (error && typeof error === 'object' && 'code' in error) {
        const axiosError = error as { code?: string; response?: { status?: number }; message?: string };
        errorMessage = axiosError.code === 'ECONNREFUSED'
          ? 'API server is not running on port 8089'
          : axiosError.response?.status === 404
          ? 'Documents endpoint not found - check API configuration'
          : `API error: ${axiosError.message || 'Unknown error'}`;
      } else if (error instanceof Error) {
        errorMessage = `API error: ${error.message}`;
      }

      return { success: false, error: errorMessage };
    }
  }
}

export default DocumentService;
