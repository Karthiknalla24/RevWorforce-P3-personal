import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-team-calendar',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './team-calendar.component.html',
  styleUrls: ['./team-calendar.component.css']
})
export class TeamCalendarComponent implements OnInit {
  events = signal<any[]>([]);
  isLoading = signal(true);

  constructor(
    private leaveService: LeaveService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadTeamCalendar();
  }

  loadTeamCalendar() {
    const managerId = this.auth.getUserId();
    this.leaveService.getTeamCalendar(managerId).subscribe({
      next: (data: any[]) => {
        this.events.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.events.set([]);
        this.isLoading.set(false);
      }
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getEventColor(type: string): string {
    if (type === 'HOLIDAY') return '#10b981';
    if (type === 'APPROVED') return '#3b82f6';
    if (type === 'PENDING') return '#f59e0b';
    return '#6b7280';
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
