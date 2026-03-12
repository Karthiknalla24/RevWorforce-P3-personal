import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { AuthService } from '../../../core/services/auth.service';
import { LeaveService } from '../../../core/services/leave.service';

interface Holiday {
  id: number;
  name: string;
  date: string;
  day: string;
  type: string;
}

@Component({
  selector: 'app-holiday-calendar',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './holiday-calendar.component.html',
  styleUrl: './holiday-calendar.component.css'
})
export class HolidayCalendarComponent implements OnInit {
  holidays = signal<Holiday[]>([]);
  isLoading = signal(true);

  constructor(public auth: AuthService, private leaveService: LeaveService) {}

  ngOnInit() {
    this.leaveService.getAllHolidays().subscribe({
      next: (data: any[]) => {
        this.holidays.set(data.map(h => ({
          id: h.id,
          name: h.name,
          date: h.holidayDate,
          day: new Date(h.holidayDate).toLocaleDateString('en-US', { weekday: 'long' }),
          type: h.type || 'Holiday'
        })));
        this.isLoading.set(false);
      },
      error: () => {
        this.holidays.set([]);
        this.isLoading.set(false);
      }
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
