import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgForOf, NgIf} from '@angular/common';
import {FriendsService} from '../../services/friends.service';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    NgForOf
  ],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.css'
})
export class FriendsComponent implements OnInit {
  selectedTab: 'friends' | 'requests' = 'friends';
  friends: any[] = [];
  friendRequests: any[] = [];

  constructor(private friendsService: FriendsService) {
  }

  ngOnInit(): void {
    this.loadFriends();
    this.loadRequests();
  }

  loadFriends() {
    this.friendsService.getAllFriends().subscribe({
      next: (data) => this.friends = data,
      error: (err) => console.error('Failed to load friends:', err)
    });
  }

  loadRequests() {
    this.friendsService.getFriendRequests().subscribe({
      next: (data: any[]) => this.friendRequests = data,
      error: (err: any) => console.error('Failed to load requests:', err)
    });
  }

  acceptRequest(request: any) {
    this.friendsService.acceptFriendRequest(request.senderId).subscribe(() => {
      this.friendRequests = this.friendRequests.filter(r => r.senderId !== request.senderId);
      this.loadFriends();
    });
  }

  deleteRequest(request: any) {
    this.friendsService.deleteFriend(request.senderId).subscribe(() => {
      this.friendRequests = this.friendRequests.filter(r => r.senderId !== request.senderId);
    });
  }
}
