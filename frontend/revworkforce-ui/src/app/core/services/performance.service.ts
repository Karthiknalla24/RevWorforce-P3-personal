import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_CONFIG, getApiUrl } from '../config/api.config';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class PerformanceService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  // Goals
  getMyGoals() { 
    const userId = this.auth.getUserId();
    console.log('Fetching my goals for user:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.PERFORMANCE.MY_GOALS(userId))); 
  }

  createGoal(data: any) { 
    console.log('Creating goal:', data);
    const goalData = {
      userId: data.employeeId || data.userId,
      title: data.title,
      description: data.description,
      deadline: data.deadline,
      priority: data.priority
    };
    return this.http.post<any>(getApiUrl(API_CONFIG.PERFORMANCE.GOALS), goalData); 
  }

  updateGoalProgress(id: number, progress: number) { 
    console.log('Updating goal progress:', id, progress);
    return this.http.put<any>(getApiUrl(API_CONFIG.PERFORMANCE.UPDATE_PROGRESS(id)), { progress }); 
  }

  deleteGoal(id: number) { 
    console.log('Deleting goal:', id);
    return this.http.delete<any>(getApiUrl(API_CONFIG.PERFORMANCE.DELETE_GOAL(id))); 
  }

  // Reviews
  getMyReviews() { 
    const userId = this.auth.getUserId();
    console.log('Fetching my reviews for user:', userId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.PERFORMANCE.MY_REVIEWS(userId))); 
  }

  createReview(data: any) { 
    console.log('Creating review:', data);
    const reviewData = {
      userId: data.employeeId || data.userId,
      year: data.year,
      deliverables: data.deliverables,
      accomplishments: data.accomplishments,
      improvements: data.improvements,
      selfRating: data.selfRating
    };
    return this.http.post<any>(getApiUrl(API_CONFIG.PERFORMANCE.REVIEWS), reviewData); 
  }

  submitReview(reviewId: number) { 
    console.log('Submitting review:', reviewId);
    return this.http.put<any>(getApiUrl(API_CONFIG.PERFORMANCE.SUBMIT_REVIEW(reviewId)), {}); 
  }

  // Manager
  getTeamGoals(managerId: number) { 
    console.log('Fetching team goals for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.PERFORMANCE.GOALS)); 
  }

  addGoalComment(goalId: number, comment: string) { 
    console.log('Adding goal comment:', goalId, comment);
    return this.http.put<any>(getApiUrl(API_CONFIG.PERFORMANCE.ADD_COMMENT(goalId)), { comment }); 
  }

  getTeamReviews(managerId: number) { 
    console.log('Fetching team reviews for manager:', managerId);
    return this.http.get<any[]>(getApiUrl(API_CONFIG.PERFORMANCE.TEAM_REVIEWS(managerId))); 
  }

  provideFeedback(id: number, data: any) { 
    console.log('Providing feedback for review:', id);
    return this.http.put<any>(getApiUrl(API_CONFIG.PERFORMANCE.FEEDBACK(id)), data); 
  }
}
