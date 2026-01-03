/**
 * DocumentList Component
 * Displays a paginated, searchable, and filterable list of documents in a grid layout.
 * Features:
 * - Search by title, external ID, or tags
 * - Filter by document status
 * - Sort by multiple criteria
 * - Adjustable page size
 * - Responsive grid layout
 */
import React, { useState, useMemo } from 'react';
import {
  Grid,
  Box,
  Typography,
  Pagination,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  InputAdornment,
  CircularProgress,
  Alert,
  Paper,
  Chip,
  FormControlLabel,
  Checkbox,
} from '@mui/material';
import {
  Search as SearchIcon,
} from '@mui/icons-material';
import { DocumentCard } from './DocumentCard';
import { useDocuments } from '../hooks/useDocuments';
import { DocumentApiDto, PaginationParams } from '../types/api';

interface DocumentListProps {
  /** Callback when a document is selected for detailed view */
  onDocumentSelect?: (document: DocumentApiDto) => void;
}

/** Available page size options for pagination */
const PAGE_SIZES = [6, 12, 24, 48];

/** Available sorting options with labels */
const SORT_OPTIONS = [
  { value: 'title,asc', label: 'Title (A-Z)' },
  { value: 'title,desc', label: 'Title (Z-A)' },
  { value: 'documentLastUpdatedAt,desc', label: 'Recently Updated' },
  { value: 'documentLastUpdatedAt,asc', label: 'Oldest Updated' },
  { value: 'documentLastCreatedAt,desc', label: 'Recently Created' },
  { value: 'documentLastCreatedAt,asc', label: 'Oldest Created' },
  { value: 'status,asc', label: 'Status (A-Z)' },
];

/**
 * DocumentList component for displaying and managing document lists.
 * @param props - Component props
 * @returns Rendered document list with search, filters, and pagination
 */
export const DocumentList: React.FC<DocumentListProps> = ({ onDocumentSelect }) => {
  // Pagination and sorting state
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(12);
  const [sortBy, setSortBy] = useState('documentLastUpdatedAt,desc');
  
  // Filter state
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [showFilters, setShowFilters] = useState(false);

  // Build pagination parameters for API call
  const paginationParams: PaginationParams = useMemo(() => ({
    page: currentPage,
    size: pageSize,
    sort: [sortBy],
  }), [currentPage, pageSize, sortBy]);

  // Fetch documents with React Query
  const { data, isLoading, error, refetch } = useDocuments(paginationParams);

  /**
   * Filter documents based on search term and status filter.
   * Searches across title, external ID, and tags.
   */
  const filteredDocuments = useMemo(() => {
    if (!data?.content) return [];

    return data.content.filter(document => {
      // Check if document matches search term
      const matchesSearch = !searchTerm ||
        document.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        document.externalId.toLowerCase().includes(searchTerm.toLowerCase()) ||
        document.tags.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase()));

      // Check if document matches status filter
      const matchesStatus = !statusFilter || document.status === statusFilter;

      return matchesSearch && matchesStatus;
    });
  }, [data, searchTerm, statusFilter]);

  /**
   * Extract unique statuses from documents for filter dropdown.
   */
  const availableStatuses = useMemo(() => {
    if (!data?.content) return [];
    const statuses = Array.from(new Set(data.content.map(doc => doc.status)));
    return statuses.sort();
  }, [data]);

  /** Handle page change (convert from 1-based to 0-based indexing) */
  const handlePageChange = (_: React.ChangeEvent<unknown>, page: number) => {
    setCurrentPage(page - 1); // MUI pagination is 1-based, our API is 0-based
  };

  /** Handle page size change and reset to first page */
  const handlePageSizeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPageSize(Number(event.target.value));
    setCurrentPage(0); // Reset to first page
  };

  /** Handle sort order change and reset to first page */
  const handleSortChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSortBy(event.target.value);
    setCurrentPage(0); // Reset to first page
  };

  /** Handle search term changes */
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  /** Handle status filter changes */
  const handleStatusFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStatusFilter(event.target.value);
  };

  // Display error state if API call fails
  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Alert
          severity="error"
          action={
            <Chip
              label="Retry"
              onClick={() => refetch()}
              size="small"
              clickable
            />
          }
        >
          Failed to load documents: {error.message}
        </Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      {/* Search bar and filter controls */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'center' }}>
          <TextField
            label="Search documents"
            variant="outlined"
            value={searchTerm}
            onChange={handleSearchChange}
            size="small"
            sx={{ minWidth: 250 }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />

          <FormControlLabel
            control={
              <Checkbox
                checked={showFilters}
                onChange={(e) => setShowFilters(e.target.checked)}
              />
            }
            label="Show Filters"
          />
        </Box>

        {/* Expandable filter section */}
        {showFilters && (
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', mt: 2 }}>
            <FormControl size="small" sx={{ minWidth: 120 }}>
              <InputLabel>Status</InputLabel>
              <Select
                value={statusFilter}
                label="Status"
                onChange={handleStatusFilterChange}
              >
                <MenuItem value="">All</MenuItem>
                {availableStatuses.map(status => (
                  <MenuItem key={status} value={status}>{status}</MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl size="small" sx={{ minWidth: 150 }}>
              <InputLabel>Sort By</InputLabel>
              <Select
                value={sortBy}
                label="Sort By"
                onChange={handleSortChange}
              >
                {SORT_OPTIONS.map(option => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl size="small" sx={{ minWidth: 100 }}>
              <InputLabel>Per Page</InputLabel>
              <Select
                value={pageSize}
                label="Per Page"
                onChange={handlePageSizeChange}
              >
                {PAGE_SIZES.map(size => (
                  <MenuItem key={size} value={size}>{size}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
        )}
      </Paper>

      {/* Document count and loading indicator */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h6" component="h2">
          Documents {data?.pageInfo && `(${data.pageInfo.elements} total)`}
        </Typography>

        {isLoading && <CircularProgress size={24} />}
      </Box>

      {/* Document grid with loading and empty states */}
      {isLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
          <CircularProgress />
        </Box>
      ) : filteredDocuments.length === 0 ? (
        <Box sx={{ textAlign: 'center', p: 4 }}>
          <Typography variant="h6" color="text.secondary">
            {searchTerm || statusFilter ? 'No documents match your filters' : 'No documents found'}
          </Typography>
          {(searchTerm || statusFilter) && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Try adjusting your search criteria or filters
            </Typography>
          )}
        </Box>
      ) : (
        <Grid container spacing={3}>
          {filteredDocuments.map((document) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={document.id}>
              <DocumentCard
                document={document}
                onViewDetails={onDocumentSelect}
              />
            </Grid>
          ))}
        </Grid>
      )}

      {/* Pagination controls (only shown if multiple pages exist) */}
      {data?.pageInfo && data.pageInfo.pages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
          <Pagination
            count={data.pageInfo.pages}
            page={currentPage + 1} // MUI pagination is 1-based
            onChange={handlePageChange}
            color="primary"
            size="large"
          />
        </Box>
      )}
    </Box>
  );
};
