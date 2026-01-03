/**
 * Custom React hooks for document data fetching.
 * Uses TanStack Query (React Query) for efficient data fetching, caching, and synchronization.
 */
import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { DocumentApiDto, PagedDocumentApiDto, PaginationParams } from '../types/api';
import { DocumentService } from '../services/documentService';

/**
 * Query keys for React Query cache management.
 * Used to identify and manage cached data across the application.
 */
export const QUERY_KEYS = {
  DOCUMENTS: 'documents',
  DOCUMENT: 'document',
  HEALTH: 'health',
} as const;

/**
 * Hook for fetching paginated documents.
 * Automatically handles loading states, errors, and caching.
 * Data is cached for 30 seconds before being considered stale.
 * @param params - Optional pagination parameters (page, size, sort)
 * @returns React Query result object with data, loading state, and error information
 */
export const useDocuments = (
  params?: PaginationParams
): UseQueryResult<PagedDocumentApiDto, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.DOCUMENTS, params],
    queryFn: () => DocumentService.getDocuments(params),
    staleTime: 30000, // Consider data fresh for 30 seconds
    refetchOnWindowFocus: false, // Don't refetch when window regains focus
  });
};

/**
 * Hook for fetching a single document by ID.
 * Only executes the query when a valid ID is provided.
 * Data is cached for 1 minute before being considered stale.
 * @param id - Document ID to fetch
 * @returns React Query result object with document data
 */
export const useDocument = (
  id: number
): UseQueryResult<DocumentApiDto, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.DOCUMENT, id],
    queryFn: () => DocumentService.getDocumentById(id),
    enabled: !!id, // Only run query if ID is truthy
    staleTime: 60000, // Consider data fresh for 1 minute
  });
};

/**
 * Hook for checking API health status.
 * Automatically polls the health endpoint every 30 seconds.
 * Data is cached for 10 seconds before being considered stale.
 * @returns React Query result object with boolean health status
 */
export const useHealthCheck = (): UseQueryResult<boolean, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.HEALTH],
    queryFn: () => DocumentService.healthCheck(),
    staleTime: 10000, // Consider data fresh for 10 seconds
    refetchInterval: 30000, // Automatically refetch every 30 seconds
  });
};
