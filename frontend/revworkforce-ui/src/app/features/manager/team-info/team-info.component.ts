import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-team-info',
  standalone: true,
  imports: [CommonModule, SidebarComponent],
  templateUrl: './team-info.component.html',
  styleUrls: ['./team-info.component.css']
})
export class TeamInfoComponent implements OnInit {
  teamMembers = signal<any[]>([]);
  isLoading = signal(true);
  selectedMember = signal<any>(null);
  memberLeaveBalance = signal<any[]>([]);
  loadingBalance = signal(false);
  showProfileModal = signal(false);
  memberProfile = signal<any>(null);
  loadingProfile = signal(false);

  constructor(
    private employeeService: EmployeeService,
    private leaveService: LeaveService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadTeam();
  }

  loadTeam() {
    this.employeeService.getMyTeam().subscribe({
      next: (data) => {
        this.teamMembers.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.teamMembers.set([]);
        this.isLoading.set(false);
      }
    });
  }

  viewLeaveBalance(member: any) {
    this.selectedMember.set(member);
    this.loadingBalance.set(true);
    this.leaveService.getEmployeeLeaveBalance(member.id).subscribe({
      next: (data) => {
        this.memberLeaveBalance.set(data);
        this.loadingBalance.set(false);
      },
      error: () => {
        this.memberLeaveBalance.set([]);
        this.loadingBalance.set(false);
      }
    });
  }

  viewProfile(member: any) {
    this.showProfileModal.set(true);
    this.loadingProfile.set(true);
    this.employeeService.getUserById(member.id).subscribe({
      next: (data) => {
        this.memberProfile.set(data);
        this.loadingProfile.set(false);
      },
      error: () => {
        this.memberProfile.set(null);
        this.loadingProfile.set(false);
      }
    });
  }

  closeModal() {
    this.selectedMember.set(null);
    this.memberLeaveBalance.set([]);
  }

  closeProfileModal() {
    this.showProfileModal.set(false);
    this.memberProfile.set(null);
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

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
