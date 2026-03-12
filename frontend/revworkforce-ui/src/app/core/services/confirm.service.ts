import { Injectable, signal } from '@angular/core';
import { Subject } from 'rxjs';

export interface ConfirmDialogOptions {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'danger' | 'warning' | 'info' | 'success'; 
}

@Injectable({ providedIn: 'root' })
export class ConfirmService {
  private confirmSubject = new Subject<boolean>();
  isOpen = signal(false);
  options = signal<ConfirmDialogOptions | null>(null);

  /**
   * Opens a confirmation dialog.
   * Returns an Observable that emits true if the user clicks "confirm", 
   * and false if they click "cancel".
   */
  confirm(options: ConfirmDialogOptions): Promise<boolean> {
    this.options.set({
      ...options,
      confirmText: options.confirmText || 'Confirm',
      cancelText: options.cancelText || 'Cancel',
      type: options.type || 'warning'
    });
    this.isOpen.set(true);
    
    return new Promise((resolve) => {
      this.confirmSubject = new Subject<boolean>();
      this.confirmSubject.subscribe(res => {
        this.isOpen.set(false);
        resolve(res);
      });
    });
  }

  /**
   * Opens a simple alert dialog (just "OK").
   */
  alert(options: { title: string, message: string, type?: 'danger' | 'warning' | 'info' | 'success' }): Promise<boolean> {
      return this.confirm({
          ...options,
          confirmText: 'OK',
          cancelText: '', // Empty cancel text to show only OK button
          type: options.type || 'info'
      });
  }

  handleResponse(response: boolean) {
    this.confirmSubject.next(response);
    this.confirmSubject.complete();
  }
}
