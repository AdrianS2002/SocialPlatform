import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {user} from "@angular/fire/auth";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  userId: string = '';

  constructor(public authService: AuthService, private router: Router) {
    const user = this.authService.getUser();
    if (user) {
      this.userId = user.id;
    }
  }

  logout() {
    this.router.navigate(['/login']);
    this.authService.logout();
  }

  protected readonly user = user;
}
