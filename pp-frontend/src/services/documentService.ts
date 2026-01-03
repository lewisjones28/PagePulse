import axios from 'axios';
import type { AxiosResponse } from 'axios';
import { DocumentApiDto, PagedDocumentApiDto, PaginationParams } from '../types/api';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8089';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for logging
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

// Add response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => {
    console.log('API Response received:', response.status);
    return response;
  },
  (error) => {
    console.error('Response error:', error);
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

export class DocumentService {
  /**
   * Fetch documents with pagination and sorting
   */
  static async getDocuments(params?: PaginationParams): Promise<PagedDocumentApiDto> {
    const searchParams = new URLSearchParams();

    if (params?.page !== undefined) {
      searchParams.append('page', params.page.toString());
    } else {
      searchParams.append('page', '0');
    }

    if (params?.size !== undefined) {
      searchParams.append('size', params.size.toString());
    } else {
      searchParams.append('size', '10');
    }

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
   * Get document by ID
   */
  static async getDocumentById(id: number): Promise<DocumentApiDto> {
    const response: AxiosResponse<DocumentApiDto> = await apiClient.get(
      `/documents/${id}`
    );

    return response.data;
  }

  /**
   * Check API health
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
   * Test API connectivity
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
