import { Component, OnInit, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { PerformanceService } from '../../../core/services/performance.service';
import { AnnouncementService } from '../../../core/services/announcement.service';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, SidebarComponent, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  leaveBalances = signal<any[]>([]);
  recentLeaves = signal<any[]>([]);
  isLoading = signal(true);
  today = new Date();
  goals = signal<any[]>([]);
  announcements = signal<any[]>([]);
  notifications = signal<any[]>([]);
  reviewsCount = signal(0);
  error = signal<string | null>(null);

  leaveColors = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6'];

  constructor(
    private leaveService: LeaveService,
    private performanceService: PerformanceService,
    private announcementService: AnnouncementService,
    private notificationService: NotificationService,
    private confirmService: ConfirmService,
    private toast: ToastService,
    public auth: AuthService
  ) {
    effect(() => {
      if (this.auth.currentUser()) {
        this.loadData();
      }
    });
  }

  ngOnInit() {
    console.log('Employee Dashboard initialized');
    if (this.auth.isLoggedIn()) {
      this.loadData();
    }
  }

  loadData() {
    console.log('Loading dashboard data for user:', this.auth.getUserId());
    this.isLoading.set(true);
    this.error.set(null);

    // Load leave balances
    this.leaveService.getMyBalances().subscribe({
      next: (data: any) => {
        console.log('Leave balances loaded:', data);
        const balances = Array.isArray(data) ? data : (data?.data || []);
        this.leaveBalances.set(balances);
      },
      error: (err: any) => {
        console.error('Error loading leave balances:', err);
        this.leaveBalances.set([]);
      }
    });

    // Load my leaves
    this.leaveService.getMyLeaves().subscribe({
      next: (data: any) => {
        console.log('My leaves loaded:', data);
        const leaves = Array.isArray(data) ? data : (data?.data || []);
        this.recentLeaves.set(leaves.slice(0, 5));
      },
      error: (err: any) => {
        console.error('Error loading leaves:', err);
        this.recentLeaves.set([]);
      }
    });

    // Load my goals
    this.performanceService.getMyGoals().subscribe({
      next: (data: any) => {
        console.log('My goals loaded:', data);
        const goals = Array.isArray(data) ? data : (data?.data || []);
        this.goals.set(goals.slice(0, 3));
      },
      error: (err: any) => {
        console.error('Error loading goals:', err);
        this.goals.set([]);
      }
    });

    // Load my reviews
    this.performanceService.getMyReviews().subscribe({
      next: (data: any) => {
        console.log('My reviews loaded:', data);
        const reviews = Array.isArray(data) ? data : (data?.data || []);
        this.reviewsCount.set(reviews.length);
      },
      error: (err: any) => {
        console.error('Error loading reviews:', err);
        this.reviewsCount.set(0);
      }
    });

    // Load announcements
    this.announcementService.getAllAnnouncements().subscribe({
      next: (data: any) => {
        console.log('Announcements loaded:', data);
        const announcements = Array.isArray(data) ? data : (data?.data || []);
        this.announcements.set(announcements.slice(0, 3));
      },
      error: (err: any) => {
        console.error('Error loading announcements:', err);
        this.announcements.set([]);
      }
    });

    // Load notifications
    const userId = this.auth.getUserId();
    if (userId) {
      this.notificationService.getNotificationsByUserId(userId).subscribe({
        next: (data: any) => {
          console.log('Notifications loaded:', data);
          const notifs = Array.isArray(data) ? data : (data?.data || []);
          this.notifications.set(notifs.slice(0, 5));
        },
        error: (err: any) => {
          console.error('Error loading notifications:', err);
          this.notifications.set([]);
        }
      });
    }

    this.isLoading.set(false);
  }

  async cancelLeave(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Cancel Leave',
      message: 'Are you sure you want to cancel this leave application?',
      type: 'warning'
    });

    if (confirmed) {
      console.log('Cancelling leave:', id);
      this.leaveService.cancelLeave(id).subscribe({
        next: () => {
          console.log('Leave cancelled successfully');
          this.recentLeaves.update(l => l.filter(x => x.id !== id));
          this.toast.success('Leave application cancelled successfully');
        },
        error: (err: any) => {
          console.error('Error cancelling leave:', err);
          this.toast.error('Failed to cancel. Try again.');
        }
      });
    }
  }

  getTotalBalance(): number {
    return this.leaveBalances().reduce((s, b) => s + (b.remaining || b.remainingDays || 0), 0);
  }

  getPendingCount(): number {
    return this.recentLeaves().filter(l => l.status === 'PENDING').length;
  }

  getBarWidth(b: any): string {
    const total = b.totalQuota || b.totalDays || 1;
    const remaining = b.remaining || b.remainingDays || 0;
    return total > 0 ? `${(remaining / total) * 100}%` : '0%';
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getGreeting(): string {
    const hour = new Date().getHours();
    if (hour >= 5 && hour < 12) return 'Good Morning';
    if (hour >= 12 && hour < 17) return 'Good Afternoon';
    if (hour >= 17 && hour < 22) return 'Good Evening';
    return 'Good Night';
  }

  isSidebarOpen = false;
  
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
}
