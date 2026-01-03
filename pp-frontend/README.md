# PagePulse Frontend - Dark Mode Dashboard

A stunning dark mode React TypeScript dashboard for the PagePulse document management system. Features a modern, professional interface with cyberpunk-inspired aesthetics to monitor and manage Confluence document health and staleness.

## ✨ Features

### 🌙 Dark Mode Design
- **Modern Dark Theme**: Cyberpunk-inspired color palette with electric blue accents
- **Gradient Effects**: Beautiful gradients and glowing elements
- **Smooth Animations**: Hover effects, transitions, and loading animations
- **Glassmorphism**: Backdrop blur effects and translucent elements
- **Responsive Layout**: Optimized for all screen sizes and devices

### 📊 Dashboard Analytics
- **Live Metrics**: Real-time document statistics with animated counters
- **Interactive Cards**: Hover effects and smooth transitions
- **Status Indicators**: Color-coded freshness indicators (Fresh/Stale/Outdated)
- **System Status**: Live API connection monitoring with visual indicators
- **Time Stamps**: Real-time last updated information

### 📄 Document Management
- **Document Library**: Enhanced card-based layout with hover animations
- **Smart Filtering**: Visual freshness indicators with color coding
- **Tag System**: Modern pill-style tags with hover effects
- **Status Tracking**: Clear document status and update timestamps
- **Interactive Elements**: Smooth hover animations and micro-interactions

### 🎨 Enhanced User Experience
- **Cyberpunk Theme**: Dark backgrounds with electric blue accents
- **Gradient Typography**: Text with gradient effects and shadows
- **Smooth Scrolling**: Native smooth scroll behavior
- **Custom Scrollbars**: Styled scrollbars matching the dark theme
- **Focus States**: Accessible focus indicators with theme colors
- **Micro-animations**: Subtle animations on page load and interactions

## 🚀 Quick Start

### Prerequisites
- Node.js 16+ and npm
- PagePulse API running on `http://localhost:8089` (optional - mock data available)

### Installation & Development
```bash
cd pp-frontend
npm install
npm run dev
```

The application will open at `http://localhost:3000` with hot reload enabled.

### Production Build
```bash
npm run build
npm run preview
```

## 🎨 Design System

### Color Palette
```css
/* Primary Colors */
--bg-primary: #0f0f23      /* Deep space blue */
--bg-secondary: #1a1a2e    /* Dark slate */
--bg-tertiary: #16213e     /* Navy blue */
--bg-card: #1e1e3f         /* Card background */

/* Accent Colors */
--accent-primary: #00d4ff   /* Electric cyan */
--accent-secondary: #7c3aed /* Electric purple */
--accent-success: #10b981   /* Success green */
--accent-warning: #f59e0b   /* Warning amber */
--accent-error: #ef4444     /* Error red */

/* Text Colors */
--text-primary: #ffffff     /* Pure white */
--text-secondary: #b8b9c4   /* Light gray */
--text-muted: #8b8ca0      /* Muted gray */
```

### Typography
- **Headers**: Bold weights with gradient text effects
- **Body Text**: Clean, readable fonts with proper line height
- **Code**: Monospace with background highlighting
- **Interactive Text**: Color-coded status indicators

### Effects
- **Box Shadows**: Multiple shadow layers for depth
- **Gradients**: Linear gradients for backgrounds and text
- **Blur Effects**: Backdrop filters for glassmorphism
- **Transitions**: Smooth 0.3s ease transitions
- **Hover States**: Transform and shadow animations

## 🛠️ Development

### File Structure
```
src/
├── App.enhanced.tsx     # Main enhanced dark mode app
├── App.css             # Dark mode styles and animations
├── index.css           # Base dark mode styles
├── main.tsx            # Application entry point
└── types/
    └── api.ts          # TypeScript interface definitions
```

### Key Components
- **App.enhanced.tsx**: Main application with dark theme
- **Metrics Grid**: Dashboard analytics with hover effects
- **Document Cards**: Enhanced document display with animations
- **Navigation**: Smooth tab switching with active states
- **Status Indicators**: Real-time API connection monitoring

### Available Scripts
- `npm run dev` - Start development server with hot reload
- `npm run build` - Build for production with optimizations
- `npm run preview` - Preview production build locally
- `npm run lint` - Run ESLint for code quality

## 🔧 Configuration

### Environment Variables
```env
VITE_API_BASE_URL=http://localhost:8089
VITE_APP_NAME=PagePulse Dashboard
VITE_APP_VERSION=1.0.0
VITE_ENABLE_MOCK_DATA=true
```

### Vite Configuration
- **Proxy Setup**: API requests proxied to backend
- **Environment Variables**: Vite-style env var handling
- **Hot Reload**: Fast development with HMR
- **Build Optimization**: Optimized production builds

## 🌟 Features in Detail

### Interactive Dashboard
- **Animated Counters**: Smooth number transitions
- **Hover Effects**: Transform and glow animations
- **Loading States**: Skeleton loading for better UX
- **Responsive Grid**: Adaptive layout for all screen sizes

### Enhanced Documents View
- **Card Animations**: Slide and scale effects on hover
- **Color-Coded Status**: Visual freshness indicators
- **Smart Typography**: Gradient text and proper hierarchy
- **Interactive Tags**: Hover effects and smooth transitions

### System Monitoring
- **Real-time Status**: Live API connection monitoring
- **Auto-refresh**: Automatic data updates every 30 seconds
- **Error Handling**: Graceful fallbacks and error states
- **Performance**: Optimized rendering and smooth animations

## 📱 Responsive Design

### Breakpoints
- **Mobile**: 480px and below - Single column layout
- **Tablet**: 768px and below - Condensed navigation
- **Desktop**: 1200px+ - Full grid layout with animations

### Adaptive Features
- **Navigation**: Collapsible mobile navigation
- **Cards**: Responsive grid with flexible columns
- **Typography**: Scalable font sizes for readability
- **Touch**: Touch-friendly interactive elements

## 🚀 Performance

### Optimizations
- **CSS Animations**: Hardware-accelerated transforms
- **Bundle Size**: Optimized with Vite's tree shaking
- **Loading States**: Smooth loading transitions
- **Memory**: Efficient React component lifecycle

### Best Practices
- **Code Splitting**: Dynamic imports for larger features
- **Asset Optimization**: Compressed images and fonts
- **Caching**: Proper browser caching headers
- **Accessibility**: ARIA labels and keyboard navigation

## 🔮 Future Enhancements

### Planned Features
- **Theme Switcher**: Toggle between dark/light modes
- **Data Visualization**: Interactive charts and graphs
- **Real-time Updates**: WebSocket integration for live data
- **Advanced Filtering**: Multi-criteria document filtering
- **Export Functions**: PDF and CSV export capabilities

### Technical Improvements
- **Progressive Web App**: Offline functionality and installation
- **Advanced Animations**: Framer Motion integration
- **Testing**: Comprehensive unit and integration tests
- **Performance**: Virtual scrolling for large datasets

## 🎯 Browser Support

- **Chrome**: 90+ ✅
- **Firefox**: 88+ ✅
- **Safari**: 14+ ✅
- **Edge**: 90+ ✅

## 📄 License

Built with ❤️ for the PagePulse project. Dark mode theme with cyberpunk aesthetics.
