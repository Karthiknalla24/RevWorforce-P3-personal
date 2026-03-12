import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { PerformanceService } from '../../../core/services/performance.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-team-goals',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './team-goals.component.html',
  styleUrls: ['./team-goals.component.css']
})
export class TeamGoalsComponent implements OnInit {
  goals = signal<any[]>([]);
  isLoading = signal(true);
  showCommentModal = signal(false);
  selectedGoal: any = null;
  comment = '';

  constructor(
    private performanceService: PerformanceService,
    private toast: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadTeamGoals();
  }

  loadTeamGoals() {
    const managerId = this.auth.getUserId();
    this.performanceService.getTeamGoals(managerId).subscribe({
      next: (data: any[]) => {
        this.goals.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.goals.set([]);
        this.isLoading.set(false);
      }
    });
  }

  openCommentModal(goal: any) {
    this.selectedGoal = goal;
    this.comment = goal.managerComment || '';
    this.showCommentModal.set(true);
  }

  submitComment() {
    if (!this.comment.trim()) {
      this.toast.error('Please enter a comment');
      return;
    }

    this.performanceService.addGoalComment(this.selectedGoal.id, this.comment).subscribe({
      next: () => {
        this.showCommentModal.set(false);
        this.loadTeamGoals();
        this.toast.success('Comment added successfully!');
      },
      error: () => this.toast.error('Failed to add comment')
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getStatusClass(status: string): string {
    switch(status) {
      case 'COMPLETED': return 'badge-completed';
      case 'IN_PROGRESS': return 'badge-progress';
      case 'NOT_STARTED': return 'badge-not-started';
      default: return '';
    }
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
