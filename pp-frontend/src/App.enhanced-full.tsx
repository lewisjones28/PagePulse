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
  Zap
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
import './App.css';

interface Document {
  id: number;
  title: string;
  status: string;
  lastUpdated: string;
  tags: string[];
  freshness: 'fresh' | 'stale' | 'outdated';
  externalId: string;
  createdDate: string;
  author: string;
}

function App() {
  const [currentView, setCurrentView] = useState<'dashboard' | 'documents'>('dashboard');
  const [isOnline, setIsOnline] = useState(true);
  const [lastUpdate, setLastUpdate] = useState(new Date());

  // Document filtering and pagination states
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [freshnessFilter, setFreshnessFilter] = useState('All');
  const [tagFilter, setTagFilter] = useState('All');
  const [currentPage, setCurrentPage] = useState(0);
  const [itemsPerPage] = useState(6);
  const [showFilters, setShowFilters] = useState(false);

  // Simulate API status check
  useEffect(() => {
    const interval = setInterval(() => {
      setLastUpdate(new Date());
      setIsOnline(Math.random() > 0.1);
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  const mockDocuments: Document[] = [
    {
      id: 1,
      title: 'API Documentation Guidelines',
      status: 'Active',
      lastUpdated: '2 hours ago',
      tags: ['documentation', 'api', 'guidelines'],
      freshness: 'fresh',
      externalId: 'CONF-12345',
      createdDate: '2025-12-01',
      author: 'John Doe'
    },
    {
      id: 2,
      title: 'Database Migration Strategy',
      status: 'Draft',
      lastUpdated: '5 days ago',
      tags: ['database', 'migration', 'strategy'],
      freshness: 'stale',
      externalId: 'CONF-12346',
      createdDate: '2025-11-15',
      author: 'Jane Smith'
    },
    {
      id: 3,
      title: 'Security Best Practices',
      status: 'Active',
      lastUpdated: '1 day ago',
      tags: ['security', 'best-practices', 'authentication'],
      freshness: 'fresh',
      externalId: 'CONF-12347',
      createdDate: '2025-10-01',
      author: 'Mike Johnson'
    },
    {
      id: 4,
      title: 'Legacy System Integration',
      status: 'Inactive',
      lastUpdated: '4 months ago',
      tags: ['legacy', 'integration', 'deprecated'],
      freshness: 'outdated',
      externalId: 'CONF-12348',
      createdDate: '2025-06-01',
      author: 'Sarah Wilson'
    },
    {
      id: 5,
      title: 'Frontend Architecture Overview',
      status: 'Active',
      lastUpdated: '3 hours ago',
      tags: ['frontend', 'architecture', 'react'],
      freshness: 'fresh',
      externalId: 'CONF-12349',
      createdDate: '2025-11-01',
      author: 'Tom Brown'
    },
    {
      id: 6,
      title: 'Testing Strategy Document',
      status: 'Active',
      lastUpdated: '1 week ago',
      tags: ['testing', 'strategy', 'qa'],
      freshness: 'stale',
      externalId: 'CONF-12350',
      createdDate: '2025-10-15',
      author: 'Lisa Davis'
    },
    {
      id: 7,
      title: 'DevOps Pipeline Configuration',
      status: 'Active',
      lastUpdated: '2 days ago',
      tags: ['devops', 'pipeline', 'ci-cd'],
      freshness: 'fresh',
      externalId: 'CONF-12351',
      createdDate: '2025-12-15',
      author: 'Alex Chen'
    },
    {
      id: 8,
      title: 'User Experience Guidelines',
      status: 'Draft',
      lastUpdated: '2 weeks ago',
      tags: ['ux', 'design', 'guidelines'],
      freshness: 'stale',
      externalId: 'CONF-12352',
      createdDate: '2025-09-20',
      author: 'Emma White'
    },
    {
      id: 9,
      title: 'Monitoring and Alerting Setup',
      status: 'Active',
      lastUpdated: '6 hours ago',
      tags: ['monitoring', 'alerting', 'observability'],
      freshness: 'fresh',
      externalId: 'CONF-12353',
      createdDate: '2025-12-20',
      author: 'David Lee'
    },
    {
      id: 10,
      title: 'Data Backup Procedures',
      status: 'Inactive',
      lastUpdated: '6 months ago',
      tags: ['backup', 'data', 'procedures'],
      freshness: 'outdated',
      externalId: 'CONF-12354',
      createdDate: '2025-04-10',
      author: 'Rachel Green'
    }
  ];

  // Filter documents based on search and filters
  const filteredDocuments = useMemo(() => {
    return mockDocuments.filter(doc => {
      const matchesSearch = !searchTerm ||
        doc.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doc.tags.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase())) ||
        doc.author.toLowerCase().includes(searchTerm.toLowerCase());

      const matchesStatus = statusFilter === 'All' || doc.status === statusFilter;
      const matchesFreshness = freshnessFilter === 'All' || doc.freshness === freshnessFilter;
      const matchesTag = tagFilter === 'All' || doc.tags.includes(tagFilter);

      return matchesSearch && matchesStatus && matchesFreshness && matchesTag;
    });
  }, [mockDocuments, searchTerm, statusFilter, freshnessFilter, tagFilter]);

  // Pagination
  const pageCount = Math.ceil(filteredDocuments.length / itemsPerPage);
  const displayedDocuments = filteredDocuments.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  const handlePageClick = (event: { selected: number }) => {
    setCurrentPage(event.selected);
  };

  // Get unique values for filters
  const allStatuses = Array.from(new Set(mockDocuments.map(doc => doc.status)));
  const allTags = Array.from(new Set(mockDocuments.flatMap(doc => doc.tags)));
  const allFreshness = ['fresh', 'stale', 'outdated'];

  // Calculate metrics
  const metrics = {
    total: mockDocuments.length,
    fresh: mockDocuments.filter(doc => doc.freshness === 'fresh').length,
    stale: mockDocuments.filter(doc => doc.freshness === 'stale').length,
    outdated: mockDocuments.filter(doc => doc.freshness === 'outdated').length
  };

  // Chart data
  const chartData = [
    { name: 'Fresh', count: metrics.fresh, color: '#10b981' },
    { name: 'Stale', count: metrics.stale, color: '#f59e0b' },
    { name: 'Outdated', count: metrics.outdated, color: '#ef4444' }
  ];

  const statusData = [
    { name: 'Active', count: mockDocuments.filter(doc => doc.status === 'Active').length },
    { name: 'Draft', count: mockDocuments.filter(doc => doc.status === 'Draft').length },
    { name: 'Inactive', count: mockDocuments.filter(doc => doc.status === 'Inactive').length }
  ];

  const pieColors = ['#00d4ff', '#7c3aed', '#ef4444'];

  const openInConfluence = (externalId: string) => {
    // In a real application, this would open the actual Confluence page
    const confluenceUrl = `https://your-confluence.atlassian.net/wiki/pages/viewpage.action?pageId=${externalId}`;
    console.log(`Opening Confluence page: ${confluenceUrl}`);
    alert(`Would open: ${confluenceUrl}\n\n(This is a demo - configure your Confluence URL)`);
  };

  const resetFilters = () => {
    setSearchTerm('');
    setStatusFilter('All');
    setFreshnessFilter('All');
    setTagFilter('All');
    setCurrentPage(0);
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
            Documents
          </button>
        </nav>
      </header>

      <main className="app-main">
        {currentView === 'dashboard' ? (
          <div className="dashboard">
            <h2>
              <TrendingUp className="section-icon" />
              Document Analytics
            </h2>

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
                <ResponsiveContainer width="100%" height={300}>
                  <PieChart>
                    <Pie
                      data={statusData}
                      cx="50%"
                      cy="50%"
                      outerRadius={100}
                      dataKey="count"
                      label={({ name, value }) => `${name}: ${value}`}
                    >
                      {statusData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={pieColors[index % pieColors.length]} />
                      ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </div>

            <div className="info-section">
              <h3>
                <Activity className="section-icon" />
                System Status
              </h3>
              <p>
                {isOnline ? <CheckCircle className="status-icon online" /> : <AlertTriangle className="status-icon offline" />}
                {isOnline ? 'Connected' : 'Disconnected'} to PagePulse API at http://localhost:8089
              </p>
              <p>
                <BarChart3 className="status-icon" />
                Mock data is enabled for development and testing
              </p>
              <p>
                <Clock className="status-icon" />
                Last updated: {lastUpdate.toLocaleTimeString()}
              </p>
              <p>
                <Zap className="status-icon" />
                Built with React + Vite + TypeScript
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
                  placeholder="Search documents, tags, or authors..."
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
                      <strong>Status:</strong> {document.status} |
                      <strong> Author:</strong> {document.author}
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
                    {document.tags.map((tag, index) => (
                      <span key={index} className="tag">
                        {tag}
                      </span>
                    ))}
                  </div>
                </div>
              ))}
            </div>

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

            {filteredDocuments.length === 0 && (
              <div className="no-results">
                <FileText size={48} />
                <h3>No documents found</h3>
                <p>Try adjusting your search criteria or filters</p>
                <button onClick={resetFilters} className="reset-btn">
                  Reset Filters
                </button>
              </div>
            )}
          </div>
        )}
      </main>

      <footer className="app-footer">
        <p>
          PagePulse Frontend v1.0.0 |
          Built with ❤️ using React + Vite + TypeScript |
          Dark Mode Theme ✨
        </p>
      </footer>
    </div>
  );
}

export default App;
