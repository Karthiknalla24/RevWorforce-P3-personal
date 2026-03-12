import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.css']
})
export class EmployeesComponent implements OnInit {
  employees = signal<any[]>([]);
  filteredEmployees = signal<any[]>([]);
  departments = signal<any[]>([]);
  designations = signal<any[]>([]);
  managers = signal<any[]>([]);
  isLoading = signal(true);
  showAddModal = signal(false);
  showEditModal = signal(false);
  showFilters = signal(false);
  showManagerModal = signal(false);
  showViewModal = signal(false);
  showPassword = signal(false);
  selectedEmployee: any = null;
  viewEmployee: any = null;
  newManagerId: number | undefined = undefined;
  searchTerm = '';
  
  filters = {
    departmentId: undefined as number | undefined,
    designationId: undefined as number | undefined,
    role: '' as string,
    active: '' as string
  };
  
  newEmployee = {
    firstName: '', lastName: '', email: '', password: '', employeeId: '', 
    departmentId: undefined, designationId: undefined, managerId: undefined,
    phone: '', address: '', joiningDate: '', salary: undefined, role: 'EMPLOYEE' as 'EMPLOYEE' | 'MANAGER' | 'ADMIN'
  };

  editEmployee: any = null;

  constructor(
    private employeeService: EmployeeService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    console.log('Admin Employees component initialized');
    this.loadEmployees();
    this.loadDepartments();
    this.loadDesignations();
    this.loadManagers();
  }

  loadEmployees() {
    console.log('Loading employees');
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        console.log('Employees loaded:', data);
        const employees = Array.isArray(data) ? data : [];
        this.employees.set(employees);
        this.filteredEmployees.set(employees);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading employees:', err);
        this.employees.set([]);
        this.isLoading.set(false);
      }
    });
  }

  loadDepartments() {
    console.log('Loading departments');
    this.employeeService.getDepartments().subscribe({
      next: (data) => {
        console.log('Departments loaded:', data);
        this.departments.set(Array.isArray(data) ? data : []);
      },
      error: (err) => {
        console.error('Error loading departments:', err);
        this.departments.set([]);
      }
    });
  }

  loadDesignations() {
    console.log('Loading designations');
    this.employeeService.getDesignations().subscribe({
      next: (data) => {
        console.log('Designations loaded:', data);
        // Filter out any 'Admin' designation — admins get 'Admin' label by role, not designation
        const filtered = (Array.isArray(data) ? data : [])
          .filter((d: any) => d.title?.toLowerCase() !== 'admin');
        this.designations.set(filtered);
      },
      error: (err) => {
        console.error('Error loading designations:', err);
        this.designations.set([]);
      }
    });
  }

  loadManagers() {
    console.log('Loading managers');
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        const employees = Array.isArray(data) ? data : [];
        const managerList = employees.filter((e: any) => e.role === 'MANAGER' && e.active);
        console.log('Managers loaded:', managerList);
        this.managers.set(managerList);
      },
      error: (err) => {
        console.error('Error loading managers:', err);
        this.managers.set([]);
      }
    });
  }

  onSearch() {
    console.log('Searching with term:', this.searchTerm);
    this.applyFilters();
  }

  applyFilters() {
    let filtered = this.employees();

    const term = this.searchTerm.toLowerCase();
    if (term) {
      filtered = filtered.filter(e =>
        e.name?.toLowerCase().includes(term) ||
        e.email?.toLowerCase().includes(term) ||
        e.employeeId?.toLowerCase().includes(term) ||
        e.departmentName?.toLowerCase().includes(term)
      );
    }

    if (this.filters.departmentId && this.filters.departmentId !== 'undefined' as any) {
      filtered = filtered.filter(e => e.departmentId == this.filters.departmentId);
    }

    if (this.filters.designationId && this.filters.designationId !== 'undefined' as any) {
      filtered = filtered.filter(e => e.designationId == this.filters.designationId);
    }

    if (this.filters.role) {
      filtered = filtered.filter(e => e.role === this.filters.role);
    }

    if (this.filters.active !== '') {
      const isActive = this.filters.active === 'true';
      filtered = filtered.filter(e => e.active === isActive);
    }

    console.log('Filtered employees:', filtered);
    this.filteredEmployees.set(filtered);
  }

  clearFilters() {
    console.log('Clearing filters');
    this.searchTerm = '';
    this.filters = {
      departmentId: undefined,
      designationId: undefined,
      role: '',
      active: ''
    };
    this.applyFilters();
  }

  addEmployee() {
    console.log('Adding employee:', this.newEmployee);
    this.employeeService.addEmployee(this.newEmployee).subscribe({
      next: (res) => {
        console.log('Employee added successfully:', res);
        const roleName = this.newEmployee.role === 'MANAGER' ? 'Manager' : 'Employee';
        this.toastService.success(`${roleName} created successfully`);
        this.showAddModal.set(false);
        this.loadEmployees();
        this.resetForm();
      },
      error: (err) => {
        console.error('Error adding employee:', err);
        this.confirmService.alert({ 
          title: 'Add Employee Failed', 
          message: err.error?.message || 'Unknown error occurred while adding employee',
          type: 'danger'
        });
      }
    });
  }

  async deactivate(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Confirm Deactivation',
      message: 'Are you sure you want to deactivate this employee? They will no longer be able to log in.',
      type: 'danger',
      confirmText: 'Deactivate'
    });
    if (!confirmed) return;
    
    console.log('Deactivating employee:', id);
    this.employeeService.deactivateEmployee(id).subscribe({
      next: () => {
        console.log('Employee deactivated');
        this.toastService.success('Employee deactivated successfully');
        this.loadEmployees();
      },
      error: (err) => {
        console.error('Error deactivating employee:', err);
        this.confirmService.alert({ title: 'Error', message: 'Failed to deactivate employee', type: 'danger' });
      }
    });
  }

  reactivate(id: number) {
    console.log('Reactivating employee:', id);
    this.employeeService.reactivateEmployee(id).subscribe({
      next: () => {
        console.log('Employee reactivated');
        this.toastService.success('Employee reactivated successfully');
        this.loadEmployees();
      },
      error: (err) => {
        console.error('Error reactivating employee:', err);
        this.confirmService.alert({ title: 'Error', message: 'Failed to reactivate employee', type: 'danger' });
      }
    });
  }

  openEditModal(emp: any) {
    console.log('Opening edit modal for employee:', emp.id);
    this.employeeService.getUserById(emp.id).subscribe({
      next: (fullEmp) => {
        let jDate = '';
        if (fullEmp.joiningDate) {
           jDate = new Date(fullEmp.joiningDate).toISOString().split('T')[0];
        } else if (emp.joiningDate) {
           jDate = new Date(emp.joiningDate).toISOString().split('T')[0];
        }

        this.editEmployee = {
          id: fullEmp.id || emp.id,
          firstName: fullEmp.firstName || emp.name?.split(' ')[0] || '',
          lastName: fullEmp.lastName || emp.name?.split(' ').slice(1).join(' ') || '',
          email: fullEmp.email || emp.email,
          employeeId: fullEmp.employeeId || emp.employeeId,
          departmentId: fullEmp.departmentId || emp.departmentId,
          designationId: fullEmp.designationId || emp.designationId,
          managerId: fullEmp.managerId || emp.managerId,
          phone: fullEmp.phone || emp.phone || '',
          address: fullEmp.address || emp.address || '',
          joiningDate: jDate,
          salary: fullEmp.salary || emp.salary,
          role: fullEmp.role || emp.role
        };
        this.showEditModal.set(true);
      },
      error: (err) => {
        console.error('Error fetching full employee details for edit:', err);
        let jDate = '';
        if (emp.joiningDate) jDate = new Date(emp.joiningDate).toISOString().split('T')[0];

        this.editEmployee = {
          id: emp.id,
          firstName: emp.name?.split(' ')[0] || '',
          lastName: emp.name?.split(' ').slice(1).join(' ') || '',
          email: emp.email,
          employeeId: emp.employeeId,
          departmentId: emp.departmentId,
          designationId: emp.designationId,
          managerId: emp.managerId,
          phone: emp.phone || '',
          address: emp.address || '',
          joiningDate: jDate,
          salary: emp.salary,
          role: emp.role
        };
        this.showEditModal.set(true);
      }
    });
  }
  
  openViewModal(emp: any) {
    console.log('Opening view modal for employee:', emp.id);
    this.employeeService.getUserById(emp.id).subscribe({
      next: (fullEmp) => {
        console.log('Full employee details loaded:', fullEmp);
        this.viewEmployee = fullEmp;
        this.showViewModal.set(true);
      },
      error: (err) => {
        console.error('Error fetching full employee details:', err);
        // Fallback to whatever we have in the list item
        this.viewEmployee = emp;
        this.showViewModal.set(true);
      }
    });
  }

  updateEmployee() {
    console.log('Updating employee:', this.editEmployee);
    this.employeeService.updateEmployee(this.editEmployee.id, this.editEmployee).subscribe({
      next: (res) => {
        console.log('Employee updated successfully:', res);
        this.toastService.success('Employee details updated');
        this.showEditModal.set(false);
        this.loadEmployees();
        this.editEmployee = null;
      },
      error: (err) => {
        console.error('Error updating employee:', err);
        this.confirmService.alert({ 
          title: 'Update Failed', 
          message: err.error?.message || 'Unknown error occurred while updating employee',
          type: 'danger'
        });
      }
    });
  }

  openManagerModal(emp: any) {
    console.log('Opening manager modal for employee:', emp);
    this.selectedEmployee = emp;
    this.newManagerId = emp.managerId;
    this.showManagerModal.set(true);
  }

  assignManager() {
    if (!this.newManagerId) {
      this.confirmService.alert({ title: 'Selection Required', message: 'Please select a manager from the list', type: 'warning' });
      return;
    }
    console.log('Assigning manager:', this.newManagerId, 'to employee:', this.selectedEmployee.id);
    this.employeeService.assignManager(this.selectedEmployee.id, this.newManagerId).subscribe({
      next: (res) => {
        console.log('Manager assigned successfully:', res);
        this.toastService.success('Reporting manager updated');
        this.showManagerModal.set(false);
        this.loadEmployees();
        this.selectedEmployee = null;
      },
      error: (err) => {
        console.error('Error assigning manager:', err);
        this.confirmService.alert({ title: 'Error', message: 'Failed to assign manager', type: 'danger' });
      }
    });
  }

  resetForm() {
    this.newEmployee = {
      firstName: '', lastName: '', email: '', password: '', employeeId: '',
      departmentId: undefined, designationId: undefined, managerId: undefined,
      phone: '', address: '', joiningDate: '', salary: undefined, role: 'EMPLOYEE' as 'EMPLOYEE' | 'MANAGER' | 'ADMIN'
    };
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
