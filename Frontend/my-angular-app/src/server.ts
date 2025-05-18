import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';

import express from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware'; // ✅ Add proxy middleware
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import dotenv from 'dotenv';
import mongoose from 'mongoose';

// Load environment variables from .env file
dotenv.config();

// Resolve paths
const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');

const app = express();
const angularApp = new AngularNodeAppEngine();

// Parse incoming JSON
app.use(express.json());

// MongoDB connection
const mongoUri = process.env['MONGO_URI'] || 'mongodb://localhost:27017/budget-monitor';
mongoose
  .connect(mongoUri)
  .then(() => {
    console.log('✅ Connected to MongoDB');
  })
  .catch((err) => {
    console.error('❌ MongoDB connection error:', err);
  });

// --- 🔥 WebSocket Proxy for /ws ---
app.use('/ws', createProxyMiddleware({
  target: 'http://localhost:8080', // Your Spring Boot backend
  changeOrigin: true,
  ws: true,
}));

// Serve static Angular files
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  })
);

// Render Angular app for any other route
app.use('/**', (req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next()
    )
    .catch(next);
});

// Start server if this is the main module
if (isMainModule(import.meta.url)) {
  const port = parseInt(process.env['PORT'] || '4000', 10);
  app.listen(port, () => {
    console.log(`🚀 Server is running at http://localhost:${port}`);
  });
}

// Export handler for Angular CLI or serverless
export const reqHandler = createNodeRequestHandler(app);
