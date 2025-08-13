import {Component, OnInit} from '@angular/core';
import {CommonModule, DatePipe, NgForOf, NgIf} from '@angular/common';
import {FeedService} from '../../services/feed.service';
import {UserFirestoreService} from '../../services/userFirestoreService';
import {AuthService} from '../../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import { ChatbotService } from '../../services/chatbot.service';
import { FormsModule } from '@angular/forms';
import { AlbumService } from '../../services/album.service';

@Component({
  selector: 'app-feed',
  standalone: true,
  templateUrl: './feed.component.html',
  imports: [
    DatePipe,
    NgForOf,
    NgIf,
    RouterLink,
    CommonModule,
    FormsModule
],
  styleUrl: './feed.component.css'
})
export class FeedComponent implements OnInit {
  posts: any[] = [];
  isAdmin = false;
  isChatOpen: boolean = false;
  userMessage: string = '';
  isAuthorized: boolean = false;
  messages: { sender: string, text: string }[] = [];

  constructor(
    private feedService: FeedService,
    private albumService: AlbumService,
    private firestoreService: UserFirestoreService,
    private authService: AuthService,
    private chatbotService: ChatbotService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    const user = this.authService.getUser();
    const role = user?.role;

    if (role === 'ROLE_ADMIN' || role === 'ROLE_USER') {
      this.isAuthorized = true;
    } else {
      this.router.navigate(['/unauthorized']);
    }
    this.isAdmin = this.authService.isAdmin();
    this.feedService.getPosts().subscribe({
      next: async (posts) => {
        const finalPosts = [];
        const visiblePosts = posts.filter(post => !post.isBlocked);
        for (const post of visiblePosts) {
          let profileData = { profilePicture: 'default.png', fullName: post.postedByName };
          try {
            const data = await this.firestoreService.getUserProfile(post.postedById.toString());
            profileData.profilePicture = data.profilePicture || 'unknown.png';
            profileData.fullName = `${data.firstName || ''} ${data.lastName || ''}`.trim() || post.postedByName;
          } catch (_) {}
          finalPosts.push({
            ...post,
            ...profileData,
            imageId: post.imageId
          });
        }
        this.posts = finalPosts.sort((a, b) => {
          if (a.isFriend === b.isFriend) {
            return new Date(b.postedAt).getTime() - new Date(a.postedAt).getTime();
          }
          return a.isFriend ? -1 : 1;
        });
      },
      error: (err) => console.error('Error at loading posts:', err)
    });
  }

  toggleChat() {
    this.isChatOpen = !this.isChatOpen;
    if (this.isChatOpen && this.messages.length === 0) {
      this.messages.push({ sender: "AI", text: "Hi there! How can I help you today?" });
    }
  }

  blockPost(postId: number) {
    this.feedService.blockPost(postId).subscribe({
      next: () => {
        this.posts = this.posts.filter(p => p.imageId !== postId);
        console.log(`Post ${postId} blocked.`);
      },
      error: (err) => {
        console.error('Failed to block post:', err);
      }
    });
  }

  deletePost(postId: number) {
    this.feedService.deletePost(postId).subscribe({
      next: () => {
        this.posts = this.posts.filter(p => p.imageId !== postId);
        console.log(`Post ${postId} deleted.`);
      },
      error: (err) => {
        console.error('Failed to delete post', err);
      }
    });
  }

  async sendMessage() {
    if (!this.userMessage.trim()) return;

    this.messages.push({ sender: "You", text: this.userMessage });
    const messageToSend = this.userMessage;
    this.userMessage = "";

    try {
      const reply = await this.chatbotService.sendMessage(messageToSend);
      this.messages.push({ sender: "AI", text: reply || "Sorry, I could not understand that." });
    } catch (err) {
      console.error("Chatbot Error:", err);
      this.messages.push({ sender: "AI", text: "Sorry, there was an error communicating with AI." });
    }
  }
}
