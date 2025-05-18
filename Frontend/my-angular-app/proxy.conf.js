const PROXY_CONFIG = {
    "/ws": {
      "target": "http://localhost:8080",
      "secure": false,
      "ws": true,
      "changeOrigin": true,
      "logLevel": "debug"
    },
    "/app": {
      "target": "http://localhost:8080",
      "secure": false,
      "ws": true,
      "changeOrigin": true,
      "logLevel": "debug"
    },
    "/topic": {
      "target": "http://localhost:8080",
      "secure": false,
      "ws": true,
      "changeOrigin": true,
      "logLevel": "debug"
    }
  };
  
  module.exports = PROXY_CONFIG;
  