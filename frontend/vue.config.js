const { defineConfig } = require('@vue/cli-service')

const path = require("path");
const CompressionPlugin = require('compression-webpack-plugin')
// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

module.exports = defineConfig({
  outputDir: path.resolve(__dirname, '../src/main/resources/static'),
  transpileDependencies: true,
  configureWebpack: {
    output: {
      filename: 'js/[name].[contenthash].js',
      chunkFilename: 'js/[name].[contenthash].chunk.js',
    },
    plugins: [
      // new BundleAnalyzerPlugin(),
      new CompressionPlugin({
        algorithm: "gzip",
        threshold: 10240,
        minRatio: 0.8,
      }),
    ]
  },
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:11024',
        changeOrigin: true
      }
    }
  },
})
