import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { LeaveService } from '../../../core/services/leave.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { User } from '../../../core/models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, SidebarComponent, RouterLink, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  employees = signal<User[]>([]);
  allEmployees = signal<User[]>([]);
  filteredEmployees = signal<User[]>([]);
  isLoading = signal(true);
  searchTerm = '';
  selectedDept = '';
  
  today = new Date();

  stats = signal({ total: 0, active: 0, inactive: 0, onLeave: 0, departments: 0, openReviews: 0 });

  departments = signal<any[]>([]);

  configs = [
    { icon: 'bi-building', color: 'rgba(59,130,246,0.1)', iconColor: 'var(--info)', name: 'Departments', desc: 'Manage company departments', btn: 'Manage', route: '/admin/departments' },
    { icon: 'bi-briefcase', color: 'rgba(16,185,129,0.1)', iconColor: 'var(--success)', name: 'Designations', desc: 'Manage job titles & roles', btn: 'Manage', route: '/admin/designations' },
    { icon: 'bi-megaphone', color: 'rgba(245,158,11,0.1)', iconColor: 'var(--warning)', name: 'Announcements', desc: 'Create & publish updates', btn: 'Manage', route: '/admin/announcements' },
    { icon: 'bi-calendar3', color: 'rgba(139,92,246,0.1)', iconColor: 'var(--purple)', name: 'Holiday Calendar', desc: 'Add/edit company holidays', btn: 'Manage', route: '/admin/holidays' }
  ];

  constructor(
    private employeeService: EmployeeService,
    private leaveService: LeaveService,
    private toast: ToastService,
    private confirmService: ConfirmService,
    private router: Router,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadData(); }

  loadData() {
    this.employeeService.getAllEmployees().subscribe({
      next: d => {
        this.allEmployees.set(d);
        this.employees.set(d.slice(0, 10));
        this.filteredEmployees.set(d.slice(0, 10));
        this.calculateStats(d);
        this.isLoading.set(false);

        // Load departments AFTER employees are set so employee counts per dept are accurate
        this.employeeService.getDepartments().subscribe({
          next: depts => this.updateDepartmentsData(depts),
          error: () => console.error('Failed to load departments for dashboard')
        });
      },
      error: () => { this.employees.set([]); this.filteredEmployees.set([]); this.isLoading.set(false); }
    });
  }

  updateDepartmentsData(allDepts: any[]) {
    const employees = this.allEmployees();
    const deptCounts: any = {};
    employees.forEach(e => {
      if (e.department) {
        deptCounts[e.department] = (deptCounts[e.department] || 0) + 1;
      }
    });

    this.stats.update(s => ({ ...s, departments: allDepts.length }));

    const deptIcons: any = {
      'Engineering': 'bi-code-slash',
      'HR': 'bi-people',
      'Human Resources': 'bi-people',
      'Finance': 'bi-currency-dollar',
      'Marketing': 'bi-graph-up',
      'Operations': 'bi-gear',
      'Sales': 'bi-cart',
      'IT': 'bi-laptop'
    };

    const deptColors: any = {
      'Engineering': { bg: 'rgba(59,130,246,0.1)', text: 'var(--info)' },
      'HR': { bg: 'rgba(236,72,153,0.1)', text: 'var(--pink)' },
      'Human Resources': { bg: 'rgba(236,72,153,0.1)', text: 'var(--pink)' },
      'Finance': { bg: 'rgba(245,158,11,0.1)', text: 'var(--warning)' },
      'Marketing': { bg: 'rgba(20,184,166,0.1)', text: 'var(--teal)' },
      'Operations': { bg: 'rgba(139,92,246,0.1)', text: 'var(--purple)' },
      'Sales': { bg: 'rgba(16,185,129,0.1)', text: 'var(--success)' },
      'IT': { bg: 'rgba(99,102,241,0.1)', text: 'var(--accent)' }
    };

    this.departments.set(
      allDepts.map(d => ({
        name: d.name,
        count: deptCounts[d.name] || 0,
        icon: deptIcons[d.name] || 'bi-building',
        color: deptColors[d.name]?.bg || 'rgba(100,100,100,0.1)',
        textColor: deptColors[d.name]?.text || 'var(--text-main)'
      }))
    );
  }

  calculateStats(employees: any[]) {
    const total = employees.length;
    const active = employees.filter(e => e.active).length;
    const inactive = total - active;
    
    // We'll let updateDepartmentsData handle this.stats().departments 
    // to ensure ALL departments are counted even if empty.
    const currentDeptCount = this.stats().departments;
    
    this.stats.update(s => ({
      ...s,
      total,
      active,
      inactive,
      onLeave: 0,
      openReviews: 0
    }));

    // The overview list (this.departments signal) should ONLY be updated here
    // as a fallback. The updateDepartmentsData method is the primary source.
    if (this.departments().length === 0) {
      const deptCounts: any = {};
      employees.forEach(e => {
        if (e.department) {
          deptCounts[e.department] = (deptCounts[e.department] || 0) + 1;
        }
      });

      const deptIcons: any = {
        'Engineering': 'bi-code-slash',
        'Human Resources': 'bi-people',
        'Finance': 'bi-currency-dollar',
        'Marketing': 'bi-graph-up',
        'Operations': 'bi-gear'
      };

      const deptColors: any = {
        'Engineering': { bg: 'rgba(59,130,246,0.1)', text: 'var(--info)' },
        'Human Resources': { bg: 'rgba(236,72,153,0.1)', text: 'var(--pink)' },
        'Finance': { bg: 'rgba(245,158,11,0.1)', text: 'var(--warning)' },
        'Marketing': { bg: 'rgba(20,184,166,0.1)', text: 'var(--teal)' },
        'Operations': { bg: 'rgba(139,92,246,0.1)', text: 'var(--purple)' }
      };

      this.departments.set(
        Object.entries(deptCounts).map(([name, count]) => ({
          name,
          count,
          icon: deptIcons[name] || 'bi-building',
          color: deptColors[name]?.bg || 'rgba(100,100,100,0.1)',
          textColor: deptColors[name]?.text || 'var(--text-main)'
        }))
      );
    }
  }

  onSearch() {
    // Search from ALL employees, not just the first 10 shown
    let list = this.allEmployees();
    const term = this.searchTerm.toLowerCase().trim();
    if (term) {
      list = list.filter(e =>
        e.name?.toLowerCase().includes(term) ||
        e.email?.toLowerCase().includes(term) ||
        e.employeeId?.toLowerCase().includes(term) ||
        e.department?.toLowerCase().includes(term)
      );
    }
    if (this.selectedDept && this.selectedDept !== '') {
      list = list.filter(e => e.department?.toLowerCase() === this.selectedDept.toLowerCase());
    }
    this.filteredEmployees.set(list);
  }

  navigateToEdit() {
    this.router.navigate(['/admin/employees']);
  }

  async deactivate(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Deactivate Employee',
      message: 'Are you sure you want to deactivate this employee? They will lose access to the system immediately.',
      confirmText: 'Yes, Deactivate',
      type: 'danger'
    });
    if (!confirmed) return;
    
    this.employeeService.deactivateEmployee(id).subscribe({
      next: () => {
        // Update both the display list and the full list
        this.employees.update(list => list.map(e => e.id === id ? { ...e, active: false } : e));
        this.filteredEmployees.update(list => list.map(e => e.id === id ? { ...e, active: false } : e));
        this.allEmployees.update(list => list.map(e => e.id === id ? { ...e, active: false } : e));
        this.calculateStats(this.allEmployees());
        this.toast.success('Employee deactivated successfully');
      },
      error: () => this.toast.error('Failed to deactivate employee')
    });
  }

  reactivate(id: number) {
    this.employeeService.reactivateEmployee(id).subscribe({
      next: () => {
        // Update both the display list and the full list
        this.employees.update(list => list.map(e => e.id === id ? { ...e, active: true } : e));
        this.filteredEmployees.update(list => list.map(e => e.id === id ? { ...e, active: true } : e));
        this.allEmployees.update(list => list.map(e => e.id === id ? { ...e, active: true } : e));
        this.calculateStats(this.allEmployees());
        this.toast.success('Employee reactivated successfully');
      },
      error: () => this.toast.error('Failed to reactivate employee')
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getAvatarColor(i: number): string {
    const colors = ['linear-gradient(135deg,#3b82f6,#1d4ed8)', 'linear-gradient(135deg,#10b981,#059669)',
      'linear-gradient(135deg,#ec4899,#be185d)', 'linear-gradient(135deg,#6b7280,#374151)',
      'linear-gradient(135deg,#f59e0b,#d97706)'];
    return colors[i % colors.length];
  }

  getGreeting(): string {
    const hour = new Date().getHours();
    if (hour >= 5 && hour < 12) return 'Good Morning';
    if (hour >= 12 && hour < 17) return 'Good Afternoon';
    if (hour >= 17 && hour < 22) return 'Good Evening';
    return 'Good Night';
  }
}
