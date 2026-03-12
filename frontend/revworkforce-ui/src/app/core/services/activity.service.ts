import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { API_CONFIG, getApiUrl } from '../config/api.config';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ActivityService {
  constructor(private http: HttpClient) {}

  getAllActivities() {
    return this.http.get<any>(`${getApiUrl(API_CONFIG.REPORTS.ACTIVITY_LOGS)}/all`)
      .pipe(map((res: any) => res.data || res));
  }

  getPaginatedActivities(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<any>(getApiUrl(API_CONFIG.REPORTS.ACTIVITY_LOGS), { params });
  }

  getUserActivities(userId: number, page: number = 0, size: number = 20) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(getApiUrl(API_CONFIG.REPORTS.ACTIVITY_BY_USER(userId)), { params });
  }

  cleanupLogs(duration: string): Observable<any> {
    const params = new HttpParams().set('duration', duration);
    return this.http.delete<any>(`${getApiUrl(API_CONFIG.REPORTS.ACTIVITY_LOGS)}/cleanup`, { params });
  }
}
