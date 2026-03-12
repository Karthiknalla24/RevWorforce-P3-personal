import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-departments',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './departments.component.html',
  styleUrls: ['./departments.component.css']
})
export class DepartmentsComponent implements OnInit {
  departments = signal<any[]>([]);
  isLoading = signal(true);
  showModal = signal(false);
  editMode = signal(false);
  currentDept: any = { id: null, name: '', description: '' };

  constructor(
    private employeeService: EmployeeService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadDepartments(); }

  loadDepartments() {
    this.employeeService.getDepartments().subscribe({
      next: (data) => { this.departments.set(data); this.isLoading.set(false); },
      error: () => { this.departments.set([]); this.isLoading.set(false); }
    });
  }

  openAddModal() {
    this.editMode.set(false);
    this.currentDept = { id: null, name: '', description: '' };
    this.showModal.set(true);
  }

  openEditModal(dept: any) {
    this.editMode.set(true);
    this.currentDept = { ...dept };
    this.showModal.set(true);
  }

  saveDepartment() {
    if (this.editMode()) {
      this.employeeService.updateDepartment(this.currentDept.id, this.currentDept).subscribe({
        next: () => { 
          this.showModal.set(false); 
          this.toastService.success('Department updated successfully');
          this.loadDepartments(); 
        },
        error: (err) => this.confirmService.alert({ title: 'Error', message: err.error?.message || 'Failed to update department', type: 'danger' })
      });
    } else {
      this.employeeService.addDepartment(this.currentDept).subscribe({
        next: () => { 
          this.showModal.set(false); 
          this.toastService.success('Department created successfully');
          this.loadDepartments(); 
        },
        error: (err) => this.confirmService.alert({ title: 'Error', message: err.error?.message || 'Failed to add department', type: 'danger' })
      });
    }
  }

  async deleteDepartment(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Department',
      message: 'Are you sure you want to delete this department? This action cannot be undone.',
      type: 'danger',
      confirmText: 'Delete'
    });
    if (!confirmed) return;
    
    this.employeeService.deleteDepartment(id).subscribe({
        next: () => {
          this.toastService.success('Department deleted');
          this.loadDepartments();
        },
        error: (err) => this.confirmService.alert({ title: 'Restriction', message: err.error?.message || 'Cannot delete department', type: 'warning' })
      });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
