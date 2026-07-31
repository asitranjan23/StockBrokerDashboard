import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: 'window', // ✅ Fix for SockJS expecting Node 'global'
  },
  server: {
    port: 5173,
    open: true,
  },
})
