import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {UserService} from '../../services/user.service';
import {UserFirestoreService} from '../../services/userFirestoreService';
import {AuthService} from '../../services/auth.service';
import {FriendsService} from '../../services/friends.service';
import {FeedService} from '../../services/feed.service';
import {FeedPost} from '../../models/feed-post';

@Component({
  selector: 'app-public-profile',
  standalone: true,
  imports: [NgIf, NgForOf, DatePipe, RouterLink],
  templateUrl: './public-profile.component.html',
  styleUrl: './public-profile.component.css'
})
export class PublicProfileComponent implements OnInit {
  user: any = {};
  userId!: string;
  currentUser: any;
  isOwnProfile = false;
  requestSent = false;
  isAlreadyFriend: boolean = false;
  publicPosts: FeedPost[] = [];
  isAdmin = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly userService: UserService,
    private readonly firestoreService: UserFirestoreService,
    private readonly authService: AuthService,
    private readonly friendsService: FriendsService,
    private readonly feedService: FeedService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.userId = this.route.snapshot.paramMap.get('id')!;
    this.currentUser = this.authService.getUser();
    this.isOwnProfile = +this.userId === +this.currentUser?.id;
    if (!this.isOwnProfile) {
      this.checkFriendStatus();
    }
    this.loadUser();
    this.loadUserPosts();
  }

  loadUser() {
    const profileRequest = this.isOwnProfile
      ? this.userService.getMyProfile()
      : this.userService.getUserProfile(this.userId);
    profileRequest.subscribe({
      next: async (userData) => {
        try {
          const firestoreData = await this.firestoreService.getUserProfile(this.userId);
          this.user = { ...userData, ...firestoreData };
        } catch (err) {
          console.error('Failed to load Firestore user data:', err);
          this.user = userData;
        }
      },
      error: (err) => {
        console.error('Failed to load user:', err);
      }
    });
  }

  sendFriendRequest() {
    this.friendsService.sendFriendRequest(this.userId).subscribe({
      next: () => {
        this.requestSent = true;
      },
      error: (err) => {
        console.error('Failed to send request:', err);
        this.requestSent = true;
      }
    });
  }

  checkFriendStatus() {
    this.friendsService.getAllFriends().subscribe({
      next: (friends) => {
        this.isAlreadyFriend = friends.some((friend: any) => +friend.id === +this.userId);
      },
      error: (err) => {
        console.error('Failed to check friend status:', err);
      }
    });
  }

  loadUserPosts() {
    this.feedService.getPosts().subscribe({
      next: (posts) => {
        this.publicPosts = posts.filter(post => +post.postedById === +this.userId &&!post.isBlocked);
      },
      error: (err) => {
        console.error('Failed to load user posts:', err);
      }
    });
  }
}
