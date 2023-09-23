import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: true,
    port: '8080',
    proxy: {
      '/api': {
        target: 'http://localhost:11024',
        changeOrigin: true
      }
    }
  },
  watch : {
    usePolling : true
  }
})
