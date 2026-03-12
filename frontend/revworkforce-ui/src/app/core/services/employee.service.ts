import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_CONFIG, getApiUrl } from '../config/api.config';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  getMyProfile() { 
    console.log('Fetching my profile');
    return this.http.get<any>(getApiUrl(API_CONFIG.USERS.ME)).pipe(
      map(data => this.processUser(data))
    ); 
  }

  updateMyProfile(data: any) { 
    console.log('Updating my profile:', data);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.PROFILE), data); 
  }

  changePassword(data: any) { 
    console.log('Changing password');
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.CHANGE_PASSWORD), data); 
  }

  changeMyPassword(currentPassword: string, newPassword: string) {
    console.log('Changing my password');
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.CHANGE_PASSWORD), {
      currentPassword,
      newPassword
    });
  }
  
  getAllEmployees() { 
    console.log('Fetching all employees');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.DIRECTORY)).pipe(
      map(employees => employees.map(emp => this.processUser(emp)))
    ); 
  }

  getEmployeeDirectory() { 
    console.log('Fetching employee directory');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.DIRECTORY)).pipe(
      map(employees => employees.map(emp => this.processUser(emp)))
    ); 
  }

  getManagers() {
    console.log('Fetching managers');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.MANAGERS));
  }

  getUserById(id: number) { 
    console.log('Fetching user:', id);
    return this.http.get<any>(getApiUrl(API_CONFIG.USERS.GET_BY_ID(id))).pipe(
      map(data => this.processUser(data))
    ); 
  }

  private processUser(data: any) {
    if (data && Array.isArray(data.joiningDate) && data.joiningDate.length >= 3) {
      data.joiningDate = new Date(data.joiningDate[0], data.joiningDate[1] - 1, data.joiningDate[2]).toISOString();
    }
    return data;
  }

  addEmployee(data: any) { 
    console.log('Adding employee:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.USERS.BASE), data); 
  }

  createUser(data: any) { 
    console.log('Creating user:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.USERS.BASE), data); 
  }

  updateEmployee(id: number, data: any) { 
    console.log('Updating employee:', id, data);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.UPDATE(id)), data); 
  }

  updateUser(id: number, data: any) { 
    console.log('Updating user:', id, data);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.UPDATE(id)), data); 
  }
  
  deactivateEmployee(id: number) { 
    console.log('Deactivating employee:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.DEACTIVATE(id)), {}); 
  }

  deactivateUser(id: number) { 
    console.log('Deactivating user:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.DEACTIVATE(id)), {}); 
  }

  reactivateEmployee(id: number) { 
    console.log('Reactivating employee:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.REACTIVATE(id)), {}); 
  }

  reactivateUser(id: number) { 
    console.log('Reactivating user:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.REACTIVATE(id)), {}); 
  }
  
  assignManager(userId: number, managerId: number) { 
    console.log('Assigning manager:', userId, 'to', managerId);
    return this.http.put<any>(getApiUrl(API_CONFIG.USERS.ASSIGN_MANAGER), { userId, managerId }); 
  }
  
  searchUsers(query: any) { 
    console.log('Searching users:', query);
    return this.http.post<any[]>(getApiUrl(API_CONFIG.USERS.SEARCH), query); 
  }

  filterUsers(params: any) { 
    console.log('Filtering users:', params);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.FILTER), { params }); 
  }

  getManagerOfUser(userId: number) { 
    console.log('Fetching manager of user:', userId);
    return this.http.get<any>(getApiUrl(API_CONFIG.USERS.MANAGER(userId))); 
  }

  getTeamMembers(managerId: number) { 
    console.log('Fetching team members for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.TEAM(managerId))); 
  }

  getMyTeam() {
    const managerId = this.auth.getUserId();
    console.log('Fetching my team for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.USERS.TEAM(managerId)));
  }
  
  getDepartments() { 
    console.log('Fetching departments');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.DEPARTMENTS.BASE)); 
  }

  getDepartmentById(id: number) { 
    console.log('Fetching department:', id);
    return this.http.get<any>(getApiUrl(API_CONFIG.DEPARTMENTS.GET_BY_ID(id))); 
  }

  createDepartment(data: any) { 
    console.log('Creating department:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.DEPARTMENTS.BASE), data); 
  }

  addDepartment(data: any) {
    console.log('Adding department:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.DEPARTMENTS.BASE), data);
  }

  updateDepartment(id: number, data: any) { 
    console.log('Updating department:', id, data);
    return this.http.put<any>(getApiUrl(API_CONFIG.DEPARTMENTS.UPDATE(id)), data); 
  }

  deleteDepartment(id: number) { 
    console.log('Deleting department:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.DEPARTMENTS.DELETE(id))); 
  }
  
  getDesignations() { 
    console.log('Fetching designations');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.DESIGNATIONS.BASE)); 
  }

  getDesignationById(id: number) { 
    console.log('Fetching designation:', id);
    return this.http.get<any>(getApiUrl(API_CONFIG.DESIGNATIONS.GET_BY_ID(id))); 
  }

  createDesignation(data: any) { 
    console.log('Creating designation:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.DESIGNATIONS.BASE), data); 
  }

  addDesignation(data: any) {
    console.log('Adding designation:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.DESIGNATIONS.BASE), data);
  }

  updateDesignation(id: number, data: any) { 
    console.log('Updating designation:', id, data);
    return this.http.put<any>(getApiUrl(API_CONFIG.DESIGNATIONS.UPDATE(id)), data); 
  }

  deleteDesignation(id: number) { 
    console.log('Deleting designation:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.DESIGNATIONS.DELETE(id))); 
  }
  
  getAnnouncements() { 
    console.log('Fetching announcements');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.BASE)); 
  }

  getAnnouncementById(id: number) { 
    console.log('Fetching announcement:', id);
    return this.http.get<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.GET_BY_ID(id))); 
  }

  createAnnouncement(data: any) { 
    console.log('Creating announcement:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.BASE), data); 
  }

  updateAnnouncement(id: number, data: any) { 
    console.log('Updating announcement:', id, data);
    return this.http.put<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.UPDATE(id)), data); 
  }

  deleteAnnouncement(id: number) { 
    console.log('Deleting announcement:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.DELETE(id))); 
  }

  getHolidays() {
    console.log('Fetching holidays');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.LEAVES.HOLIDAYS));
  }

  addHoliday(data: any) {
    console.log('Adding holiday:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.LEAVES.HOLIDAYS), data);
  }

  deleteHoliday(id: number) {
    console.log('Deleting holiday:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.LEAVES.DELETE_HOLIDAY(id)));
  }
}
