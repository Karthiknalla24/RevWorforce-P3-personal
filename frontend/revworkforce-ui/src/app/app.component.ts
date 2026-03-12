import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { ToastService } from './core/services/toast.service';
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ConfirmDialogComponent],
  template: `
    <router-outlet />
    <app-confirm-dialog />
    
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 9999">
      @for (toast of toastService.toasts(); track toast) {
        <div class="toast show animate-in" [ngClass]="'toast-' + toast.type" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="d-flex">
            <div class="toast-body">
              {{ toast.message }}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" (click)="remove(toast)"></button>
          </div>
        </div>
      }
    </div>

    <style>
      .toast { 
        background: #333; 
        color: white; 
        min-width: 250px; 
        margin-bottom: 10px; 
        border: none;
        border-left: 5px solid #666;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      }
      .toast-success { border-left-color: #10b981; }
      .toast-error { border-left-color: #ef4444; }
      .animate-in { animation: slideIn 0.3s ease-out; }
      @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
      }
    </style>
  `
})
export class AppComponent {
  toastService = inject(ToastService);
  remove(toast: any) { this.toastService.toasts.update(t => t.filter(x => x !== toast)); }
}
