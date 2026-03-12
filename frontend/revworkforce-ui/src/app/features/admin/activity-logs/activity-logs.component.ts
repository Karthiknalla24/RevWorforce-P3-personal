import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { ActivityService } from '../../../core/services/activity.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-activity-logs',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './activity-logs.component.html',
  styleUrls: ['./activity-logs.component.css']
})
export class ActivityLogsComponent implements OnInit {
  activities = signal<any[]>([]);
  isLoading = signal(true);
  searchTerm = '';
  
  // Pagination
  currentPage = signal(0);
  pageSize = signal(20);
  totalPages = signal(0);
  totalElements = signal(0);

  // Cleanup
  cleanupDuration = '24h';
  isCleaning = signal(false);

  constructor(
    private activityService: ActivityService,
    public auth: AuthService,
    private confirmService: ConfirmService
  ) {}

  ngOnInit() {
    this.loadActivities();
  }

  loadActivities() {
    this.isLoading.set(true);
    this.activityService.getPaginatedActivities(this.currentPage(), this.pageSize()).subscribe({
      next: (data) => {
        this.activities.set(data.content || []);
        this.totalPages.set(data.totalPages || 0);
        this.totalElements.set(data.totalElements || 0);
        this.isLoading.set(false);
      },
      error: () => {
        this.activities.set([]);
        this.isLoading.set(false);
      }
    });
  }

  changePage(newPage: number) {
    if (newPage >= 0 && newPage < this.totalPages()) {
      this.currentPage.set(newPage);
      this.loadActivities();
    }
  }

  onSearch() {
    if (!this.searchTerm) {
      this.loadActivities();
    } else {
      const lower = this.searchTerm.toLowerCase();
      this.activities.set(
        this.activities().filter(a =>
          a.action?.toLowerCase().includes(lower) ||
          a.userName?.toLowerCase().includes(lower) ||
          a.userRole?.toLowerCase().includes(lower)
        )
      );
    }
  }

  async performCleanup() {
    const durationLabel: Record<string, string> = {
      '24h': 'the last 24 hours',
      'week': 'the last week',
      'month': 'the last month'
    };
    const label = durationLabel[this.cleanupDuration] || this.cleanupDuration;

    const confirmed = await this.confirmService.confirm({
      title: 'Delete Activity Logs',
      message: `Are you sure you want to permanently delete all logs older than ${label}? This action cannot be undone.`,
      confirmText: 'Delete Logs',
      cancelText: 'Cancel',
      type: 'danger'
    });

    if (!confirmed) return;

    this.isCleaning.set(true);
    this.activityService.cleanupLogs(this.cleanupDuration).subscribe({
      next: () => {
        this.isCleaning.set(false);
        this.currentPage.set(0);
        this.loadActivities();
        this.confirmService.alert({
          title: 'Cleanup Successful',
          message: `All activity logs older than ${label} have been deleted successfully.`,
          type: 'success'
        });
      },
      error: () => {
        this.isCleaning.set(false);
        this.confirmService.alert({
          title: 'Cleanup Failed',
          message: 'An error occurred while trying to delete the logs. Please try again.',
          type: 'danger'
        });
      }
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
