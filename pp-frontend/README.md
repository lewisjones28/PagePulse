# PagePulse Frontend

React TypeScript frontend application for the PagePulse document management system. Provides a dashboard to monitor and manage Confluence document health and staleness.

## 🚀 Quick Start

### Prerequisites
- Node.js 18+
- npm or yarn
- PagePulse API running on `http://localhost:8089` (optional - has mock data fallback)

### Development
```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

The application will be available at `http://localhost:3000`

### Production
```bash
# Build for production
npm run build

# Preview production build
npm run preview
```

## ⚙️ Environment Variables

Create a `.env` file in the root directory:

```env
# API Configuration
VITE_API_BASE_URL=http://localhost:8089

# Application Settings
VITE_APP_NAME=PagePulse Dashboard
VITE_APP_VERSION=1.0.0
```

## ✨ Key Features

### Dashboard
- **Document Metrics**: Total documents, fresh/stale/outdated counts
- **Status Distribution**: Visual breakdown of document health
- **Real-time Updates**: Auto-refresh every 30 seconds
- **API Status Monitoring**: Live connection status indicator

### Document Management
- **Document Library**: Browse all documents with filtering
- **Status Indicators**: Color-coded freshness levels (Fresh/Stale/Outdated)
- **Tag System**: Document categorization and filtering
- **Pagination**: Navigate through large document sets
- **Confluence Integration**: Direct links to open documents

### User Interface
- **Dark Mode Theme**: Modern cyberpunk-inspired design
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Smooth Animations**: Hover effects and transitions
- **Interactive Charts**: Document status distribution graphs

## 🛠️ Development

### Available Scripts
```bash
npm run dev      # Start development server
npm run build    # Build for production
npm run preview  # Preview production build
npm run lint     # Run ESLint
```

### Project Structure
```
src/
├── App.tsx             # Main application component
├── App.css             # Global styles and theming
├── main.tsx            # Application entry point
├── components/         # Reusable UI components
├── services/           # API and data services
├── types/              # TypeScript type definitions
├── hooks/              # Custom React hooks
└── utils/              # Helper utilities
```

### Key Technologies
- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **CSS3** - Styling with custom properties
- **Chart.js** - Data visualization

## 🔧 Configuration

### API Integration
The frontend connects to the PagePulse API for document data. If the API is unavailable, it falls back to mock data for development.

### CORS Setup
Ensure the backend API has CORS configured to allow requests from the frontend origin.

### Proxy Configuration
Development server proxies API requests to avoid CORS issues during development.

## 🔍 API Endpoints

The frontend consumes the following endpoints from the PagePulse API:

- `GET /documents` - Fetch paginated documents with filtering
- `GET /documents/{id}` - Get specific document details
- `GET /health` - API health check