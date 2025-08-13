import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';
  message: string = '';
  error: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  submitEmail() {
    if (!this.email) {
      this.error = 'Please enter your email.';
      return;
    }
    this.authService.sendRecoveryCode(this.email).subscribe({
      error: (err) => {
        console.error('Failed to send recovery code:', err);
        this.error = 'Failed to send recovery code. Try again.';
      }
    });
    this.router.navigate(['/reset-password'], { queryParams: { email: this.email } });
  }
}
