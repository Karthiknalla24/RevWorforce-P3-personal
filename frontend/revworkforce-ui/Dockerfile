# Stage 1: Build the Angular application
FROM node:20-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

# Stage 2: Serve the application with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/revworkforce-frontend/browser /usr/share/nginx/html
# Note: In Angular 17+, the build output might be in dist/[project-name]/browser
# Based on angular.json, outputPath is dist/revworkforce-frontend
# If SSR is enabled, it might be slightly different. 
# Let's adjust based on typical Angular 18 output structure.
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
