import React, { useState } from 'react';
import {
  ThemeProvider,
  createTheme,
  CssBaseline,
  Box,
  Container,
  Tabs,
  Tab,
  Paper,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  Description as DocumentIcon,
} from '@mui/icons-material';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter as Router } from 'react-router-dom';

// Components
import { ErrorBoundary } from './components/ErrorBoundary';
import { Header } from './components/Header';
import { Dashboard } from './components/Dashboard';
import { DocumentList } from './components/DocumentList';
import { DocumentDetailModal } from './components/DocumentDetailModal';

// Types
import { DocumentApiDto } from './types/api';

// Create theme
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
  typography: {
    h4: {
      fontWeight: 600,
    },
    h6: {
      fontWeight: 600,
    },
  },
});

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      staleTime: 30000,
    },
  },
});

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box>
          {children}
        </Box>
      )}
    </div>
  );
}

function App() {
  const [currentTab, setCurrentTab] = useState(0);
  const [selectedDocument, setSelectedDocument] = useState<DocumentApiDto | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setCurrentTab(newValue);
  };

  const handleDocumentSelect = (document: DocumentApiDto) => {
    setSelectedDocument(document);
    setIsDetailModalOpen(true);
  };

  const handleCloseDetailModal = () => {
    setIsDetailModalOpen(false);
    setSelectedDocument(null);
  };

  return (
    <ErrorBoundary>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <Router>
            <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
              <Header />

              <Container maxWidth={false} sx={{ flex: 1, mt: 3, mb: 3 }}>
                <Paper sx={{ mb: 3 }}>
                  <Tabs
                    value={currentTab}
                    onChange={handleTabChange}
                    aria-label="main navigation tabs"
                    sx={{ px: 2 }}
                  >
                    <Tab
                      icon={<DashboardIcon />}
                      label="Dashboard"
                      iconPosition="start"
                    />
                    <Tab
                      icon={<DocumentIcon />}
                      label="Documents"
                      iconPosition="start"
                    />
                  </Tabs>
                </Paper>

                <ErrorBoundary>
                  <TabPanel value={currentTab} index={0}>
                    <Dashboard />
                  </TabPanel>

                  <TabPanel value={currentTab} index={1}>
                    <DocumentList onDocumentSelect={handleDocumentSelect} />
                  </TabPanel>
                </ErrorBoundary>
              </Container>

              {/* Document Detail Modal */}
              <DocumentDetailModal
                document={selectedDocument}
                open={isDetailModalOpen}
                onClose={handleCloseDetailModal}
              />
            </Box>
          </Router>
        </ThemeProvider>
      </QueryClientProvider>
    </ErrorBoundary>
  );
}

export default App;
