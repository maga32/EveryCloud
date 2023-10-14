import { fileURLToPath, URL } from 'node:url'

import path from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

import { splitVendorChunkPlugin } from 'vite'
import { visualizer } from 'rollup-plugin-visualizer'

export default defineConfig({
  build: {
    outDir: path.resolve(__dirname, '../src/main/resources/static'),
    transpileDependencies: true,
    emptyOutDir: true,
    rollupOptions : {
      output: {
        assetFileNames: (assetInfo) => {
          let extType = assetInfo.name.split(".").at(1);
          if (/png|jpe?g|svg|gif|tiff|bmp|ico/i.test(extType)) {
            extType = "img";
          } else if (/ttf|woff?/i.test(extType)) {
            extType = "fonts";
          }
          return `assets/${extType}/[name].[hash][extname]`;
        },
        chunkFileNames: 'assets/js/[name].[hash].js',
        entryFileNames: 'assets/js/[name].[hash].js',
        manualChunks(id) {
          if (id.includes('vue-sweetalert')) {
            return '@vue-sweetalert'
          }
        },
      },
    }
  },
  plugins: [
    vue(),
    splitVendorChunkPlugin(),
    visualizer({
      filename: "./dist/report.html",
      open: true,
      brotliSize: true,
    }),
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
