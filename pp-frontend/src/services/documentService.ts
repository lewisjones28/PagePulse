import axios from 'axios';
import type { AxiosResponse } from 'axios';
import { DocumentApiDto, PagedDocumentApiDto, PaginationParams } from '../types/api';
import { MockDocumentService } from './mockDocumentService';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8089';
const USE_MOCK_DATA = import.meta.env.VITE_ENABLE_MOCK_DATA === 'true';

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
    return response;
  },
  (error) => {
    console.error('Response error:', error);
    if (error.response?.status === 404) {
      console.error('API endpoint not found');
    } else if (error.response?.status >= 500) {
      console.error('Server error occurred');
    }
    return Promise.reject(error);
  }
);

export class DocumentService {
  /**
   * Fetch documents with pagination and sorting
   */
  static async getDocuments(params?: PaginationParams): Promise<PagedDocumentApiDto> {
    if (USE_MOCK_DATA) {
      console.log('Using mock data for development');
      return MockDocumentService.getDocuments(params);
    }

    try {
      const searchParams = new URLSearchParams();

      if (params?.page !== undefined) {
        searchParams.append('page', params.page.toString());
      }

      if (params?.size !== undefined) {
        searchParams.append('size', params.size.toString());
      }

      if (params?.sort && params.sort.length > 0) {
        params.sort.forEach(sortParam => {
          searchParams.append('sort', sortParam);
        });
      }

      const response: AxiosResponse<PagedDocumentApiDto> = await apiClient.get(
        `/documents?${searchParams.toString()}`
      );

      return response.data;
    } catch (error) {
      console.warn('API call failed, falling back to mock data:', error);
      return MockDocumentService.getDocuments(params);
    }
  }

  /**
   * Get document by ID
   */
  static async getDocumentById(id: number): Promise<DocumentApiDto> {
    if (USE_MOCK_DATA) {
      return MockDocumentService.getDocumentById(id);
    }

    try {
      const response: AxiosResponse<DocumentApiDto> = await apiClient.get(
        `/documents/${id}`
      );

      return response.data;
    } catch (error) {
      console.warn('API call failed, falling back to mock data:', error);
      return MockDocumentService.getDocumentById(id);
    }
  }

  /**
   * Check API health
   */
  static async healthCheck(): Promise<boolean> {
    if (USE_MOCK_DATA) {
      return MockDocumentService.healthCheck();
    }

    try {
      await apiClient.get('/actuator/health');
      return true;
    } catch (error) {
      console.warn('Health check failed:', error);
      return false;
    }
  }
}

export default DocumentService;
