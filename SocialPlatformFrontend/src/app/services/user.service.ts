import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class UserService {
  private API_BASE = 'http://localhost:8080/profile';

  constructor(private http: HttpClient) { }

  updateUserProfile(firstName: string, lastName: string, bio: string) {
    return this.http.put(`${this.API_BASE}/update-profile`, {
      firstName,
      lastName,
      bio
    }, { responseType: 'text' });
  }

  getMyProfile() {
    return this.http.get(`${this.API_BASE}/me`)
  }

  getUserProfile(id: any) {
    return this.http.get(`${this.API_BASE}?userId=${id}`);
  }
}
