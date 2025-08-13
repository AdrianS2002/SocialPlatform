import { Component, ElementRef, inject, ViewChild } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { AuthService } from '../../services/auth.service';
import { UserFirestoreService } from '../../services/userFirestoreService';
import { UserService } from '../../services/user.service';
import {RouterLink} from "@angular/router";
import { AlbumComponent } from '../../albums/albums.component';

@Component({
  selector: 'app-my-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AlbumComponent],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})


export class MyProfileComponent {
  protected authService = inject(AuthService);

  successMessage: string = '';
  editMode = false;

  errorMessage: string = '';


  user: any = {}

  constructor(
    private userFirestoreService: UserFirestoreService,
    private http: HttpClient,
    private userService: UserService,
  ) { }

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  async ngOnInit() {
    const authUser = this.authService.getUser();
    console.log('authUser:', authUser);
    if (!authUser) return;

    this.user = {
      id: authUser.id,
      firstName:this.user.firstName,
      lastName: this.user.lastName,
      email: authUser.email,
      profilePicture: '',
      bio: this.user.bio
    };

    console.log('user:', this.user);

    try {
      const userFromBackend = await this.userService.getMyProfile().toPromise();
      this.user = {
        ...this.user,
        ...userFromBackend
      };
    } catch (err) {
      console.error('Failed to fetch backend profile:', err);
    }

    const storedUser = await this.userFirestoreService.getUserProfile(this.user.id);
    if (storedUser) {
      this.user = { ...this.user, ...storedUser };
    }

    console.log('user:', this.user);
  }

  async handleImageUpload(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;

    const compressedBase64 = await compressAndConvertToBase64(file);

    this.user.profilePicture = compressedBase64;
  }

  triggerFileInput() {
    this.fileInput.nativeElement.click();
  }

  async saveProfile() {
    try {
      await this.userFirestoreService.saveUserProfile(this.user.id, {
        profilePicture: this.user.profilePicture
      });

      await this.userService.updateUserProfile(
        this.user.firstName || '-',
        this.user.lastName || '-',
        this.user.bio || '-'
      ).toPromise();

      this.successMessage = 'The profile was saved!';
      this.errorMessage = '';
      setTimeout(() => {
        this.successMessage = '';
      }, 5000);
    } catch (error) {
      console.error('Eroare la salvare:', error);
      this.errorMessage = 'An error occurred while saving the profile.';
      this.successMessage = '';
      setTimeout(() => {
        this.errorMessage = '';
      }, 5000);
    }
  }

  toggleEdit(){
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.ngOnInit();
    }
  }

}
function compressAndConvertToBase64(file: File, maxSize = 300): Promise<string> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    const reader = new FileReader();

    reader.onload = (e: any) => {
      img.src = e.target.result;
    };

    img.onload = () => {
      const canvas = document.createElement('canvas');
      let width = img.width;
      let height = img.height;

      if (width > height) {
        if (width > maxSize) {
          height *= maxSize / width;
          width = maxSize;
        }
      } else {
        if (height > maxSize) {
          width *= maxSize / height;
          height = maxSize;
        }
      }

      canvas.width = width;
      canvas.height = height;

      const ctx = canvas.getContext('2d');
      if (!ctx) return reject('Canvas context error');

      ctx.drawImage(img, 0, 0, width, height);

      const base64 = canvas.toDataURL('image/jpeg', 0.7);
      resolve(base64);
    };

    reader.onerror = reject;
    reader.readAsDataURL(file);
  });

}
