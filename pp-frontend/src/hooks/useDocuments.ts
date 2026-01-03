import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { DocumentApiDto, PagedDocumentApiDto, PaginationParams } from '../types/api';
import { DocumentService } from '../services/documentService';

export const QUERY_KEYS = {
  DOCUMENTS: 'documents',
  DOCUMENT: 'document',
  HEALTH: 'health',
} as const;

/**
 * Hook for fetching paginated documents
 */
export const useDocuments = (
  params?: PaginationParams
): UseQueryResult<PagedDocumentApiDto, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.DOCUMENTS, params],
    queryFn: () => DocumentService.getDocuments(params),
    staleTime: 30000, // 30 seconds
    refetchOnWindowFocus: false,
  });
};

/**
 * Hook for fetching a single document by ID
 */
export const useDocument = (
  id: number
): UseQueryResult<DocumentApiDto, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.DOCUMENT, id],
    queryFn: () => DocumentService.getDocumentById(id),
    enabled: !!id,
    staleTime: 60000, // 1 minute
  });
};

/**
 * Hook for checking API health
 */
export const useHealthCheck = (): UseQueryResult<boolean, Error> => {
  return useQuery({
    queryKey: [QUERY_KEYS.HEALTH],
    queryFn: () => DocumentService.healthCheck(),
    staleTime: 10000, // 10 seconds
    refetchInterval: 30000, // Refetch every 30 seconds
  });
};
