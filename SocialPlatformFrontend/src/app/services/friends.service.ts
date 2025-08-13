import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private baseUrl = 'http://localhost:8080/friends';

  constructor(private http: HttpClient) {}

  getAllFriends(): Observable<any> {
    return this.http.get(`${this.baseUrl}/all`);
  }

  getFriendRequests(): Observable<any> {
    return this.http.get(`${this.baseUrl}/all-requests`);
  }

  sendFriendRequest(userId: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/send-request/${userId}`, {});
  }

  acceptFriendRequest(friendId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/accept/${friendId}`, {});
  }

  deleteFriend(friendId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${friendId}`);
  }
}
