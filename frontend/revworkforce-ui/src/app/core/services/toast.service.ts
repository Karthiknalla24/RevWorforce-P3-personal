import { Injectable, signal } from '@angular/core';

export interface Toast {
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts = signal<Toast[]>([]);

  show(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'success') {
    const toast: Toast = { message, type };
    this.toasts.update(t => [...t, toast]);
    setTimeout(() => {
      this.toasts.update(t => t.filter(x => x !== toast));
    }, 5000);
  }

  success(message: string) { this.show(message, 'success'); }
  error(message: string) { this.show(message, 'error'); }
}
