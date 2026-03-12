import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_CONFIG, getApiUrl } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class AnnouncementService {
  constructor(private http: HttpClient) {}

  getAllAnnouncements() { 
    console.log('Fetching all announcements');
    return this.http.get<any[]>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.BASE)); 
  }

  getAnnouncementById(id: number) { 
    console.log('Fetching announcement:', id);
    return this.http.get<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.GET_BY_ID(id))); 
  }

  createAnnouncement(data: any) { 
    console.log('Creating announcement:', data);
    return this.http.post<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.BASE), data); 
  }

  updateAnnouncement(id: number, data: any) { 
    console.log('Updating announcement:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.UPDATE(id)), data); 
  }

  deleteAnnouncement(id: number) { 
    console.log('Deleting announcement:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.ANNOUNCEMENTS.DELETE(id))); 
  }
}
