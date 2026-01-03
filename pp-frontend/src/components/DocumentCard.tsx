/**
 * DocumentCard Component
 * Displays a single document as a card with key information including:
 * - Document title, status, and ID
 * - Creation and update timestamps
 * - Associated tags
 * - Action buttons for viewing details or opening externally
 */
import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  Chip,
  Box,
  IconButton,
  Tooltip,
  CardActions,
} from '@mui/material';
import {
  Launch as LaunchIcon,
  Schedule as ScheduleIcon,
  Update as UpdateIcon,
} from '@mui/icons-material';
import { DocumentApiDto } from '../types/api';
import { formatDate, formatRelativeTime, getStatusColor, truncateText, getTagColor } from '../utils/helpers';

interface DocumentCardProps {
  /** The document data to display */
  document: DocumentApiDto;
  /** Optional callback when the "View Details" button is clicked */
  onViewDetails?: (document: DocumentApiDto) => void;
}

/**
 * DocumentCard component for displaying document information in a grid.
 * @param props - Component props
 * @returns Rendered document card
 */
export const DocumentCard: React.FC<DocumentCardProps> = ({
  document,
  onViewDetails
}) => {
  /** Handler for viewing document details in a modal */
  const handleViewDetails = () => {
    if (onViewDetails) {
      onViewDetails(document);
    }
  };

  /** Handler for opening the document in an external system (e.g., Confluence) */
  const handleOpenExternal = () => {
    // This would typically open the Confluence page
    // For now, we'll just log the external ID
    console.log('Opening external document:', document.externalId);
  };

  // Determine the status color based on document staleness
  const statusColor = getStatusColor(document.status, document.documentLastUpdatedAt);

  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
        '&:hover': {
          transform: 'translateY(-2px)',
          boxShadow: 3,
        }
      }}
    >
      <CardContent sx={{ flexGrow: 1 }}>
        {/* Document title and status badge */}
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
          <Typography variant="h6" component="h3" gutterBottom sx={{ fontSize: '1.1rem', fontWeight: 600 }}>
            {truncateText(document.title, 60)}
          </Typography>
          <Chip
            label={document.status}
            color={statusColor}
            size="small"
            variant="outlined"
          />
        </Box>

        {/* External ID */}
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
          ID: {document.externalId}
        </Typography>

        {/* Timestamps section */}
        <Box sx={{ mb: 2 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
            <ScheduleIcon fontSize="small" color="action" />
            <Typography variant="caption" color="text.secondary">
              Created: {formatRelativeTime(document.documentLastCreatedAt)}
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <UpdateIcon fontSize="small" color="action" />
            <Typography variant="caption" color="text.secondary">
              Updated: {formatRelativeTime(document.documentLastUpdatedAt)}
            </Typography>
          </Box>
        </Box>

        {/* Tags section */}
        {document.tags && document.tags.length > 0 && (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5, mt: 1 }}>
            {document.tags.map((tag, index) => (
              <Chip
                key={index}
                label={tag}
                size="small"
                variant="outlined"
                sx={{
                  fontSize: '0.7rem',
                  height: '24px',
                  borderColor: getTagColor(tag),
                  color: getTagColor(tag),
                }}
              />
            ))}
          </Box>
        )}
      </CardContent>

      {/* Action buttons */}
      <CardActions sx={{ justifyContent: 'space-between', px: 2, pb: 2 }}>
        <Box>
          <Typography variant="caption" color="text.secondary">
            {formatDate(document.documentLastUpdatedAt)}
          </Typography>
        </Box>
        <Box>
          {/* View details button */}
          {onViewDetails && (
            <Tooltip title="View Details">
              <IconButton size="small" onClick={handleViewDetails}>
                <LaunchIcon fontSize="small" />
              </IconButton>
            </Tooltip>
          )}
          {/* Open in external system button */}
          <Tooltip title="Open External">
            <IconButton size="small" onClick={handleOpenExternal}>
              <LaunchIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Box>
      </CardActions>
    </Card>
  );
};
