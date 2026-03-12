import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { PerformanceService } from '../../../core/services/performance.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-my-reviews',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './my-reviews.component.html',
  styleUrls: ['./my-reviews.component.css']
})
export class MyReviewsComponent implements OnInit {
  reviews = signal<any[]>([]);
  isLoading = signal(true);
  showCreateModal = false;
  isSubmitting = false;
  isProcessingSubmit = false;

  newReview = {
    employeeId: null as number | null,
    year: new Date().getFullYear(),
    deliverables: '',
    accomplishments: '',
    improvements: '',
    selfRating: null as number | null
  };

  constructor(
    private performanceService: PerformanceService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.newReview.employeeId = this.auth.getUserId();
    this.loadReviews();
  }

  loadReviews() {
    this.performanceService.getMyReviews().subscribe({
      next: (data) => {
        this.reviews.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load reviews:', err);
        this.isLoading.set(false);
      }
    });
  }

  createReview() {
    if (!this.newReview.year || !this.newReview.deliverables || !this.newReview.accomplishments || 
        !this.newReview.improvements || !this.newReview.selfRating) {
      this.confirmService.alert({ title: 'Form Incomplete', message: 'Please provide all details for your self-review.' });
      return;
    }
    
    if (this.isSubmitting) return;
    this.isSubmitting = true;
    
    const reviewData = {
      employeeId: this.newReview.employeeId,
      year: this.newReview.year,
      deliverables: this.newReview.deliverables,
      accomplishments: this.newReview.accomplishments,
      improvements: this.newReview.improvements,
      selfRating: this.newReview.selfRating,
      status: 'DRAFT'
    };

    this.performanceService.createReview(reviewData).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.showCreateModal = false;
        this.resetForm();
        this.loadReviews();
        this.toastService.success('Self-review created successfully!');
      },
      error: (err) => {
        this.isSubmitting = false;
        this.confirmService.alert({ title: 'Error', message: err.error?.message || 'Failed to create review', type: 'danger' });
      }
    });
  }

  resetForm() {
    this.newReview = {
      employeeId: this.auth.getUserId(),
      year: new Date().getFullYear(),
      deliverables: '',
      accomplishments: '',
      improvements: '',
      selfRating: null
    };
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getStars(rating: number): string {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  }

  async submitReview(reviewId: number) {
    if (this.isProcessingSubmit) return;
    const confirmed = await this.confirmService.confirm({
      title: 'Submit Review',
      message: 'Are you sure you want to submit this review for manager feedback? Once submitted, you cannot edit it.',
      type: 'info',
      confirmText: 'Yes, Submit'
    });
    if (!confirmed) return;
    
    this.isProcessingSubmit = true;
    
    this.performanceService.submitReview(reviewId).subscribe({
      next: () => {
        this.isProcessingSubmit = false;
        this.loadReviews();
        this.toastService.success('Review submitted successfully!');
      },
      error: (err) => {
        this.isProcessingSubmit = false;
        this.confirmService.alert({ title: 'Error', message: err.error?.message || 'Failed to submit review', type: 'danger' });
      }
    });
  }

  getRole(): 'EMPLOYEE' | 'MANAGER' | 'ADMIN' {
    const role = this.auth.getRole();
    return (role === 'EMPLOYEE' || role === 'MANAGER' || role === 'ADMIN') ? role : 'EMPLOYEE';
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
