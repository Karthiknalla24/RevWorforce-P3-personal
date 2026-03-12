import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { EmployeeService } from '../../../core/services/employee.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile = signal<any>(null);
  isLoading = signal(true);
  isEditing = signal(false);
  showPasswordSection = signal(false);
  showManagerModal = signal(false);
  isChangingPassword = signal(false);
  isSaving = signal(false);
  editData: any = {};
  passwordData = { currentPassword: '', newPassword: '', confirmPassword: '' };
  successMsg = signal('');
  errorMsg = signal('');
  passwordError = signal('');
  
  // Password Visibility
  showCurrentPassword = signal(false);
  showNewPassword = signal(false);
  showConfirmPassword = signal(false);

  constructor(
    private employeeService: EmployeeService,
    private toast: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    const userId = this.auth.getUserId();
    if (!userId) {
      const stored = localStorage.getItem('user');
      const user = stored ? JSON.parse(stored) : null;
      this.profile.set(user);
      this.editData = {...user};
      this.isLoading.set(false);
      return;
    }

    this.employeeService.getMyProfile().subscribe({
      next: (data) => {
        this.profile.set(data);
        this.editData = {...data};
        this.isLoading.set(false);
      },
      error: () => {
        const stored = localStorage.getItem('user');
        const user = stored ? JSON.parse(stored) : this.auth.currentUser();
        this.profile.set(user);
        this.editData = {...user};
        this.isLoading.set(false);
      }
    });
  }

  toggleEdit() {
    this.isEditing.update(v => !v);
    if (!this.isEditing()) {
      this.editData = {...this.profile()};
    }
  }

  togglePasswordSection() {
    this.showPasswordSection.update(v => !v);
    // Reset visibility when toggling section
    this.showCurrentPassword.set(false);
    this.showNewPassword.set(false);
    this.showConfirmPassword.set(false);
    this.passwordData = { currentPassword: '', newPassword: '', confirmPassword: '' };
    this.passwordError.set('');
  }

  saveProfile() {
    this.successMsg.set('');
    this.errorMsg.set('');
    this.isSaving.set(true);

    const updateData = {
      firstName: this.editData.firstName,
      lastName: this.editData.lastName,
      email: this.editData.email,
      phone: this.editData.phone,
      address: this.editData.address,
      city: this.editData.city,
      state: this.editData.state,
      zipCode: this.editData.zipCode,
      emergencyContact: this.editData.emergencyContact
    };

    this.employeeService.updateMyProfile(updateData).subscribe({
      next: (data) => {
        this.profile.set(data);
        this.editData = {...data};
        this.isEditing.set(false);
        this.isSaving.set(false);
        this.toast.success('Profile updated successfully!');
      },
      error: (err) => {
        this.isSaving.set(false);
        this.toast.error(err.error?.message || 'Failed to update profile');
      }
    });
  }

  changePassword() {
    this.passwordError.set('');
    this.successMsg.set('');

    if (!this.passwordData.currentPassword || !this.passwordData.newPassword || !this.passwordData.confirmPassword) {
      this.passwordError.set('All password fields are required');
      return;
    }

    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.passwordError.set('New passwords do not match');
      return;
    }

    if (this.passwordData.currentPassword === this.passwordData.newPassword) {
      this.passwordError.set('New password cannot be the same as current password');
      return;
    }

    if (this.passwordData.newPassword.length < 6) {
      this.passwordError.set('Password must be at least 6 characters');
      return;
    }

    this.isChangingPassword.set(true);

    this.employeeService.changeMyPassword(this.passwordData.currentPassword, this.passwordData.newPassword).subscribe({
      next: () => {
        this.isChangingPassword.set(false);
        this.toast.success('Password changed successfully!');
        this.showPasswordSection.set(false);
        this.passwordData = { currentPassword: '', newPassword: '', confirmPassword: '' };
        this.showCurrentPassword.set(false);
        this.showNewPassword.set(false);
        this.showConfirmPassword.set(false);
      },
      error: (err) => {
        this.isChangingPassword.set(false);
        const msg = err.error?.message || 'Failed to change password';
        this.passwordError.set(msg);  // show inline in the form
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
  
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
}
