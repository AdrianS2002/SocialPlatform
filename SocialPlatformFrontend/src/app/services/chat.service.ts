import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatMessage {
  id: number;
  senderId: number;
  receiverId: number;
  content: string;
  sentAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly API = 'http://localhost:8080/chat';

  constructor(private http: HttpClient) {}

  sendMessage(to: number, content: string): Observable<ChatMessage> {
    const params = new HttpParams().set('to', to.toString());
    return this.http.post<ChatMessage>(`${this.API}/send`, content, { params });
  }

  receiveMessages(withUserId: number): Observable<ChatMessage[]> {
    const params = new HttpParams().set('with', withUserId.toString());
    return this.http.get<ChatMessage[]>(`${this.API}/receive`, { params });
  }
}
