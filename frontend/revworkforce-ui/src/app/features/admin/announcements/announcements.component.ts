import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { AnnouncementService } from '../../../core/services/announcement.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-admin-announcements',
  standalone: true,
  imports: [CommonModule, FormsModule, SidebarComponent],
  templateUrl: './announcements.component.html',
  styleUrls: ['./announcements.component.css']
})
export class AdminAnnouncementsComponent implements OnInit {
  announcements = signal<any[]>([]);
  isLoading = signal(true);
  showModal = signal(false);
  editMode = signal(false);
  currentAnnouncement: any = { id: null, title: '', content: '' };

  constructor(
    private announcementService: AnnouncementService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.loadAnnouncements(); }

  loadAnnouncements() {
    this.announcementService.getAllAnnouncements().subscribe({
      next: (data: any[]) => { this.announcements.set(data); this.isLoading.set(false); },
      error: () => { this.announcements.set([]); this.isLoading.set(false); }
    });
  }

  openAddModal() {
    this.editMode.set(false);
    this.currentAnnouncement = { id: null, title: '', content: '' };
    this.showModal.set(true);
  }

  openEditModal(announcement: any) {
    this.editMode.set(true);
    this.currentAnnouncement = { ...announcement };
    this.showModal.set(true);
  }

  saveAnnouncement() {
    const payload = {
      title: this.currentAnnouncement.title,
      content: this.currentAnnouncement.content
    };
    if (this.editMode()) {
      this.announcementService.updateAnnouncement(this.currentAnnouncement.id, payload).subscribe({
        next: () => { 
          this.toastService.success('Announcement updated');
          this.showModal.set(false); 
          this.loadAnnouncements(); 
        },
        error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to update announcement', type: 'danger' })
      });
    } else {
      this.announcementService.createAnnouncement(payload).subscribe({
        next: () => { 
          this.toastService.success('Announcement created');
          this.showModal.set(false); 
          this.loadAnnouncements(); 
        },
        error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to create announcement', type: 'danger' })
      });
    }
  }

  async deleteAnnouncement(id: number) {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Announcement',
      message: 'Are you sure you want to delete this announcement? This action is permanent.',
      type: 'danger',
      confirmText: 'Delete'
    });
    if (!confirmed) return;
    
    this.announcementService.deleteAnnouncement(id).subscribe({
      next: () => {
        this.toastService.success('Announcement deleted');
        this.loadAnnouncements();
      },
      error: () => this.confirmService.alert({ title: 'Error', message: 'Failed to delete announcement', type: 'danger' })
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
