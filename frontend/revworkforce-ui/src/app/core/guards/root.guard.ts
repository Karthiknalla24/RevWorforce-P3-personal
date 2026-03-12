import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const rootGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isLoggedIn()) {
    const role = auth.getRole();
    const map: Record<string, string> = { EMPLOYEE: '/employee', MANAGER: '/manager', ADMIN: '/admin' };
    router.navigate([map[role] || '/login']);
    return false;
  }

  router.navigate(['/login']);
  return false;
};
