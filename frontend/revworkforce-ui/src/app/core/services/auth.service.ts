import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { API_CONFIG, getApiUrl } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class AuthService {
  currentUser = signal<any | null>(null);

  constructor(private http: HttpClient, private router: Router) {
    const stored = localStorage.getItem('user');
    if (stored) {
      try {
        this.currentUser.set(JSON.parse(stored));
      } catch {}
    }
  }

  login(credentials: { identifier: string; password: string }) {
    return this.http.post<any>(getApiUrl(API_CONFIG.AUTH.LOGIN), credentials).pipe(
      tap(response => {
        console.log('Login response:', response);
        localStorage.setItem('token', response.token);
        localStorage.setItem('userId', String(response.userId));
        localStorage.setItem('role', response.role);
        localStorage.setItem('email', response.email);
        localStorage.setItem('employeeId', response.employeeId);
        localStorage.setItem('user', JSON.stringify(response));
        this.currentUser.set(response);
        console.log('User logged in:', response.userId, response.role);
      })
    );
  }

  logout() {
    localStorage.clear();
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRole(): string {
    return localStorage.getItem('role') || '';
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserId(): number {
    return parseInt(localStorage.getItem('userId') || '0');
  }
}
