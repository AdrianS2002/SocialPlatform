import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserService} from '../../services/user-management.service';
import {User} from '../../models/user-model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  pendingUsers: User[] = [];
  successMessage = '';
  errorMessage = '';
  userIdToDelete: string | null = null;

  constructor(private readonly userService: UserService) {
  }

  ngOnInit(): void {
    this.loadPendingUsers();
  }

  loadPendingUsers() {
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.pendingUsers = users.map(user => {
          user.isValidated = user.isValidated === true || (typeof user.isValidated === 'string' && user.isValidated === 'true');
          return user;
        });
        this.pendingUsers = this.pendingUsers.filter((user) => user.email !== 'admin@admin.com');
      },
      error: (err) => {
        this.errorMessage = 'Failed to load pending users.';
        console.error(err);
      }
    });
  }

  validateUser(userId: string) {
    this.userService.validateUser(userId).subscribe({
      next: () => {
        this.successMessage = 'User validated successfully!';
        setTimeout(() => this.successMessage = '', 3000);
        this.loadPendingUsers();
      },
      error: (err) => {
        this.errorMessage = 'User validation failed.';
        console.error(err);
      }
    });
  }

  cancelDelete() {
    this.userIdToDelete = null;
  }

  deleteUser() {
    if (!this.userIdToDelete) return;
    this.userService.deleteUser(this.userIdToDelete).subscribe({
      next: () => {
        this.userIdToDelete = null;
        this.loadPendingUsers();
      },
      error: (err) => {
        this.userIdToDelete = null;
        this.loadPendingUsers();
      }
    });
  }

  openDeleteModal(userId: string) {
    this.userIdToDelete = userId;
  }

  getInitials(user: User): string {
    const initials = `${user.firstName || ''} ${user.lastName || ''}`.trim().split(' ').map(n => n.charAt(0)).join('').toUpperCase();
    return initials || 'NCY';
  }

  getFullName(user: User): string {
    return [user.firstName, user.lastName].filter(Boolean).join(' ') || 'Not completed yet';
  }
}
