import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (confirmService.isOpen()) {
      <div class="modal-overlay" (click)="onCancel()" style="z-index: 9999;">
        <div class="modal-container" (click)="$event.stopPropagation()" style="max-width: 450px;">
          <div class="modal-header">
            <h3 class="modal-title d-flex align-items-center gap-2">
              @if (confirmService.options()?.type === 'danger') {
                <i class="bi bi-exclamation-triangle-fill text-danger"></i>
              } @else if (confirmService.options()?.type === 'warning') {
                <i class="bi bi-exclamation-circle-fill text-warning"></i>
              } @else if (confirmService.options()?.type === 'success') {
                <i class="bi bi-check-circle-fill text-success"></i>
              } @else {
                <i class="bi bi-info-circle-fill text-primary"></i>
              }
              {{ confirmService.options()?.title }}
            </h3>
            <button type="button" (click)="onCancel()" class="modal-close">&times;</button>
          </div>
          <div class="modal-body">
            <p style="font-size: 1rem; color: var(--text-muted); line-height: 1.5; margin: 0;">
              {{ confirmService.options()?.message }}
            </p>
          </div>
          <div class="modal-footer" style="padding: 1.25rem;">
            @if (confirmService.options()?.cancelText) {
              <button type="button" class="btn-secondary-sm" (click)="onCancel()">
                {{ confirmService.options()?.cancelText }}
              </button>
            }
            <button type="button" 
                    [class]="getButtonClass()"
                    (click)="onConfirm()">
              {{ confirmService.options()?.confirmText }}
            </button>
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    .modal-overlay {
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0,0,0,0.5);
      backdrop-filter: blur(4px);
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .modal-container {
      background: white;
      border-radius: 12px;
      padding: 0;
      width: 90%;
      box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1), 0 10px 10px -5px rgba(0,0,0,0.04);
      animation: modalSlide 0.3s ease-out;
    }
    @keyframes modalSlide {
      from { transform: translateY(20px); opacity: 0; }
      to { transform: translateY(0); opacity: 1; }
    }
    .modal-header {
      padding: 1.25rem;
      border-bottom: 1px solid var(--border);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .modal-title { margin: 0; font-size: 1.25rem; font-weight: 700; color: #111827; }
    .modal-close {
      background: none; border: none; font-size: 1.5rem; color: #6b7280; cursor: pointer;
    }
    .modal-body { padding: 1.25rem; }
    .modal-footer {
      padding: 1rem 1.25rem;
      border-top: 1px solid var(--border);
      display: flex;
      justify-content: flex-end;
      gap: 0.75rem;
      background: #f9fafb;
      border-bottom-left-radius: 12px;
      border-bottom-right-radius: 12px;
    }
    .btn-danger { background: #ef4444; color: white !important; }
    .btn-danger:hover { background: #dc2626; }
    .btn-warning { background: #f59e0b; color: white !important; }
    .btn-warning:hover { background: #d97706; }
    .btn-success { background: #10b981; color: white !important; }
    .btn-success:hover { background: #059669; }
    .btn-primary { background: #3b82f6; color: white !important; }
    .btn-primary:hover { background: #2563eb; }
  `]
})
export class ConfirmDialogComponent {
  constructor(public confirmService: ConfirmService) {}

  onConfirm() {
    this.confirmService.handleResponse(true);
  }

  onCancel() {
    this.confirmService.handleResponse(false);
  }

  getButtonClass() {
    const type = this.confirmService.options()?.type;
    switch(type) {
      case 'danger': return 'btn-primary-sm btn-danger';
      case 'warning': return 'btn-primary-sm btn-warning';
      case 'success': return 'btn-primary-sm btn-success';
      default: return 'btn-primary-sm';
    }
  }
}
