/**
 * Custom React hooks for rule data fetching.
 * Uses TanStack Query (React Query) for efficient data fetching, caching, and synchronization.
 */
import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { RuleApiDto, PagedRuleApiDto, PaginationParams } from '../types/api';
import { RuleService } from '../services/ruleService';

/**
 * Query keys for React Query cache management.
 * Used to identify and manage cached data across the application.
 */
export const RULE_QUERY_KEYS = {
  RULES: 'rules',
  RULE: 'rule',
} as const;

/**
 * Hook for fetching paginated rules.
 * Automatically handles loading states, errors, and caching.
 * Data is cached for 30 seconds before being considered stale.
 * @param params - Optional pagination parameters (page, size, sort)
 * @returns React Query result object with data, loading state, and error information
 */
export const useRules = (
  params?: PaginationParams
): UseQueryResult<PagedRuleApiDto, Error> => {
  return useQuery({
    queryKey: [RULE_QUERY_KEYS.RULES, params],
    queryFn: () => RuleService.getRules(params),
    staleTime: 30000, // Consider data fresh for 30 seconds
    refetchOnWindowFocus: false, // Don't refetch when window regains focus
  });
};

/**
 * Hook for fetching a single rule by ID.
 * Only executes the query when a valid ID is provided.
 * Data is cached for 1 minute before being considered stale.
 * @param id - Rule ID to fetch
 * @returns React Query result object with rule data
 */
export const useRule = (
  id: number
): UseQueryResult<RuleApiDto, Error> => {
  return useQuery({
    queryKey: [RULE_QUERY_KEYS.RULE, id],
    queryFn: () => RuleService.getRuleById(id),
    enabled: !!id, // Only run query if ID is truthy
    staleTime: 60000, // Consider data fresh for 1 minute
  });
};
