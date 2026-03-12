// API Configuration for Microservices Architecture
export const API_CONFIG = {
  // API Gateway (single entry point for all microservices)
  GATEWAY_URL: 'http://localhost:8080',
  
  // Auth Service endpoints
  AUTH: {
    LOGIN: '/api/auth/login',
    REGISTER: '/api/auth/register'
  },
  
  // User Service endpoints
  USERS: {
    BASE: '/api/users',
    ME: '/api/users/me',
    PROFILE: '/api/users/me',
    CHANGE_PASSWORD: '/api/users/me/change-password',
    GET_BY_ID: (id: number) => `/api/users/${id}`,
    UPDATE: (id: number) => `/api/users/${id}`,
    DIRECTORY: '/api/users/directory',
    MANAGERS: '/api/users/managers',
    SEARCH: '/api/users/search',
    FILTER: '/api/users/filter',
    MANAGER: (id: number) => `/api/users/${id}/manager`,
    TEAM: (managerId: number) => `/api/users/manager/${managerId}/team`,
    ASSIGN_MANAGER: '/api/users/assign-manager',
    DEACTIVATE: (id: number) => `/api/users/${id}/deactivate`,
    REACTIVATE: (id: number) => `/api/users/${id}/reactivate`,
    COUNT_BY_DEPT: (deptId: number) => `/api/users/department/${deptId}/count`,
    COUNT_BY_DESIG: (desigId: number) => `/api/users/designation/${desigId}/count`,
    BY_DEPARTMENT: (deptId: number) => `/api/users/department/${deptId}/users`
  },
  
  // Employee Management Service endpoints
  DEPARTMENTS: {
    BASE: '/api/departments',
    GET_BY_ID: (id: number) => `/api/departments/${id}`,
    UPDATE: (id: number) => `/api/departments/${id}`,
    DELETE: (id: number) => `/api/departments/${id}`
  },
  
  DESIGNATIONS: {
    BASE: '/api/designations',
    GET_BY_ID: (id: number) => `/api/designations/${id}`,
    UPDATE: (id: number) => `/api/designations/${id}`,
    DELETE: (id: number) => `/api/designations/${id}`
  },
  
  ANNOUNCEMENTS: {
    BASE: '/api/announcements',
    GET_BY_ID: (id: number) => `/api/announcements/${id}`,
    UPDATE: (id: number) => `/api/announcements/${id}`,
    DELETE: (id: number) => `/api/announcements/${id}`
  },
  
  // Leave Service endpoints
  LEAVES: {
    TYPES: '/api/leaves/types',
    DELETE_TYPE: (id: number) => `/api/leaves/types/${id}`,
    APPLY: '/api/leaves/apply',
    MY_LEAVES: (userId: number) => `/api/leaves/user/${userId}`,
    TEAM_LEAVES: (managerId: number) => `/api/leaves/manager/${managerId}/team`,
    APPROVE: (id: number) => `/api/leaves/${id}/approve`,
    REJECT: (id: number) => `/api/leaves/${id}/reject`,
    CANCEL: (id: number) => `/api/leaves/${id}/cancel`,
    BALANCE: (userId: number) => `/api/leaves/balance/${userId}`,
    ASSIGN_BALANCE: '/api/leaves/balance',
    ALL_LEAVES: '/api/leaves/all',
    ALL_BALANCES: '/api/leaves/all-balances',
    DEPT_REPORT: (deptId: number) => `/api/leaves/report/department/${deptId}`,
    EMP_REPORT: (empId: number) => `/api/leaves/report/employee/${empId}`,
    HOLIDAYS: '/api/leaves/holidays',
    DELETE_HOLIDAY: (id: number) => `/api/leaves/holidays/${id}`
  },
  
  // Notification Service endpoints
  NOTIFICATIONS: {
    BASE: '/api/notifications',
    BY_USER: (userId: number) => `/api/notifications/user/${userId}`,
    UNREAD_COUNT: (userId: number) => `/api/notifications/user/${userId}/unread-count`,
    MARK_READ: (id: number) => `/api/notifications/${id}/read`,
    MARK_ALL_READ: (userId: number) => `/api/notifications/user/${userId}/read-all`,
    CLEAR: (userId: number) => `/api/notifications/user/${userId}/clear`
  },
  
  // Performance Service endpoints
  PERFORMANCE: {
    REVIEWS: '/api/performance/reviews',
    REVIEW_BY_ID: (id: number) => `/api/performance/reviews/${id}`,
    MY_REVIEWS: (userId: number) => `/api/performance/reviews/user/${userId}`,
    TEAM_REVIEWS: (managerId: number) => `/api/performance/reviews/manager/${managerId}/team`,
    SUBMIT_REVIEW: (id: number) => `/api/performance/reviews/${id}/submit`,
    FEEDBACK: (id: number) => `/api/performance/reviews/${id}/feedback`,
    GOALS: '/api/performance/goals',
    GOAL_BY_ID: (id: number) => `/api/performance/goals/${id}`,
    MY_GOALS: (userId: number) => `/api/performance/goals/user/${userId}`,
    UPDATE_PROGRESS: (id: number) => `/api/performance/goals/${id}/progress`,
    ADD_COMMENT: (id: number) => `/api/performance/goals/${id}/comment`,
    DELETE_GOAL: (id: number) => `/api/performance/goals/${id}`
  },
  
  // Reporting Service endpoints
  REPORTS: {
    DASHBOARD: '/api/reports/dashboard',
    LEAVE_REPORT: (userId: number) => `/api/reports/leave/${userId}`,
    PERFORMANCE_REPORT: (userId: number) => `/api/reports/performance/${userId}`,
    EMPLOYEE_REPORT: (userId: number) => `/api/reports/employee/${userId}`,
    DEPARTMENT_REPORT: (deptId: number) => `/api/reports/department/${deptId}`,
    ACTIVITY_LOGS: '/api/activity',
    ACTIVITY_BY_USER: (userId: number) => `/api/activity/${userId}`
  }
};

export function getApiUrl(endpoint: string): string {
  return `${API_CONFIG.GATEWAY_URL}${endpoint}`;
}
