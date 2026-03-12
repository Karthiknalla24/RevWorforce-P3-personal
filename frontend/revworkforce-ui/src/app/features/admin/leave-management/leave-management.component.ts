import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-leave-management',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './leave-management.component.html',
  styleUrls: ['./leave-management.component.css']
})
export class LeaveManagementComponent implements OnInit {
  leaves = signal<any[]>([]);
  filteredLeaves = signal<any[]>([]);
  isLoading = signal(true);
  searchTerm = signal('');
  filterStatus = signal('ALL');

  constructor(
    private leaveService: LeaveService,
    public auth: AuthService
  ) {}

  get pendingCount(): number {
    return this.leaves().filter(l => l.status === 'PENDING').length;
  }

  get approvedCount(): number {
    return this.leaves().filter(l => l.status === 'APPROVED').length;
  }

  get rejectedCount(): number {
    return this.leaves().filter(l => l.status === 'REJECTED').length;
  }

  get totalCount(): number {
    return this.leaves().length;
  }

  ngOnInit() {
    this.loadAllLeaves();
  }

  loadAllLeaves() {
    this.leaveService.getAllLeaves().subscribe({
      next: (data) => {
        this.leaves.set(data);
        this.filteredLeaves.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.leaves.set([]);
        this.filteredLeaves.set([]);
        this.isLoading.set(false);
      }
    });
  }

  onSearch(term: string) {
    this.searchTerm.set(term);
    this.applyFilters();
  }

  onFilterStatus(status: string) {
    this.filterStatus.set(status);
    this.applyFilters();
  }

  applyFilters() {
    let filtered = this.leaves();
    
    if (this.searchTerm()) {
      const lower = this.searchTerm().toLowerCase();
      filtered = filtered.filter(l => 
        l.employeeName?.toLowerCase().includes(lower) ||
        l.leaveType?.toLowerCase().includes(lower)
      );
    }
    
    if (this.filterStatus() !== 'ALL') {
      filtered = filtered.filter(l => l.status === this.filterStatus());
    }
    
    this.filteredLeaves.set(filtered);
  }

  getStatusClass(status: string): string {
    return {
      'PENDING': 'status-pending',
      'APPROVED': 'status-approved',
      'REJECTED': 'status-rejected',
      'CANCELLED': 'status-cancelled'
    }[status] || '';
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
