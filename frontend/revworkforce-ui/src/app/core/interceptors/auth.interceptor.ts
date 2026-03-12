import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { tap, catchError, timeout, retry } from 'rxjs/operators';
import { throwError, of } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const role = localStorage.getItem('role');
  
  console.log(`[HTTP] ${req.method} ${req.url}`);
  
  let cloned = req;
  
  if (token) {
    cloned = req.clone({
      setHeaders: { 
        Authorization: `Bearer ${token}`,
        'X-User-Id': userId || '',
        'X-User-Role': role || ''
      }
    });
    console.log(`[HTTP] Headers attached - Token, UserId: ${userId}, Role: ${role}`);
  }
  
  const basePipe = next(cloned).pipe(timeout(30000));
  const retriedPipe = req.method === 'GET' ? basePipe.pipe(retry({ count: 1, delay: 1000 })) : basePipe;
  
  return retriedPipe.pipe(
    tap(response => {
      console.log(`[HTTP] ✓ ${req.method} ${req.url} - Success`, response);
    }),
    catchError((error: HttpErrorResponse) => {
      console.error(`[HTTP] ✗ ${req.method} ${req.url} - Error ${error.status}`, error);
      
      if (error.status === 401) {
        console.error('[AUTH] Unauthorized - Clearing session');
        localStorage.clear();
        router.navigate(['/login']);
      } else if (error.status === 403) {
        console.error('[AUTH] Forbidden - Access denied');
        router.navigate(['/unauthorized']);
      } else if (error.status === 404) {
        console.error('[HTTP] Endpoint not found:', req.url);
      } else if (error.status === 500) {
        console.error('[HTTP] Server error:', error.error);
      } else if (error.status === 0) {
        console.error('[HTTP] Network error - Backend may be down');
      }
      
      return throwError(() => ({
        status: error.status,
        message: error.error?.message || error.message || 'Unknown error',
        error: error.error
      }));
    })
  );
};
