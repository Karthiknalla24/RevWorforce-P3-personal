export interface LeaveBalance {
  leaveTypeId?: number;
  leaveTypeName: string;
  totalQuota: number;
  used: number;
  remaining: number;
}

export interface LeaveApplication {
  id: number;
  employeeId?: number;
  employeeName?: string;
  leaveType: string;
  startDate: string;
  endDate: string;
  days: number;
  reason: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';
  managerComment?: string;
  appliedDate?: string;
}

export interface Goal {
  id: number;
  title: string;
  description: string;
  deadline: string;
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  progress: number;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'NOT_STARTED';
}

export interface Notification {
  id: number;
  message: string;
  isRead: boolean;
  createdAt: string;
  type: 'LEAVE' | 'PERFORMANCE' | 'ANNOUNCEMENT';
}

export interface Announcement {
  id: number;
  title: string;
  description: string;
  createdAt: string;
  type: 'INFO' | 'WARNING' | 'SUCCESS';
}
