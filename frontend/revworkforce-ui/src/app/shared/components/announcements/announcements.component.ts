import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { AnnouncementService } from '../../../core/services/announcement.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule, SidebarComponent, FormsModule],
  templateUrl: './announcements.component.html',
  styleUrls: ['./announcements.component.css']
})
export class AnnouncementsComponent implements OnInit {
  announcements = signal<any[]>([]);
  isLoading = signal(true);
  showModal = signal(false);
  editMode = signal(false);
  currentAnnouncement: any = { id: null, title: '', content: '' };

  constructor(
    private announcementService: AnnouncementService,
    private toast: ToastService,
    private confirmService: ConfirmService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadAnnouncements();
  }

  loadAnnouncements() {
    this.isLoading.set(true);
    this.announcementService.getAllAnnouncements().subscribe({
      next: (data: any[]) => {
        this.announcements.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.announcements.set([]);
        this.isLoading.set(false);
      }
    });
  }

  getInitials(name: string | undefined): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getRole(): 'EMPLOYEE' | 'MANAGER' | 'ADMIN' {
    const role = this.auth.getRole();
    return (role === 'EMPLOYEE' || role === 'MANAGER' || role === 'ADMIN') ? role : 'EMPLOYEE';
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
          this.showModal.set(false); 
          this.loadAnnouncements(); 
        },
        error: () => this.toast.error('Failed to update announcement')
      });
    } else {
      this.announcementService.createAnnouncement(payload).subscribe({
        next: () => { 
          this.showModal.set(false); 
          this.loadAnnouncements(); 
          this.toast.success('Announcement created successfully');
        },
        error: () => this.toast.error('Failed to create announcement')
      });
    }
  }

  async deleteAnnouncement(id: number) {
    const confirmed = await this.confirmService.confirm({
        title: 'Delete Announcement',
        message: 'Are you sure you want to delete this announcement?',
        type: 'danger'
    });
    if (!confirmed) return;

    this.announcementService.deleteAnnouncement(id).subscribe({
      next: () => {
        this.loadAnnouncements();
        this.toast.success('Announcement deleted successfully');
      },
      error: () => this.toast.error('Failed to delete announcement')
    });
  }

  formatDate(date: any): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('en-US', { 
      day: '2-digit', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  isSidebarOpen = false;
  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }
}
