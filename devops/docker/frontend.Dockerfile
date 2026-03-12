# Stage 1: Build the Angular application
FROM node:18-alpine as build
WORKDIR /app
COPY frontend/revworkforce-ui/package*.json ./
RUN npm install
COPY frontend/revworkforce-ui/ .
RUN npm run build --configuration=production

# Stage 2: Serve the application with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/revworkforce-frontend/browser /usr/share/nginx/html
COPY frontend/revworkforce-ui/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
