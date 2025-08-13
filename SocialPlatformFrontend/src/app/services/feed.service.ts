import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import {FeedPost} from '../models/feed-post';

@Injectable({
  providedIn: 'root'
})
export class FeedService {

  private readonly API_BASE =  'http://localhost:8080'

  constructor(private http: HttpClient) {}

  getPosts(): Observable<FeedPost[]> {
    return this.http.get<FeedPost[]>(`${this.API_BASE}/feed/posts`);
  }

  blockPost(postId: number) {
    const params = new HttpParams().set('id', postId.toString());
    return this.http.post(`${this.API_BASE}/image/block-photo`, null, { params });
  }

  deletePost(postId: number) {
    return this.http.delete(`${this.API_BASE}/image/delete-photo?id=${postId}`);
  }
}
