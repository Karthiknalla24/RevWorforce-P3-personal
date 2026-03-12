import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';
import { LeaveApplication } from '../../../core/models/leave.model';

@Component({
  selector: 'app-my-leaves',
  standalone: true,
  imports: [CommonModule, SidebarComponent, RouterLink],
  templateUrl: './my-leaves.component.html',
  styleUrl: './my-leaves.component.css'
})
export class MyLeavesComponent implements OnInit {
  leaves = signal<LeaveApplication[]>([]);
  isLoading = signal(true);

  constructor(
    private leaveService: LeaveService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadLeaves();
  }

  loadLeaves() {
    this.leaveService.getMyLeaves().subscribe({
      next: (data) => {
        this.leaves.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.leaves.set([]);
        this.isLoading.set(false);
      }
    });
  }

  async cancelLeave(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Cancel Leave',
      message: 'Are you sure you want to cancel this leave application?',
      type: 'warning',
      confirmText: 'Yes, Cancel'
    });
    if (!confirmed) return;
    
    this.leaveService.cancelLeave(id).subscribe({
      next: () => {
        this.toastService.success('Leave cancelled successfully');
        this.leaves.update(list => list.filter(l => l.id !== id));
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to cancel leave', type: 'danger' })
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getRole(): 'EMPLOYEE' | 'MANAGER' | 'ADMIN' {
    const role = this.auth.getRole();
    return (role === 'EMPLOYEE' || role === 'MANAGER' || role === 'ADMIN') ? role : 'EMPLOYEE';
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
