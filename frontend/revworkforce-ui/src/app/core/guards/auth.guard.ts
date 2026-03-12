import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRole = route.data?.['role'];
  if (requiredRole && auth.getRole() !== requiredRole) {
    // redirect to correct dashboard
    const role = auth.getRole();
    const map: Record<string, string> = { EMPLOYEE: '/employee', MANAGER: '/manager', ADMIN: '/admin' };
    router.navigate([map[role] || '/login']);
    return false;
  }

  return true;
};
