import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Input() role: 'EMPLOYEE' | 'MANAGER' | 'ADMIN' = 'EMPLOYEE';
  @Input() isOpen = false;
  
  constructor(public auth: AuthService) {}

  ngOnInit() {
    // If role is not explicitly provided or is default, try to get from AuthService
    if (this.role === 'EMPLOYEE') {
      const authRole = this.auth.getRole();
      if (authRole === 'ADMIN' || authRole === 'MANAGER') {
        this.role = authRole as any;
      }
    }
  }
  
  logout() { this.auth.logout(); }
  
  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }
}
