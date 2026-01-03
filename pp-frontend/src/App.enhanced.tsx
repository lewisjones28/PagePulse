import { useState, useEffect } from 'react';
import './App.css';

// Enhanced PagePulse Dashboard with dark mode design
function App() {
  const [currentView, setCurrentView] = useState<'dashboard' | 'documents'>('dashboard');
  const [isOnline, setIsOnline] = useState(true);
  const [lastUpdate, setLastUpdate] = useState(new Date());

  // Simulate API status check
  useEffect(() => {
    const interval = setInterval(() => {
      setLastUpdate(new Date());
      // Simulate occasional offline status
      setIsOnline(Math.random() > 0.1);
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  const mockDocuments = [
    {
      id: 1,
      title: 'API Documentation Guidelines',
      status: 'Active',
      lastUpdated: '2 hours ago',
      tags: ['documentation', 'api', 'guidelines'],
      freshness: 'fresh'
    },
    {
      id: 2,
      title: 'Database Migration Strategy',
      status: 'Draft',
      lastUpdated: '5 days ago',
      tags: ['database', 'migration', 'strategy'],
      freshness: 'stale'
    },
    {
      id: 3,
      title: 'Security Best Practices',
      status: 'Active',
      lastUpdated: '1 day ago',
      tags: ['security', 'best-practices', 'authentication'],
      freshness: 'fresh'
    },
    {
      id: 4,
      title: 'Legacy System Integration',
      status: 'Inactive',
      lastUpdated: '4 months ago',
      tags: ['legacy', 'integration', 'deprecated'],
      freshness: 'outdated'
    },
    {
      id: 5,
      title: 'Frontend Architecture Overview',
      status: 'Active',
      lastUpdated: '3 hours ago',
      tags: ['frontend', 'architecture', 'react'],
      freshness: 'fresh'
    },
    {
      id: 6,
      title: 'Testing Strategy Document',
      status: 'Active',
      lastUpdated: '1 week ago',
      tags: ['testing', 'strategy', 'qa'],
      freshness: 'stale'
    }
  ];

  const metrics = {
    total: mockDocuments.length,
    fresh: mockDocuments.filter(doc => doc.freshness === 'fresh').length,
    stale: mockDocuments.filter(doc => doc.freshness === 'stale').length,
    outdated: mockDocuments.filter(doc => doc.freshness === 'outdated').length
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>PagePulse Dashboard</h1>
        <nav>
          <button
            onClick={() => setCurrentView('dashboard')}
            className={currentView === 'dashboard' ? 'active' : ''}
          >
            📊 Dashboard
          </button>
          <button
            onClick={() => setCurrentView('documents')}
            className={currentView === 'documents' ? 'active' : ''}
          >
            📄 Documents
          </button>
        </nav>
      </header>

      <main className="app-main">
        {currentView === 'dashboard' ? (
          <div className="dashboard">
            <h2>Document Analytics</h2>
            <div className="metrics-grid">
              <div className="metric-card">
                <h3>Total Documents</h3>
                <p className="metric-value">{metrics.total}</p>
              </div>
              <div className="metric-card">
                <h3>Fresh Documents</h3>
                <p className="metric-value">{metrics.fresh}</p>
              </div>
              <div className="metric-card">
                <h3>Stale Documents</h3>
                <p className="metric-value">{metrics.stale}</p>
              </div>
              <div className="metric-card">
                <h3>Outdated Documents</h3>
                <p className="metric-value">{metrics.outdated}</p>
              </div>
            </div>

            <div className="info-section">
              <h3>System Status</h3>
              <p>
                {isOnline ? '🟢' : '🔴'}
                {isOnline ? 'Connected' : 'Disconnected'} to PagePulse API at http://localhost:8089
              </p>
              <p>
                📊 Mock data is enabled for development and testing
              </p>
              <p>
                🕐 Last updated: {lastUpdate.toLocaleTimeString()}
              </p>
              <p>
                ⚡ Built with React + Vite + TypeScript
              </p>
            </div>
          </div>
        ) : (
          <div className="documents">
            <h2>Document Library</h2>
            <div className="documents-list">
              {mockDocuments.map((document) => (
                <div key={document.id} className="document-card">
                  <h3>{document.title}</h3>
                  <p>
                    Status: <strong>{document.status}</strong> |
                    Last Updated: <strong>{document.lastUpdated}</strong> |
                    Freshness: <strong style={{
                      color: document.freshness === 'fresh' ? '#10b981' :
                             document.freshness === 'stale' ? '#f59e0b' : '#ef4444'
                    }}>
                      {document.freshness.charAt(0).toUpperCase() + document.freshness.slice(1)}
                    </strong>
                  </p>
                  <div className="tags">
                    {document.tags.map((tag, index) => (
                      <span key={index} className="tag">
                        {tag}
                      </span>
                    ))}
                  </div>
                </div>
              ))}
            </div>
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
