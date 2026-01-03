import { useState, useEffect, useMemo } from 'react';
import {
  BarChart3,
  FileText,
  Search,
  Filter,
  ChevronDown,
  ExternalLink,
  Calendar,
  Tag,
  Activity,
  TrendingUp,
  AlertTriangle,
  CheckCircle,
  Clock,
  Database,
  Zap,
  Loader,
  WifiOff
} from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend
} from 'recharts';
import ReactPaginate from 'react-paginate';
import { DocumentService } from './services/documentService';
import { DocumentApiDto, PagedDocumentApiDto } from './types/api';
import './App.css';

// Enhanced document interface that maps API data to display format
interface EnhancedDocument {
  id: number;
  title: string;
  status: string;
  lastUpdated: string;
  tags: string[];
  freshness: 'fresh' | 'stale' | 'outdated';
  externalId: string;
  createdDate: string;
  documentLastCreatedAt: string;
  documentLastUpdatedAt: string;
}

function App() {
  const [currentView, setCurrentView] = useState<'dashboard' | 'documents'>('dashboard');
  const [isOnline, setIsOnline] = useState(true);
  const [lastUpdate, setLastUpdate] = useState(new Date());

  // API Data State
  const [documents, setDocuments] = useState<EnhancedDocument[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [apiError, setApiError] = useState<string | null>(null);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Document filtering and pagination states
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [freshnessFilter, setFreshnessFilter] = useState('All');
  const [tagFilter, setTagFilter] = useState('All');
  const [currentPage, setCurrentPage] = useState(0);
  const [itemsPerPage] = useState(6);
  const [showFilters, setShowFilters] = useState(false);

  // Helper function to calculate document freshness
  const calculateFreshness = (lastUpdated: string): 'fresh' | 'stale' | 'outdated' => {
    const now = new Date();
    const updatedDate = new Date(lastUpdated);
    const daysDiff = Math.floor((now.getTime() - updatedDate.getTime()) / (1000 * 60 * 60 * 24));

    if (daysDiff < 30) return 'fresh';
    if (daysDiff < 90) return 'stale';
    return 'outdated';
  };

  // Helper function to format relative time
  const formatRelativeTime = (dateString: string): string => {
    const now = new Date();
    const date = new Date(dateString);
    const diffMs = now.getTime() - date.getTime();
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMinutes = Math.floor(diffMs / (1000 * 60));

    if (diffDays > 0) {
      return diffDays === 1 ? '1 day ago' : `${diffDays} days ago`;
    } else if (diffHours > 0) {
      return diffHours === 1 ? '1 hour ago' : `${diffHours} hours ago`;
    } else if (diffMinutes > 0) {
      return diffMinutes === 1 ? '1 minute ago' : `${diffMinutes} minutes ago`;
    } else {
      return 'Just now';
    }
  };

  // Transform API data to enhanced format
  const transformApiDocument = (apiDoc: DocumentApiDto): EnhancedDocument => {
    const freshness = calculateFreshness(apiDoc.documentLastUpdatedAt);
    return {
      id: apiDoc.id,
      title: apiDoc.title,
      status: apiDoc.status,
      lastUpdated: formatRelativeTime(apiDoc.documentLastUpdatedAt),
      tags: apiDoc.tags || [],
      freshness,
      externalId: apiDoc.externalId,
      createdDate: apiDoc.documentLastCreatedAt,
      documentLastCreatedAt: apiDoc.documentLastCreatedAt,
      documentLastUpdatedAt: apiDoc.documentLastUpdatedAt
    };
  };

  // Fetch documents from API
  const fetchDocuments = async (page: number = 0, size: number = 50) => {
    setIsLoading(true);
    setApiError(null);

    try {
      const response: PagedDocumentApiDto = await DocumentService.getDocuments({
        page,
        size,
        sort: ['documentLastUpdatedAt,desc']
      });

      const transformedDocs = response.content.map(transformApiDocument);
      setDocuments(transformedDocs);
      setTotalElements(response.pageInfo.elements);
      setTotalPages(response.pageInfo.pages);
      setIsOnline(true);

      console.log(`Loaded ${transformedDocs.length} documents from API`);
    } catch (error: any) {
      console.error('Failed to fetch documents:', error);
      setApiError(error.message || 'Failed to connect to API');
      setIsOnline(false);
      setDocuments([]);
    } finally {
      setIsLoading(false);
    }
  };

  // Check API health
  const checkApiHealth = async () => {
    try {
      const healthy = await DocumentService.healthCheck();
      setIsOnline(healthy);
    } catch (error) {
      setIsOnline(false);
    }
  };

  // Initial data load
  useEffect(() => {
    fetchDocuments();
    checkApiHealth();
  }, []);

  // Periodic health check and data refresh
  useEffect(() => {
    const interval = setInterval(() => {
      setLastUpdate(new Date());
      checkApiHealth();
      // Optionally refresh data every 30 seconds
      // fetchDocuments();
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  // Filter documents based on search and filters
  const filteredDocuments = useMemo(() => {
    return documents.filter(doc => {
      const matchesSearch = !searchTerm ||
        doc.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doc.tags.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase())) ||
        doc.externalId.toLowerCase().includes(searchTerm.toLowerCase());

      const matchesStatus = statusFilter === 'All' || doc.status === statusFilter;
      const matchesFreshness = freshnessFilter === 'All' || doc.freshness === freshnessFilter;
      const matchesTag = tagFilter === 'All' || doc.tags.includes(tagFilter);

      return matchesSearch && matchesStatus && matchesFreshness && matchesTag;
    });
  }, [documents, searchTerm, statusFilter, freshnessFilter, tagFilter]);

  // Pagination for filtered results
  const pageCount = Math.ceil(filteredDocuments.length / itemsPerPage);
  const displayedDocuments = filteredDocuments.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  const handlePageClick = (event: { selected: number }) => {
    setCurrentPage(event.selected);
  };

  // Get unique values for filters
  const allStatuses = Array.from(new Set(documents.map(doc => doc.status)));
  const allTags = Array.from(new Set(documents.flatMap(doc => doc.tags)));
  const allFreshness = ['fresh', 'stale', 'outdated'];

  // Calculate metrics
  const metrics = {
    total: documents.length,
    fresh: documents.filter(doc => doc.freshness === 'fresh').length,
    stale: documents.filter(doc => doc.freshness === 'stale').length,
    outdated: documents.filter(doc => doc.freshness === 'outdated').length
  };

  // Chart data
  const chartData = [
    { name: 'Fresh', count: metrics.fresh, color: '#10b981' },
    { name: 'Stale', count: metrics.stale, color: '#f59e0b' },
    { name: 'Outdated', count: metrics.outdated, color: '#ef4444' }
  ];

  // Dynamic status distribution based on actual document statuses
  const statusData = useMemo(() => {
    const statusCounts = documents.reduce((acc, doc) => {
      acc[doc.status] = (acc[doc.status] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    return Object.entries(statusCounts)
      .map(([status, count]) => ({
        name: status,
        count
      }))
      .filter(item => item.count > 0) // Only show statuses that exist
      .sort((a, b) => b.count - a.count); // Sort by count descending
  }, [documents]);

  const pieColors = ['#00d4ff', '#7c3aed', '#ef4444', '#f59e0b', '#10b981', '#8b5cf6', '#06b6d4', '#f97316'];

  const openInConfluence = (externalId: string) => {
    // Configure this URL to match your Confluence instance
    const confluenceBaseUrl = 'https://your-confluence-instance.atlassian.net';
    const confluenceUrl = `${confluenceBaseUrl}/wiki/spaces/YOUR_SPACE/pages/${externalId}`;

    console.log(`Opening Confluence page: ${confluenceUrl}`);

    // For demo purposes, show an alert. In production, use:
    // window.open(confluenceUrl, '_blank');
    alert(`Would open Confluence page:\n${confluenceUrl}\n\nDocument ID: ${externalId}\n\n(Configure CONFLUENCE_BASE_URL in production)`);
  };

  const resetFilters = () => {
    setSearchTerm('');
    setStatusFilter('All');
    setFreshnessFilter('All');
    setTagFilter('All');
    setCurrentPage(0);
  };

  const refreshData = () => {
    fetchDocuments();
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>
          <Database className="header-icon" />
          PagePulse Dashboard
        </h1>
        <nav>
          <button
            onClick={() => setCurrentView('dashboard')}
            className={currentView === 'dashboard' ? 'active' : ''}
          >
            <BarChart3 size={20} />
            Dashboard
          </button>
          <button
            onClick={() => setCurrentView('documents')}
            className={currentView === 'documents' ? 'active' : ''}
          >
            <FileText size={20} />
            Documents ({documents.length})
          </button>
        </nav>
      </header>

      <main className="app-main">
        {/* API Error Display */}
        {apiError && (
          <div className="api-error">
            <WifiOff className="error-icon" />
            <div>
              <h3>API Connection Error</h3>
              <p>{apiError}</p>
              <p>Make sure the PagePulse API is running on port 8089</p>
              <button onClick={refreshData} className="retry-btn">
                <Activity size={16} />
                Retry Connection
              </button>
            </div>
          </div>
        )}

        {/* Loading State */}
        {isLoading && (
          <div className="loading-state">
            <Loader className="spinner" />
            <p>Loading documents from API...</p>
          </div>
        )}

        {currentView === 'dashboard' ? (
          <div className="dashboard">
            <div className="dashboard-header">
              <h2>
                <TrendingUp className="section-icon" />
                Document Analytics
              </h2>
              <button onClick={refreshData} className="refresh-btn" disabled={isLoading}>
                <Activity size={16} className={isLoading ? 'spinning' : ''} />
                Refresh Data
              </button>
            </div>

            <div className="metrics-grid">
              <div className="metric-card">
                <div className="metric-icon">
                  <FileText />
                </div>
                <h3>Total Documents</h3>
                <p className="metric-value">{metrics.total}</p>
              </div>
              <div className="metric-card fresh">
                <div className="metric-icon">
                  <CheckCircle />
                </div>
                <h3>Fresh Documents</h3>
                <p className="metric-value">{metrics.fresh}</p>
              </div>
              <div className="metric-card stale">
                <div className="metric-icon">
                  <Clock />
                </div>
                <h3>Stale Documents</h3>
                <p className="metric-value">{metrics.stale}</p>
              </div>
              <div className="metric-card outdated">
                <div className="metric-icon">
                  <AlertTriangle />
                </div>
                <h3>Outdated Documents</h3>
                <p className="metric-value">{metrics.outdated}</p>
              </div>
            </div>

            {!isLoading && documents.length > 0 && (
              <div className="charts-grid">
                <div className="chart-card">
                  <h3>
                    <BarChart3 className="chart-icon" />
                    Document Freshness
                  </h3>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={chartData}>
                      <CartesianGrid strokeDasharray="3 3" stroke="var(--border-primary)" />
                      <XAxis dataKey="name" stroke="var(--text-secondary)" />
                      <YAxis stroke="var(--text-secondary)" />
                      <Tooltip
                        contentStyle={{
                          backgroundColor: 'var(--bg-card)',
                          border: '1px solid var(--border-primary)',
                          borderRadius: '8px'
                        }}
                      />
                      <Bar dataKey="count" fill="#00d4ff" radius={[4, 4, 0, 0]} />
                    </BarChart>
                  </ResponsiveContainer>
                </div>

                <div className="chart-card">
                  <h3>
                    <Activity className="chart-icon" />
                    Status Distribution
                  </h3>
                  {statusData.length > 0 ? (
                    <ResponsiveContainer width="100%" height={300}>
                      <PieChart>
                        <Pie
                          data={statusData}
                          cx="50%"
                          cy="50%"
                          outerRadius={100}
                          dataKey="count"
                          label={({ name, value, percent }) => `${name}: ${value} (${(percent * 100).toFixed(1)}%)`}
                        >
                          {statusData.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={pieColors[index % pieColors.length]} />
                          ))}
                        </Pie>
                        <Tooltip
                          formatter={(value, name) => [value, `${name} documents`]}
                          contentStyle={{
                            backgroundColor: 'var(--bg-card)',
                            border: '1px solid var(--border-primary)',
                            borderRadius: '8px'
                          }}
                        />
                        <Legend />
                      </PieChart>
                    </ResponsiveContainer>
                  ) : (
                    <div style={{
                      height: 300,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: 'var(--text-muted)'
                    }}>
                      No status data available
                    </div>
                  )}
                </div>
              </div>
            )}

            <div className="info-section">
              <h3>
                <Activity className="section-icon" />
                API Connection Status
              </h3>
              <p>
                {isOnline ? <CheckCircle className="status-icon online" /> : <AlertTriangle className="status-icon offline" />}
                {isOnline ? 'Connected' : 'Disconnected'} to PagePulse API at {import.meta.env.VITE_API_BASE_URL}
              </p>
              <p>
                <Database className="status-icon" />
                Total documents in database: {totalElements}
              </p>
              <p>
                <Clock className="status-icon" />
                Last updated: {lastUpdate.toLocaleTimeString()}
              </p>
              <p>
                <Zap className="status-icon" />
                Real-time data from pp-syndication-api
              </p>
            </div>
          </div>
        ) : (
          <div className="documents">
            <div className="documents-header">
              <h2>
                <FileText className="section-icon" />
                Document Library
              </h2>
              <div className="documents-stats">
                Showing {displayedDocuments.length} of {filteredDocuments.length} documents
              </div>
            </div>

            {/* Search and Filters */}
            <div className="filters-section">
              <div className="search-bar">
                <Search className="search-icon" />
                <input
                  type="text"
                  placeholder="Search documents, tags, or IDs..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>

              <button
                className="filters-toggle"
                onClick={() => setShowFilters(!showFilters)}
              >
                <Filter size={16} />
                Filters
                <ChevronDown className={`chevron ${showFilters ? 'rotated' : ''}`} />
              </button>
            </div>

            {showFilters && (
              <div className="filters-expanded">
                <div className="filter-group">
                  <label>Status:</label>
                  <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
                    <option value="All">All Statuses</option>
                    {allStatuses.map(status => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                </div>

                <div className="filter-group">
                  <label>Freshness:</label>
                  <select value={freshnessFilter} onChange={(e) => setFreshnessFilter(e.target.value)}>
                    <option value="All">All</option>
                    {allFreshness.map(freshness => (
                      <option key={freshness} value={freshness}>
                        {freshness.charAt(0).toUpperCase() + freshness.slice(1)}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="filter-group">
                  <label>Tag:</label>
                  <select value={tagFilter} onChange={(e) => setTagFilter(e.target.value)}>
                    <option value="All">All Tags</option>
                    {allTags.map(tag => (
                      <option key={tag} value={tag}>{tag}</option>
                    ))}
                  </select>
                </div>

                <button className="reset-filters" onClick={resetFilters}>
                  Reset All Filters
                </button>
              </div>
            )}

            {/* Documents Grid */}
            {!isLoading && documents.length > 0 && (
              <div className="documents-grid">
                {displayedDocuments.map((document) => (
                  <div key={document.id} className="document-card">
                    <div className="document-header">
                      <h3>{document.title}</h3>
                      <button
                        className="confluence-btn"
                        onClick={() => openInConfluence(document.externalId)}
                        title="Open in Confluence"
                      >
                        <ExternalLink size={16} />
                      </button>
                    </div>

                    <div className="document-meta">
                      <p>
                        <strong>Status:</strong> {document.status}
                      </p>
                      <p>
                        <Calendar size={14} />
                        <strong> Last Updated:</strong> {document.lastUpdated}
                      </p>
                      <p>
                        <strong>Freshness:</strong>
                        <span className={`freshness-badge ${document.freshness}`}>
                          {document.freshness === 'fresh' && <CheckCircle size={14} />}
                          {document.freshness === 'stale' && <Clock size={14} />}
                          {document.freshness === 'outdated' && <AlertTriangle size={14} />}
                          {document.freshness.charAt(0).toUpperCase() + document.freshness.slice(1)}
                        </span>
                      </p>
                      <p>
                        <strong>ID:</strong> {document.externalId}
                      </p>
                    </div>

                    <div className="tags">
                      <Tag size={14} />
                      {document.tags.length > 0 ? document.tags.map((tag, index) => (
                        <span key={index} className="tag">
                          {tag}
                        </span>
                      )) : (
                        <span className="no-tags">No tags</span>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Pagination */}
            {pageCount > 1 && (
              <div className="pagination-wrapper">
                <ReactPaginate
                  previousLabel={"← Previous"}
                  nextLabel={"Next →"}
                  pageCount={pageCount}
                  onPageChange={handlePageClick}
                  containerClassName={"pagination"}
                  activeClassName={"active"}
                  pageClassName={"page-item"}
                  pageLinkClassName={"page-link"}
                  previousClassName={"page-item"}
                  previousLinkClassName={"page-link"}
                  nextClassName={"page-item"}
                  nextLinkClassName={"page-link"}
                  disabledClassName={"disabled"}
                  forcePage={currentPage}
                />
              </div>
            )}

            {!isLoading && filteredDocuments.length === 0 && documents.length > 0 && (
              <div className="no-results">
                <FileText size={48} />
                <h3>No documents match your filters</h3>
                <p>Try adjusting your search criteria or filters</p>
                <button onClick={resetFilters} className="reset-btn">
                  Reset Filters
                </button>
              </div>
            )}

            {!isLoading && documents.length === 0 && !apiError && (
              <div className="no-results">
                <Database size={48} />
                <h3>No documents found</h3>
                <p>The API returned no documents. Check your database.</p>
                <button onClick={refreshData} className="reset-btn">
                  Refresh Data
                </button>
              </div>
            )}
          </div>
        )}
      </main>

      <footer className="app-footer">
        <p>
          PagePulse Frontend v1.0.0 |
          Connected to API at {import.meta.env.VITE_API_BASE_URL} |
          Real-time Data ⚡
        </p>
      </footer>
    </div>
  );
}

export default App;
