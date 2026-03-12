import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-holidays',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './holidays.component.html',
  styleUrls: ['./holidays.component.css']
})
export class HolidaysComponent implements OnInit {
  holidays = signal<any[]>([]);
  isLoading = signal(true);
  showModal = signal(false);
  currentHoliday: any = { name: '', holidayDate: '' };

  constructor(
    private employeeService: EmployeeService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadHolidays(); }

  loadHolidays() {
    this.employeeService.getHolidays().subscribe({
      next: (data) => { this.holidays.set(data); this.isLoading.set(false); },
      error: () => { this.holidays.set([]); this.isLoading.set(false); }
    });
  }

  openAddModal() {
    this.currentHoliday = { name: '', holidayDate: '' };
    this.showModal.set(true);
  }

  saveHoliday() {
    this.employeeService.addHoliday(this.currentHoliday).subscribe({
      next: () => { 
        this.showModal.set(false); 
        this.toastService.success('Holiday added successfully');
        this.loadHolidays(); 
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to add holiday', type: 'danger' })
    });
  }

  async deleteHoliday(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Holiday',
      message: 'Are you sure you want to delete this holiday?',
      type: 'warning',
      confirmText: 'Delete'
    });
    if (!confirmed) return;
    
    this.employeeService.deleteHoliday(id).subscribe({
      next: () => {
        this.toastService.success('Holiday deleted');
        this.loadHolidays();
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to delete holiday', type: 'danger' })
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' });
  }

  getMonthYear(date: string): string {
    return new Date(date).toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
