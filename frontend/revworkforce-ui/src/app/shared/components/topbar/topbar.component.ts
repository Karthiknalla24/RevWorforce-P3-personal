import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="topbar">
      <div>
        <div class="page-title">{{ title }}</div>
        <div class="page-breadcrumb">{{ breadcrumb }}</div>
      </div>
      <div class="topbar-right">
        <button class="icon-btn" style="background:none;border:none;cursor:pointer;"><i class="bi bi-search"></i></button>
        <button class="icon-btn" style="background:none;border:none;cursor:pointer;"><i class="bi bi-bell"></i><span class="dot"></span></button>
        <div class="topbar-avatar">{{ getInitials(auth.currentUser()?.name) }}</div>
      </div>
    </div>
  `
})
export class TopbarComponent {
  @Input() title = 'Dashboard';
  @Input() breadcrumb = 'Home / Dashboard';
  constructor(public auth: AuthService) {}
  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }
}
