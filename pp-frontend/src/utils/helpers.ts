import { format, formatDistanceToNow, parseISO } from 'date-fns';

/**
 * Format a date string to a readable format
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
 * Format a date string to a relative time (e.g., "2 hours ago")
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
 * Get the status color based on document staleness
 */
export const getStatusColor = (status: string, lastUpdated: string): 'success' | 'warning' | 'error' | 'info' => {
  const daysSinceUpdate = getDaysSinceDate(lastUpdated);

  switch (status.toLowerCase()) {
    case 'active':
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
 * Calculate days since a given date
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
 * Truncate text to specified length
 */
export const truncateText = (text: string, maxLength: number = 100): string => {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength).trim() + '...';
};

/**
 * Generate a unique color for tags
 */
export const getTagColor = (tag: string): string => {
  const colors = [
    '#1976d2', '#388e3c', '#f57c00', '#d32f2f', '#7b1fa2',
    '#0288d1', '#689f38', '#f9a825', '#c62828', '#8e24aa'
  ];

  let hash = 0;
  for (let i = 0; i < tag.length; i++) {
    hash = tag.charCodeAt(i) + ((hash << 5) - hash);
  }

  return colors[Math.abs(hash) % colors.length];
};
