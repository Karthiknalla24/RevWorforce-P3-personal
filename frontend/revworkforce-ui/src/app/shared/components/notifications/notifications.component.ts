import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  notifications = signal<any[]>([]);
  isLoading = signal(true);

  constructor(
    private notificationService: NotificationService,
    private toast: ToastService,
    private confirmService: ConfirmService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.notificationService.getMyNotifications().subscribe({
      next: (data) => {
        this.notifications.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.notifications.set([]);
        this.isLoading.set(false);
      }
    });
  }

  markAsRead(id: number) {
    this.notificationService.markAsRead(id).subscribe({
      next: () => this.notifications.update(list => list.map(n => n.id === id ? {...n, readStatus: true} : n)),
      error: () => {}
    });
  }

  markAllAsRead() {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.update(list => list.map(n => ({...n, readStatus: true})));
        this.toast.success('All notifications marked as read');
      },
      error: () => this.toast.error('Failed to mark all as read')
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

  async clearNotifications() {
    const confirmed = await this.confirmService.confirm({
      title: 'Clear Notifications',
      message: 'Are you sure you want to clear all notifications? This action cannot be undone.',
      type: 'warning',
      confirmText: 'Clear All'
    });
    if (!confirmed) return;
    
    this.notificationService.clearNotifications().subscribe({
      next: () => {
        this.notifications.set([]);
        this.toast.success('Notifications cleared');
      },
      error: () => this.toast.error('Failed to clear notifications')
    });
  }
}
