import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class DataService {
  constructor(private http: HttpClient) {}

  /**
   * Handle API response - extract data if wrapped in ApiResponse format
   */
  private handleResponse<T>(response: any): T {
    if (response && typeof response === 'object') {
      if ('data' in response && 'status' in response && 'message' in response) {
        return response.data as T;
      }
    }
    return response as T;
  }

  /**
   * GET request with proper response handling
   */
  get<T>(url: string, options?: any): Observable<T> {
    return this.http.get<any>(url, options).pipe(
      map(response => this.handleResponse<T>(response)),
      tap(data => console.log('[DATA] GET Success:', data)),
      catchError(error => {
        console.error('[DATA] GET Error:', error);
        return of([] as any);
      })
    );
  }

  /**
   * POST request with proper response handling
   */
  post<T>(url: string, body: any, options?: any): Observable<T> {
    return this.http.post<any>(url, body, options).pipe(
      map(response => this.handleResponse<T>(response)),
      tap(data => console.log('[DATA] POST Success:', data)),
      catchError(error => {
        console.error('[DATA] POST Error:', error);
        throw error;
      })
    );
  }

  /**
   * PUT request with proper response handling
   */
  put<T>(url: string, body: any, options?: any): Observable<T> {
    return this.http.put<any>(url, body, options).pipe(
      map(response => this.handleResponse<T>(response)),
      tap(data => console.log('[DATA] PUT Success:', data)),
      catchError(error => {
        console.error('[DATA] PUT Error:', error);
        throw error;
      })
    );
  }

  /**
   * DELETE request with proper response handling
   */
  delete<T>(url: string, options?: any): Observable<T> {
    return this.http.delete<any>(url, options).pipe(
      map(response => this.handleResponse<T>(response)),
      tap(data => console.log('[DATA] DELETE Success:', data)),
      catchError(error => {
        console.error('[DATA] DELETE Error:', error);
        throw error;
      })
    );
  }

  /**
   * Ensure response is array
   */
  ensureArray<T>(data: any): T[] {
    if (Array.isArray(data)) return data;
    if (data && typeof data === 'object' && 'data' in data && Array.isArray(data.data)) {
      return data.data;
    }
    return [];
  }

  /**
   * Ensure response is object
   */
  ensureObject<T>(data: any): T {
    if (data && typeof data === 'object' && 'data' in data) {
      return data.data as T;
    }
    return data as T;
  }
}
