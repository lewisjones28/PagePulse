import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    'process.env': {},
  },
  server: {
    port: 3000,
    host: true,
    proxy: {
      // Proxy API requests to Spring Boot backend
      '/api': {
        target: 'http://localhost:8089',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req) => {
            console.log(`Proxying ${req.method} ${req.url} to ${options.target}`);
          });
        },
      },
      // Direct proxy for documents endpoint
      '/documents': {
        target: 'http://localhost:8089',
        changeOrigin: true,
      },
      // Direct proxy for actuator health
      '/actuator': {
        target: 'http://localhost:8089',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom'],
          charts: ['recharts'],
          icons: ['lucide-react'],
        },
      },
    },
  },
})
