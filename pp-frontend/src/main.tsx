/**
 * Main entry point for the PagePulse frontend application.
 * Initializes the React app and renders it to the DOM.
 */
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

// Render the application in StrictMode for additional development checks
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
