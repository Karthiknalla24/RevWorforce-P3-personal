import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  selectedRole = signal<'EMPLOYEE' | 'MANAGER' | 'ADMIN'>('EMPLOYEE');
  showPassword = signal(false);
  isLoading = signal(false);
  errorMsg = signal('');

  credentials = { identifier: '', password: '' };

  constructor(private auth: AuthService, private router: Router) {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('user');
  }

  setRole(role: 'EMPLOYEE' | 'MANAGER' | 'ADMIN') {
    this.selectedRole.set(role);
    this.errorMsg.set('');
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  onSubmit() {
    if (!this.credentials.identifier || !this.credentials.password) {
      this.errorMsg.set('Please fill in all fields.');
      return;
    }

    this.isLoading.set(true);
    this.errorMsg.set('');

    console.log('Attempting login with:', this.credentials.identifier);

    this.auth.login({ ...this.credentials }).subscribe({
      next: (res: any) => {
        console.log('Login successful:', res);
        this.isLoading.set(false);
        const userRole = res.role;
        const selectedTab = this.selectedRole();

        if (selectedTab === 'MANAGER' && userRole !== 'MANAGER') {
          this.errorMsg.set('Only Managers can login through Manager tab');
          return;
        }
        if (selectedTab === 'ADMIN' && userRole !== 'ADMIN') {
          this.errorMsg.set('Only Admins can login through Admin tab');
          return;
        }

        const map: Record<string, string> = {
          EMPLOYEE: '/employee',
          MANAGER: '/manager',
          ADMIN: '/admin'
        };

        console.log('Navigating to:', map[userRole]);
        this.router.navigate([map[userRole] || '/login']);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.isLoading.set(false);
        this.errorMsg.set(err.error?.message || 'Invalid Login Credentials');
      }
    });
  }
}
