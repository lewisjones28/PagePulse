/**
 * Utility functions for formatting and displaying document data.
 * These helpers are used throughout the application for consistent data presentation.
 */
import { format, formatDistanceToNow, parseISO } from 'date-fns';

/**
 * Format a date string to a readable format.
 * @param dateString - ISO 8601 date string from the API
 * @returns Formatted date string in 'MMM d, yyyy HH:mm' format (e.g., "Jan 3, 2026 14:30")
 * @example formatDate("2026-01-03T14:30:00Z") // Returns "Jan 3, 2026 14:30"
 */
export const formatDate = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return format(date, 'MMM d, yyyy HH:mm');
  } catch (error) {
    console.error('Error formatting date:', dateString, error);
    return 'Invalid Date';
  }
};

/**
 * Format a date string to a relative time (e.g., "2 hours ago").
 * Uses the date-fns library for human-friendly time differences.
 * @param dateString - ISO 8601 date string from the API
 * @returns Human-readable relative time string
 * @example formatRelativeTime("2026-01-03T12:00:00Z") // Returns "2 hours ago"
 */
export const formatRelativeTime = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return formatDistanceToNow(date, { addSuffix: true });
  } catch (error) {
    console.error('Error formatting relative time:', dateString, error);
    return 'Unknown time';
  }
};

/**
 * Get the status color based on document staleness.
 * Determines the appropriate Material-UI color based on how recently
 * the document was updated and its current status.
 * @param status - Document status (e.g., 'active', 'draft', 'inactive')
 * @param lastUpdated - ISO 8601 date string of last update
 * @returns Material-UI color variant
 */
export const getStatusColor = (status: string, lastUpdated: string): 'success' | 'warning' | 'error' | 'info' => {
  const daysSinceUpdate = getDaysSinceDate(lastUpdated);

  // Color logic based on status and freshness
  switch (status.toLowerCase()) {
    case 'active':
      // Active documents: green if fresh, yellow if stale, red if outdated
      return daysSinceUpdate < 30 ? 'success' : daysSinceUpdate < 90 ? 'warning' : 'error';
    case 'inactive':
      return 'error';
    case 'draft':
      return 'info';
    default:
      return 'info';
  }
};

/**
 * Calculate the number of days since a given date.
 * Used to determine document freshness and staleness.
 * @param dateString - ISO 8601 date string
 * @returns Number of days since the date (rounded up)
 * @example getDaysSinceDate("2025-12-01T00:00:00Z") // Returns number of days since Dec 1, 2025
 */
export const getDaysSinceDate = (dateString: string): number => {
  try {
    const date = parseISO(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  } catch (error) {
    console.error('Error calculating days since date:', dateString, error);
    return 0;
  }
};

/**
 * Truncate text to a specified maximum length.
 * Adds ellipsis (...) if the text exceeds the maximum length.
 * @param text - Text to truncate
 * @param maxLength - Maximum allowed length (default: 100 characters)
 * @returns Truncated text with ellipsis if needed
 * @example truncateText("This is a very long text...", 10) // Returns "This is a..."
 */
export const truncateText = (text: string, maxLength: number = 100): string => {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength).trim() + '...';
};

/**
 * Generate a consistent color for a tag based on its name.
 * Uses a hash function to ensure the same tag always gets the same color.
 * Useful for visual consistency across the application.
 * @param tag - Tag name to generate a color for
 * @returns Hex color code
 * @example getTagColor("documentation") // Returns "#1976d2" (consistent for "documentation")
 */
export const getTagColor = (tag: string): string => {
  // Predefined color palette
  const colors = [
    '#1976d2', '#388e3c', '#f57c00', '#d32f2f', '#7b1fa2',
    '#0288d1', '#689f38', '#f9a825', '#c62828', '#8e24aa'
  ];

  // Generate a hash from the tag string
  let hash = 0;
  for (let i = 0; i < tag.length; i++) {
    hash = tag.charCodeAt(i) + ((hash << 5) - hash);
  }

  // Use modulo to select a color from the palette
  return colors[Math.abs(hash) % colors.length];
};
