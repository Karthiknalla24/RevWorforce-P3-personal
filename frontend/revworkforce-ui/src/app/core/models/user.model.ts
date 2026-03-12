export interface User {
  id: number;
  employeeId: string;
  name: string;
  email: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'ADMIN';
  department: string;
  departmentId?: number;
  designation: string;
  designationId?: number;
  managerId?: number;
  managerName?: string;
  phone?: string;
  address?: string;
  joiningDate?: string;
  salary?: number;
  active: boolean;
  status?: 'ACTIVE' | 'INACTIVE';
}

export interface AuthResponse {
  token: string;
  user: User;
}
