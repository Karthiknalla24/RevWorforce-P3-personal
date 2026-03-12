import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_CONFIG, getApiUrl } from '../config/api.config';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class LeaveService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  getLeaveTypes() { 
    console.log('Fetching leave types');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.TYPES)); 
  }
  
  createLeaveType(data: any) { 
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.TYPES), data); 
  }
  
  deleteLeaveType(id: number) { 
    return this.http.delete<any>(getApiUrl(API_CONFIG.LEAVES.DELETE_TYPE(id))); 
  }

  applyLeave(data: {userId: number, leaveTypeId: number, startDate: string, endDate: string, reason: string}) {
    console.log('Applying leave:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.APPLY), data);
  }

  getMyLeaves() { 
    const userId = this.auth.getUserId();
    console.log('Fetching leaves for user:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.MY_LEAVES(userId))); 
  }

  getTeamLeaves(managerId: number) { 
    console.log('Fetching team leaves for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.TEAM_LEAVES(managerId))); 
  }

  approveLeave(id: number, data: any) { 
    console.log('Approving leave:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.LEAVES.APPROVE(id)), data); 
  }
  
  rejectLeave(id: number, data: any) { 
    console.log('Rejecting leave:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.LEAVES.REJECT(id)), data); 
  }
  
  cancelLeave(id: number) { 
    const userId = this.auth.getUserId();
    console.log('Cancelling leave:', id, 'for user:', userId);
    return this.http.put<any>(getApiUrl(API_CONFIG.LEAVES.CANCEL(id)), {}, { params: { userId } }); 
  }

  getLeaveBalance(userId: number) { 
    console.log('Fetching leave balance for user:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.BALANCE(userId))); 
  }

  getMyBalances() {
    const userId = this.auth.getUserId();
    console.log('Fetching my leave balances:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.BALANCE(userId)));
  }

  assignLeaveBalance(data: any) { 
    console.log('Assigning leave balance:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.ASSIGN_BALANCE), data); 
  }

  getHolidays() { 
    console.log('Fetching holidays');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.HOLIDAYS)); 
  }

  getAllHolidays() {
    console.log('Fetching all holidays');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.HOLIDAYS));
  }
  
  createHoliday(data: any) { 
    console.log('Creating holiday:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.HOLIDAYS), data); 
  }
  
  deleteHoliday(id: number) { 
    console.log('Deleting holiday:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.LEAVES.DELETE_HOLIDAY(id))); 
  }

  // Additional methods for manager/admin features
  getTeamPendingLeaves(managerId: number) {
    console.log('Fetching pending leaves for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.TEAM_LEAVES(managerId)));
  }

  getTeamCalendar(managerId: number) {
    console.log('Fetching team calendar for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.TEAM_LEAVES(managerId)));
  }

  getEmployeeLeaveBalance(userId: number) {
    console.log('Fetching leave balance for employee:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.BALANCE(userId)));
  }

  getAllLeaves() {
    console.log('Fetching all leaves');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.ALL_LEAVES));
  }

  getAllLeaveBalances() {
    console.log('Fetching all leave balances');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.ALL_BALANCES));
  }

  adjustLeaveBalance(userId: number, leaveTypeId: number, adjustment: number, reason?: string) {
    console.log('Adjusting leave balance:', userId, leaveTypeId, adjustment);
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.ASSIGN_BALANCE), {
      userId,
      leaveTypeId,
      adjustment,
      reason
    });
  }

  getDepartmentWiseReport() {
    console.log('Fetching department wise report');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.DEPT_REPORT(0)));
  }

  getEmployeeWiseReport() {
    console.log('Fetching employee wise report');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.EMP_REPORT(0)));
  }

  getAllEmployees() {
    console.log('Fetching all employees from leave service');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.DIRECTORY));
  }
}
