/**
 * Header Component
 * Displays the application header with title, branding, and API status indicator.
 * Shows real-time API health status with automatic polling.
 */
import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Chip,
  CircularProgress,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  Description as DocumentIcon,
} from '@mui/icons-material';
import { useHealthCheck } from '../hooks/useDocuments';

interface HeaderProps {
  /** Optional custom title for the header (defaults to "PagePulse Dashboard") */
  title?: string;
}

/**
 * Header component with API health status monitoring.
 * @param props - Component props
 * @returns Rendered header component
 */
export const Header: React.FC<HeaderProps> = ({ title = 'PagePulse Dashboard' }) => {
  // Fetch API health status with automatic polling
  const { data: isHealthy, isLoading: healthLoading } = useHealthCheck();

  return (
    <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
      <Toolbar>
        {/* Dashboard icon and title */}
        <DashboardIcon sx={{ mr: 2 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          {title}
        </Typography>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {/* Application subtitle */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <DocumentIcon fontSize="small" />
            <Typography variant="body2">
              Document Management System
            </Typography>
          </Box>

          {/* API health status indicator */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            {healthLoading ? (
              <CircularProgress size={16} color="inherit" />
            ) : (
              <Chip
                label={isHealthy ? 'API Online' : 'API Offline'}
                color={isHealthy ? 'success' : 'error'}
                size="small"
                variant="outlined"
                sx={{
                  color: 'white',
                  borderColor: 'white',
                  '&.MuiChip-colorSuccess': {
                    borderColor: '#4caf50',
                    color: '#4caf50',
                  },
                  '&.MuiChip-colorError': {
                    borderColor: '#f44336',
                    color: '#f44336',
                  }
                }}
              />
            )}
          </Box>
        </Box>
      </Toolbar>
    </AppBar>
  );
};
