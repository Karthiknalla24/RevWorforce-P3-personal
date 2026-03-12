import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-designations',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './designations.component.html',
  styleUrls: ['./designations.component.css']
})
export class DesignationsComponent implements OnInit {
  designations = signal<any[]>([]);
  isLoading = signal(true);
  showModal = signal(false);
  editMode = signal(false);
  currentDesig: any = { id: null, title: '', description: '' };

  constructor(
    private employeeService: EmployeeService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadDesignations(); }

  loadDesignations() {
    this.employeeService.getDesignations().subscribe({
      next: (data) => { this.designations.set(data); this.isLoading.set(false); },
      error: () => { this.designations.set([]); this.isLoading.set(false); }
    });
  }

  openAddModal() {
    this.editMode.set(false);
    this.currentDesig = { id: null, title: '', description: '' };
    this.showModal.set(true);
  }

  openEditModal(desig: any) {
    this.editMode.set(true);
    this.currentDesig = { ...desig };
    this.showModal.set(true);
  }

  saveDesignation() {
    if (this.editMode()) {
      this.employeeService.updateDesignation(this.currentDesig.id, this.currentDesig).subscribe({
        next: () => { 
          this.showModal.set(false); 
          this.toastService.success('Designation updated successfully');
          this.loadDesignations(); 
        },
        error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to update designation', type: 'danger' })
      });
    } else {
      this.employeeService.addDesignation(this.currentDesig).subscribe({
        next: () => { 
          this.showModal.set(false); 
          this.toastService.success('Designation added successfully');
          this.loadDesignations(); 
        },
        error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to add designation', type: 'danger' })
      });
    }
  }

  async deleteDesignation(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Designation',
      message: 'Are you sure you want to delete this designation? This action cannot be undone.',
      type: 'danger',
      confirmText: 'Delete'
    });
    if (!confirmed) return;
    
    this.employeeService.deleteDesignation(id).subscribe({
      next: () => {
        this.toastService.success('Designation deleted');
        this.loadDesignations();
      },
      error: () => this.confirmService.alert({ title: 'Restriction', message: 'Cannot delete designation with existing employees', type: 'warning' })
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
