import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from '../models/user-model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_BASE = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_BASE}/user/all`);
  }

  validateUser(userId: string): Observable<any> {
    return this.http.post(`${this.API_BASE}/admin/validate-user?id=${userId}`, {});
  }

  deleteUser(userId: string) {
    return this.http.delete(`${this.API_BASE}/admin/delete-user/${userId}`);
  }
}
