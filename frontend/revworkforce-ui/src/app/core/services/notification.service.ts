import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { API_CONFIG, getApiUrl } from '../config/api.config';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  getMyNotifications() { 
    const userId = this.auth.getUserId();
    return this.http.get<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.BY_USER(userId))).pipe(
      map((res: any) => res.data || res)
    ); 
  }

  getNotificationsByUserId(userId: number) {
    return this.http.get<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.BY_USER(userId))).pipe(
      map((res: any) => res.data || res)
    );
  }

  getUnreadCount(userId?: number) {
    const id = userId || this.auth.getUserId();
    return this.http.get<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.UNREAD_COUNT(id))).pipe(
      map((res: any) => res.data || res)
    );
  }

  markAsRead(id: number, userId?: number) { 
    const uid = userId || this.auth.getUserId();
    return this.http.put<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.MARK_READ(id)), {}, { params: { userId: uid.toString() } }).pipe(
      map((res: any) => res.data || res)
    ); 
  }

  markAllAsRead(userId?: number) { 
    const uid = userId || this.auth.getUserId();
    return this.http.put<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.MARK_ALL_READ(uid)), {}).pipe(
      map((res: any) => res.data || res)
    ); 
  }

  clearNotifications(userId?: number) {
    const uid = userId || this.auth.getUserId();
    return this.http.delete<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.CLEAR(uid))).pipe(
      map((res: any) => res.data || res)
    );
  }

  createNotification(data: any) {
    return this.http.post<any>(getApiUrl(API_CONFIG.NOTIFICATIONS.BASE), data).pipe(
      map((res: any) => res.data || res)
    );
  }
}
