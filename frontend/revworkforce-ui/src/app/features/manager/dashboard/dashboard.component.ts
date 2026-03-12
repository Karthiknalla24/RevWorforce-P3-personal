import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { LeaveApplication } from '../../../core/models/leave.model';
import { User } from '../../../core/models/user.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, SidebarComponent, RouterLink, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  pendingLeaves = signal<LeaveApplication[]>([]);
  teamMembers = signal<User[]>([]);
  isLoading = signal(true);
  today = new Date();
  
  // Feedback Modal
  showFeedbackModal = signal(false);
  selectedLeaveId: number | null = null;
  feedbackAction: 'approve' | 'reject' = 'approve';
  feedbackComment = '';

  constructor(
    private leaveService: LeaveService,
    private employeeService: EmployeeService,
    private toast: ToastService,
    private confirmService: ConfirmService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadData(); }

  loadData() {
    const managerId = this.auth.getUserId();
    this.leaveService.getTeamPendingLeaves(managerId).subscribe({
      next: (d: any[]) => { 
        this.pendingLeaves.set(d.filter((l: any) => l.status === 'PENDING')); 
        this.isLoading.set(false); 
      },
      error: () => { this.pendingLeaves.set([]); this.isLoading.set(false); }
    });
    this.employeeService.getMyTeam().subscribe({
      next: (d: any[]) => this.teamMembers.set(d),
      error: () => this.teamMembers.set([])
    });
  }

  openFeedbackModal(id: number, action: 'approve' | 'reject') {
    this.selectedLeaveId = id;
    this.feedbackAction = action;
    this.feedbackComment = '';
    this.showFeedbackModal.set(true);
  }

  submitFeedback() {
    if (!this.selectedLeaveId) return;

    const payload = {
      managerId: this.auth.getUserId(),
      comment: this.feedbackComment
    };

    if (this.feedbackAction === 'approve') {
      this.leaveService.approveLeave(this.selectedLeaveId, payload).subscribe({
        next: () => {
          this.toast.success('Leave approved successfully');
          this.pendingLeaves.update(l => l.filter(x => x.id !== this.selectedLeaveId));
          this.showFeedbackModal.set(false);
        },
        error: () => this.toast.error('Failed to approve leave')
      });
    } else {
      if (!this.feedbackComment.trim()) {
        this.toast.error('Comment is required for rejection');
        return;
      }
      this.leaveService.rejectLeave(this.selectedLeaveId, payload).subscribe({
        next: () => {
          this.toast.success('Leave rejected successfully');
          this.pendingLeaves.update(l => l.filter(x => x.id !== this.selectedLeaveId));
          this.showFeedbackModal.set(false);
        },
        error: () => this.toast.error('Failed to reject leave')
      });
    }
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getAvatarColor(i: number): string {
    const colors = ['linear-gradient(135deg,#3b82f6,#1d4ed8)', 'linear-gradient(135deg,#10b981,#059669)',
      'linear-gradient(135deg,#8b5cf6,#6d28d9)', 'linear-gradient(135deg,#f59e0b,#d97706)',
      'linear-gradient(135deg,#ec4899,#be185d)', 'linear-gradient(135deg,#14b8a6,#0d9488)'];
    return colors[i % colors.length];
  }

  getStars(rating: number): string { return '★'.repeat(rating) + '☆'.repeat(5 - rating); }

  getGreeting(): string {
    const hour = new Date().getHours();
    if (hour >= 5 && hour < 12) return 'Good Morning';
    if (hour >= 12 && hour < 17) return 'Good Afternoon';
    if (hour >= 17 && hour < 22) return 'Good Evening';
    return 'Good Night';
  }
}
