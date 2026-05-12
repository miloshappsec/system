import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      // All /api requests → api-gateway (auth, admin, SSRF, challenges)
      '/api': 'http://localhost:8080',
      // All /data requests → data-service directly (bypass gateway — intentional)
      '/data': 'http://localhost:8082',
    },
  },
})
