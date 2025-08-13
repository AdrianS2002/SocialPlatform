import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatService, ChatMessage } from '../../services/chat.service';
import {DatePipe, NgClass, NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [NgForOf, NgClass, FormsModule, DatePipe],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit, OnDestroy {
  userId!: number;
  userName!: string;
  messages: ChatMessage[] = [];
  newMessage: string = '';
  intervalId: any;

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService
  ) {}

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
    this.route.queryParams.subscribe(params => {
      this.userName = params['name'] || 'User';
    });
    this.loadMessages();
    this.intervalId = setInterval(() => {
      this.loadMessages();
    }, 1000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  loadMessages() {
    this.chatService.receiveMessages(this.userId).subscribe({
      next: (msgs) => this.messages = msgs,
      error: (err) => console.error('Failed to load chat messages', err)
    });
  }

  sendMessage() {
    const content = this.newMessage.trim();
    if (!content) return;

    this.chatService.sendMessage(this.userId, content).subscribe({
      next: (msg) => {
        this.messages.push(msg);
        this.newMessage = '';
      },
      error: (err) => console.error('Failed to send message', err)
    });
  }
}
