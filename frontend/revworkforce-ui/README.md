# RevWorkforce Frontend - Microservices Edition

This is the Angular frontend for RevWorkforce microservices architecture (P3).

## Setup Instructions

### Prerequisites
- Node.js 18+
- Angular CLI 18+

### Installation

1. Navigate to frontend directory:
```bash
cd RevWorkForce-P3/frontend/revworkforce-ui
```

2. Install dependencies:
```bash
npm install
```

### Running the Application

Start the development server:
```bash
npm start
```

The application will be available at `http://localhost:4200`

### API Configuration

The frontend is configured to connect to the microservices API Gateway at `http://localhost:8080`

All API endpoints are centralized in: `src/app/core/config/api.config.ts`

### Test Credentials

- **Admin**: admin@revature.com / admin123
- **Manager**: manager@revature.com / admin123
- **Employee**: employee@revature.com / admin123

### Building for Production

```bash
npm run build
```

Output will be in `dist/revworkforce-frontend`

## Architecture

- **API Gateway**: http://localhost:8080
- **User Service**: Handles authentication and user management
- **Employee Management Service**: Departments, designations, announcements
- **Leave Service**: Leave applications, approvals, balance management
- **Notification Service**: Real-time notifications
- **Performance Service**: Reviews and goals
- **Reporting Service**: Dashboards and reports

## Features

- Role-based access control (Admin, Manager, Employee)
- Leave management workflow
- Performance reviews and goals
- Employee directory
- Real-time notifications
- Comprehensive reporting

## Services Updated for Microservices

- `auth.service.ts` - JWT authentication with API Gateway
- `employee.service.ts` - User and employee management
- `leave.service.ts` - Leave management
- `notification.service.ts` - Notifications
- `performance.service.ts` - Performance reviews and goals
- `activity.service.ts` - Reports and analytics

All services use the centralized API configuration for consistent endpoint management.
