import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-team-leaves',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './team-leaves.component.html',
  styleUrls: ['./team-leaves.component.css']
})
export class TeamLeavesComponent implements OnInit {
  leaves = signal<any[]>([]);
  isLoading = signal(true);
  showFeedbackModal = signal(false);
  selectedLeave: any = null;
  feedback = { comments: '', action: '' };

  constructor(
    private leaveService: LeaveService,
    private toast: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadTeamLeaves();
  }

  loadTeamLeaves() {
    const managerId = this.auth.getUserId();
    this.leaveService.getTeamLeaves(managerId).subscribe({
      next: (data: any[]) => {
        this.leaves.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.leaves.set([]);
        this.isLoading.set(false);
      }
    });
  }

  openFeedbackModal(leave: any, action: string) {
    this.selectedLeave = leave;
    this.feedback.action = action;
    this.feedback.comments = '';
    this.showFeedbackModal.set(true);
  }

  submitFeedback() {
    const payload = {
      managerId: this.auth.getUserId(),
      comment: this.feedback.comments
    };

    if (this.feedback.action === 'approve') {
      this.leaveService.approveLeave(this.selectedLeave.id, payload).subscribe({
        next: () => {
          this.toast.success('Leave application approved');
          this.showFeedbackModal.set(false);
          this.loadTeamLeaves();
        },
        error: () => this.toast.error('Failed to approve leave')
      });
    } else {
      if (!this.feedback.comments.trim()) {
        this.toast.error('Comments are mandatory for rejection');
        return;
      }
      this.leaveService.rejectLeave(this.selectedLeave.id, payload).subscribe({
        next: () => {
          this.toast.success('Leave application rejected');
          this.showFeedbackModal.set(false);
          this.loadTeamLeaves();
        },
        error: () => this.toast.error('Failed to reject leave')
      });
    }
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
