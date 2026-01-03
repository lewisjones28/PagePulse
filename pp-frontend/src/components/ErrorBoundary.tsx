/**
 * ErrorBoundary Component
 * React error boundary that catches JavaScript errors anywhere in the component tree.
 * Displays a fallback UI when an error occurs instead of crashing the entire app.
 * Implements React's error boundary pattern using class components.
 */
import { Component } from 'react';
import type { ErrorInfo, ReactNode } from 'react';
import {
  Box,
  Typography,
  Button,
  Paper,
  Alert,
} from '@mui/material';
import {
  Refresh as RefreshIcon,
  BugReport as BugReportIcon,
} from '@mui/icons-material';

/** Props for the ErrorBoundary component */
interface Props {
  /** Child components to be wrapped by the error boundary */
  children?: ReactNode;
  /** Optional custom fallback UI to display on error */
  fallback?: ReactNode;
}

/** State for tracking error information */
interface State {
  /** Whether an error has been caught */
  hasError: boolean;
  /** The error object if an error was caught */
  error?: Error;
  /** Additional error information from React */
  errorInfo?: ErrorInfo;
}

/**
 * ErrorBoundary class component.
 * Catches errors in child components and displays a user-friendly error UI.
 */
export class ErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false };
  }

  /**
   * Static lifecycle method called when an error is thrown.
   * Updates state to trigger error UI rendering.
   * @param error - The error that was thrown
   * @returns New state object
   */
  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  /**
   * Lifecycle method called after an error has been caught.
   * Used for error logging and side effects.
   * @param error - The error that was thrown
   * @param errorInfo - Additional error information from React
   */
  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('ErrorBoundary caught an error:', error, errorInfo);
    this.setState({ error, errorInfo });
  }

  /**
   * Handler for the refresh button.
   * Reloads the entire page to recover from the error.
   */
  private handleRefresh = () => {
    this.setState({ hasError: false, error: undefined, errorInfo: undefined });
    window.location.reload();
  };

  /**
   * Handler for the try again button.
   * Resets error state to attempt re-rendering without a full page reload.
   */
  private handleReset = () => {
    this.setState({ hasError: false, error: undefined, errorInfo: undefined });
  };

  /**
   * Renders either the error UI or the child components.
   * @returns Error UI if an error occurred, otherwise child components
   */
  public render() {
    if (this.state.hasError) {
      // Use custom fallback if provided
      if (this.props.fallback) {
        return this.props.fallback;
      }

      // Default error UI
      return (
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            minHeight: '50vh',
            p: 3,
          }}
        >
          <Paper
            sx={{
              p: 4,
              maxWidth: 600,
              textAlign: 'center',
            }}
          >
            {/* Error icon */}
            <BugReportIcon
              sx={{
                fontSize: 64,
                color: 'error.main',
                mb: 2,
              }}
            />

            <Typography variant="h5" gutterBottom color="error">
              Something went wrong
            </Typography>

            <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
              An unexpected error occurred in the PagePulse dashboard.
              Please try refreshing the page or contact support if the problem persists.
            </Typography>

            {/* Display error details if available */}
            {this.state.error && (
              <Alert severity="error" sx={{ mb: 3, textAlign: 'left' }}>
                <Typography variant="subtitle2" gutterBottom>
                  Error Details:
                </Typography>
                <Typography variant="body2" component="pre" sx={{ fontSize: '0.8rem' }}>
                  {this.state.error.message}
                </Typography>
              </Alert>
            )}
            {/* Action buttons for error recovery */}
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
              <Button
                variant="contained"
                startIcon={<RefreshIcon />}
                onClick={this.handleRefresh}
                color="primary"
              >
                Refresh Page
              </Button>

              <Button
                variant="outlined"
                onClick={this.handleReset}
                color="primary"
              >
                Try Again
              </Button>
            </Box>

            {/* Show detailed stack trace in development mode only */}
            {import.meta.env.DEV && this.state.errorInfo && (
              <Box sx={{ mt: 3, textAlign: 'left' }}>
                <Typography variant="subtitle2" gutterBottom>
                  Stack Trace (Development):
                </Typography>
                <Typography
                  variant="body2"
                  component="pre"
                  sx={{
                    fontSize: '0.7rem',
                    backgroundColor: 'grey.100',
                    p: 2,
                    borderRadius: 1,
                    overflow: 'auto',
                    maxHeight: 200,
                  }}
                >
                  {this.state.errorInfo.componentStack}
                </Typography>
              </Box>
            )}
          </Paper>
        </Box>
      );
    }

    // No error - render children normally
    return this.props.children;
  }
}
