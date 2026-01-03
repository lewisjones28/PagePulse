/**
 * DocumentDetailModal Component
 * A modal dialog that displays comprehensive details about a selected document.
 * Shows document metadata, timeline, tags, and health analysis.
 * Provides warnings for stale or outdated documents.
 */
import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box,
  Chip,
  Divider,
  IconButton,
  Grid,
  Card,
  CardContent,
} from '@mui/material';
import {
  Close as CloseIcon,
  Launch as LaunchIcon,
  Schedule as ScheduleIcon,
  Tag as TagIcon,
  Info as InfoIcon,
} from '@mui/icons-material';
import { DocumentApiDto } from '../types/api';
import { formatDate, formatRelativeTime, getStatusColor, getDaysSinceDate, getTagColor } from '../utils/helpers';

interface DocumentDetailModalProps {
  /** The document to display (null if no document selected) */
  document: DocumentApiDto | null;
  /** Whether the modal is open */
  open: boolean;
  /** Callback to close the modal */
  onClose: () => void;
}

/**
 * DocumentDetailModal component for displaying full document information.
 * @param props - Component props
 * @returns Rendered modal dialog
 */
export const DocumentDetailModal: React.FC<DocumentDetailModalProps> = ({
  document,
  open,
  onClose,
}) => {
  // Return null if no document is provided
  if (!document) return null;

  // Calculate document metrics
  const statusColor = getStatusColor(document.status, document.documentLastUpdatedAt);
  const daysSinceUpdate = getDaysSinceDate(document.documentLastUpdatedAt);
  const daysSinceCreation = getDaysSinceDate(document.documentLastCreatedAt);

  /**
   * Determines the freshness indicator based on days since last update.
   * @returns Object with label and color for the freshness indicator
   */
  const getFreshnessIndicator = () => {
    if (daysSinceUpdate < 30) return { label: 'Fresh', color: 'success' as const };
    if (daysSinceUpdate < 90) return { label: 'Stale', color: 'warning' as const };
    return { label: 'Outdated', color: 'error' as const };
  };

  const freshnessIndicator = getFreshnessIndicator();

  /**
   * Handler for opening the document in an external system (e.g., Confluence).
   */
  const handleOpenExternal = () => {
    // This would typically open the Confluence page
    console.log('Opening external document:', document.externalId);
    // window.open(`https://confluence.example.com/pages/viewpage.action?pageId=${document.externalId}`, '_blank');
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      {/* Modal header with close button */}
      <DialogTitle sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Typography variant="h6" component="span">
          Document Details
        </Typography>
        <IconButton onClick={onClose} size="small">
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <DialogContent>
        {/* Document title and status badges */}
        <Box sx={{ mb: 3 }}>
          <Typography variant="h5" gutterBottom sx={{ fontWeight: 600 }}>
            {document.title}
          </Typography>

          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 2 }}>
            <Chip
              label={document.status}
              color={statusColor}
              variant="filled"
            />
            <Chip
              label={freshnessIndicator.label}
              color={freshnessIndicator.color}
              variant="outlined"
            />
          </Box>

          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            External ID: {document.externalId}
          </Typography>
        </Box>

        <Divider sx={{ my: 3 }} />

        <Grid container spacing={3}>
          {/* Timeline card showing creation and update dates */}
          <Grid item xs={12} md={6}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <ScheduleIcon color="primary" />
                  Timeline
                </Typography>

                <Box sx={{ mb: 2 }}>
                  <Typography variant="subtitle2" color="text.secondary">
                    Created
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(document.documentLastCreatedAt)}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {formatRelativeTime(document.documentLastCreatedAt)} ({daysSinceCreation} days ago)
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Last Updated
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(document.documentLastUpdatedAt)}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {formatRelativeTime(document.documentLastUpdatedAt)} ({daysSinceUpdate} days ago)
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>

          {/* Tags and metadata card */}
          <Grid item xs={12} md={6}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <TagIcon color="primary" />
                  Tags & Metadata
                </Typography>

                {document.tags && document.tags.length > 0 ? (
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                    {document.tags.map((tag, index) => (
                      <Chip
                        key={index}
                        label={tag}
                        variant="outlined"
                        size="small"
                        sx={{
                          borderColor: getTagColor(tag),
                          color: getTagColor(tag),
                        }}
                      />
                    ))}
                  </Box>
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    No tags assigned
                  </Typography>
                )}
              </CardContent>
            </Card>
          </Grid>

          {/* Document health analysis card */}
          <Grid item xs={12}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <InfoIcon color="primary" />
                  Document Health Analysis
                </Typography>

                <Grid container spacing={2}>
                  <Grid item xs={12} md={4}>
                    <Typography variant="subtitle2" color="text.secondary">
                      Freshness Status
                    </Typography>
                    <Typography variant="body1" color={`${freshnessIndicator.color}.main`}>
                      {freshnessIndicator.label}
                    </Typography>
                  </Grid>

                  <Grid item xs={12} md={4}>
                    <Typography variant="subtitle2" color="text.secondary">
                      Days Since Update
                    </Typography>
                    <Typography variant="body1">
                      {daysSinceUpdate} days
                    </Typography>
                  </Grid>

                  <Grid item xs={12} md={4}>
                    <Typography variant="subtitle2" color="text.secondary">
                      Document Age
                    </Typography>
                    <Typography variant="body1">
                      {daysSinceCreation} days
                    </Typography>
                  </Grid>
                </Grid>

                {/* Warning for outdated documents (>90 days) */}
                {daysSinceUpdate > 90 && (
                  <Box sx={{ mt: 2, p: 2, backgroundColor: 'error.light', borderRadius: 1 }}>
                    <Typography variant="body2" color="error.contrastText">
                      ⚠️ This document hasn't been updated in over 90 days and may contain outdated information.
                    </Typography>
                  </Box>
                )}

                {/* Warning for stale documents (30-90 days) */}
                {daysSinceUpdate > 30 && daysSinceUpdate <= 90 && (
                  <Box sx={{ mt: 2, p: 2, backgroundColor: 'warning.light', borderRadius: 1 }}>
                    <Typography variant="body2" color="warning.contrastText">
                      📝 This document is getting stale. Consider reviewing for updates.
                    </Typography>
                  </Box>
                )}
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </DialogContent>

      {/* Modal action buttons */}
      <DialogActions sx={{ px: 3, pb: 3 }}>
        <Button onClick={handleOpenExternal} startIcon={<LaunchIcon />} variant="outlined">
          Open in Confluence
        </Button>
        <Button onClick={onClose} variant="contained">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};
