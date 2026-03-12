import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { PerformanceService } from '../../../core/services/performance.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-my-goals',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './my-goals.component.html',
  styleUrls: ['./my-goals.component.css']
})
export class MyGoalsComponent implements OnInit {
  goals = signal<any[]>([]);
  isLoading = signal(true);
  showAddForm = signal(false);
  newGoal = { title: '', description: '', deadline: '', priority: 'MEDIUM' };

  constructor(
    private performanceService: PerformanceService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadGoals();
  }

  loadGoals() {
    this.performanceService.getMyGoals().subscribe({
      next: (data) => {
        this.goals.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.goals.set([]);
        this.isLoading.set(false);
      }
    });
  }

  addGoal() {
    if (!this.newGoal.title || !this.newGoal.deadline) {
      this.confirmService.alert({ 
        title: 'Form Incomplete', 
        message: 'Please provide both a title and a deadline for your new goal.',
        type: 'warning'
      });
      return;
    }
    
    const goalData = {
      employeeId: this.auth.getUserId(),
      title: this.newGoal.title,
      description: this.newGoal.description,
      deadline: this.newGoal.deadline,
      priority: this.newGoal.priority,
      status: 'ACTIVE',
      progress: 0
    };

    this.performanceService.createGoal(goalData).subscribe({
      next: () => {
        this.showAddForm.set(false);
        this.newGoal = { title: '', description: '', deadline: '', priority: 'MEDIUM' };
        this.loadGoals();
        this.toastService.success('Goal created successfully!');
      },
      error: (err) => {
        this.confirmService.alert({ 
          title: 'Error', 
          message: err.error?.message || 'Failed to create goal', 
          type: 'danger' 
        });
      }
    });
  }

  updateProgress(id: number, progress: number) {
    this.performanceService.updateGoalProgress(id, progress).subscribe({
      next: () => {
        this.toastService.success('Progress updated');
        this.goals.update(list => list.map(g => g.id === id ? {...g, progress} : g));
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to update progress', type: 'danger' })
    });
  }

  async deleteGoal(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Goal',
      message: 'Are you sure you want to delete this goal? This action is permanent.',
      type: 'danger',
      confirmText: 'Delete'
    });
    if (!confirmed) return;
    
    this.performanceService.deleteGoal(id).subscribe({
      next: () => {
        this.toastService.success('Goal deleted');
        this.goals.update(list => list.filter(g => g.id !== id));
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to delete goal', type: 'danger' })
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getProgressColor(progress: number): string {
    if (progress >= 75) return '#10b981';
    if (progress >= 50) return '#3b82f6';
    if (progress >= 25) return '#f59e0b';
    return '#ef4444';
  }

  getRole(): 'EMPLOYEE' | 'MANAGER' | 'ADMIN' {
    const role = this.auth.getRole();
    return (role === 'EMPLOYEE' || role === 'MANAGER' || role === 'ADMIN') ? role : 'EMPLOYEE';
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
