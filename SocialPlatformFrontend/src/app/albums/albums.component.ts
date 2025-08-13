import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Album } from '../models/album-model';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { ImageResponse } from '../models/image-model';
import { AlbumService } from '../services/album.service';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-albums',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, FormsModule],
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.css']
})
export class AlbumComponent implements OnInit {
  albums: Album[] = [];
  selectedPhotos: ImageResponse[] = [];
  newAlbumTitle: string = '';
  newAlbumDescription: string = '';
  userId!: number;
  showAddAlbumModal = false;
  successMessageAlbum: string = '';
  errorMessageAlbum: string = '';
  editingAlbumId: number | null = null;
  selectedAlbumTitle: string = '';
  showPhotoModal: boolean = false;
  selectedAlbumId: number | null = null;

  selectedImage: string | null = null;
  showImageModal: boolean = false;

  showConfirmModal: boolean = false;
  photoToDeleteId: number | null = null;

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  constructor(
    private albumService: AlbumService,
    private authService: AuthService
  ) { }

  openAddAlbumModal() {
    this.showAddAlbumModal = true;
    this.editingAlbumId = null;
    this.newAlbumTitle = '';
    this.newAlbumDescription = '';
  }

  openEditAlbumModal(album: Album) {
    this.showAddAlbumModal = true;
    this.editingAlbumId = album.id!;
    this.newAlbumTitle = album.title;
    this.newAlbumDescription = album.description;
  }

  closeAddAlbumModal() {
    this.showAddAlbumModal = false;
    this.newAlbumTitle = '';
    this.newAlbumDescription = '';
  }

  ngOnInit(): void {
    const user = this.authService.getUser();
    if (!user) return;

    this.userId = user.id;
    this.loadAlbums();
    this.loadAlbumsPrev();
  }

  loadAlbums(): void {
    this.albumService.getUserAlbums(this.userId).subscribe({
      next: (data: Album[]) => {
        this.albums = data;
        this.selectedPhotos = [];
      },
      error: (err: any) => {
        console.error('Error loading albums:', err);
      }
    });
  }

  createAlbum() {
    if (!this.newAlbumTitle.trim()) return;

    const album: any = {
      title: this.newAlbumTitle,
      description: this.newAlbumDescription,
      userId: this.userId
    };

    if (this.editingAlbumId !== null) {
      album.id = this.editingAlbumId;
    }

    if (this.editingAlbumId) {

      this.albumService.editAlbum(album).subscribe({
        next: () => {
          this.successMessageAlbum = 'Album updated!';
          this.closeAddAlbumModal();
          this.loadAlbums();
          this.resetMessagesAfterDelay();
        },
        error: () => {
          this.errorMessageAlbum = 'Failed to update album.';
          this.resetMessagesAfterDelay();
        }
      });
    } else {

      this.albumService.createAlbum(album).subscribe({
        next: () => {
          this.successMessageAlbum = 'Album created!';
          this.closeAddAlbumModal();
          this.loadAlbums();
          this.resetMessagesAfterDelay();
        },
        error: () => {
          this.errorMessageAlbum = 'Failed to create album.';
        }
      });
    }
  }


  deleteAlbum(albumId: number): void {
    if (!confirm('Are you sure you want to delete this album?')) return;

    this.albumService.deleteAlbum(albumId).subscribe({
      next: () => {
        this.loadAlbums();
      },
      error: (err: any) => {
        console.error('Error deleting album:', err);
      }
    });
  }

  openAlbum(albumId: number): void {
    this.selectedAlbumId = albumId;
    const album = this.albums.find(a => a.id === albumId);
    this.selectedAlbumTitle = album?.title || 'Album';
    this.showPhotoModal = true;
    this.loadPhotos(albumId);
    this.loadAlbumsPrev();
  }

  closePhotoModal(): void {
    this.showPhotoModal = false;
    this.selectedPhotos = [];
    this.selectedAlbumId = null;
    this.loadAlbumsPrev();
  }

  uploadPicture() {
    console.log("Upload picture for album:", this.selectedAlbumId);
  }
  private resetMessagesAfterDelay(): void {
    setTimeout(() => {
      this.successMessageAlbum = '';
      this.errorMessageAlbum = '';
    }, 5000);
  }

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  loadPhotos(albumId: number): void {
    this.albumService.getAlbumPhotos(albumId).subscribe({
      next: (photos) => {
        this.selectedPhotos = photos;
      },
      error: (err) => {
        console.error('Error loading photos:', err);
        this.errorMessageAlbum = 'Failed to load photos.';
        this.resetMessagesAfterDelay();
      }
    });
  }

  deletePhoto(photoId: number): void {
    if (!confirm('Are you sure you want to delete this photo?')) return;

    this.albumService.deleteImage(photoId).subscribe({
      next: () => {
        this.selectedPhotos = this.selectedPhotos.filter(p => p.id !== photoId);
        this.successMessageAlbum = 'Photo deleted!';
        this.resetMessagesAfterDelay();
      },
      error: () => {
        this.errorMessageAlbum = 'Failed to delete photo.';
        this.resetMessagesAfterDelay();
      }
    });
  }


  handleImageUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0 || this.selectedAlbumId === null) return;

    const file = input.files[0];

    this.albumService.uploadImage(file, this.selectedAlbumId).subscribe({
      next: () => {
        this.successMessageAlbum = 'Image uploaded!';
        this.loadPhotos(this.selectedAlbumId!);
        this.resetMessagesAfterDelay();
      },
      error: () => {
        this.errorMessageAlbum = 'Failed to upload image.';
        this.resetMessagesAfterDelay();
      }
    });
    input.value = '';
  }

  openImageModal(photoBase64: string): void {
    this.selectedImage = photoBase64;
    this.showImageModal = true;
  }

  closeImageModal(): void {
    this.selectedImage = null;
    this.showImageModal = false;
  }

  askToDeletePhoto(photoId: number): void {
    this.photoToDeleteId = photoId;
    this.showConfirmModal = true;
  }

  confirmDelete(): void {
    if (this.photoToDeleteId === null) return;

    this.albumService.deleteImage(this.photoToDeleteId).subscribe({
      next: () => {
        this.selectedPhotos = this.selectedPhotos.filter(p => p.id !== this.photoToDeleteId);
        this.successMessageAlbum = 'Photo deleted!';
        this.resetMessagesAfterDelay();
        this.showConfirmModal = false;
        this.photoToDeleteId = null;
        this.resetMessagesAfterDelay();
      },
      error: () => {
        this.errorMessageAlbum = 'Failed to delete photo.';
        this.resetMessagesAfterDelay();
        this.showConfirmModal = false;
      }
    });
  }

  cancelDelete(): void {
    this.showConfirmModal = false;
    this.photoToDeleteId = null;
    this.showConfirmAlbumModal = false;
    this.albumToDeleteId = null;
  }


  showConfirmAlbumModal: boolean = false;
  albumToDeleteId: number | null = null;

  askToDeleteAlbum(albumId: number): void {
    this.albumToDeleteId = albumId;
    this.showConfirmAlbumModal = true;
  }

  confirmDeleteAlbum(): void {
    if (this.albumToDeleteId === null) return;

    this.albumService.deleteAlbum(this.albumToDeleteId).subscribe({
      next: () => {
        this.loadAlbums();
        this.showConfirmAlbumModal = false;
        this.albumToDeleteId = null;
      },
      error: (err: any) => {
        console.error('Error deleting album:', err);
        this.showConfirmAlbumModal = false;
      }
    });
  }

  albumPreviews: { [albumId: number]: ImageResponse[] } = {};
  loadAlbumsPrev(): void {
  this.albumService.getUserAlbums(this.userId).subscribe({
    next: (data: Album[]) => {
      this.albums = data;
      this.selectedPhotos = [];
      this.albums.forEach(album => {
        if (album.id) {
          this.albumService.getAlbumPhotos(album.id).subscribe({
            next: (photos) => {
              this.albumPreviews[album.id!] = photos.slice(0, 4); // doar primele 4
            },
            error: () => {
              console.error(`Error loading preview for album ${album.id}`);
            }
          });
        }
      });
    },
    error: (err: any) => {
      console.error('Error loading albums:', err);
    }
  });
}



}
