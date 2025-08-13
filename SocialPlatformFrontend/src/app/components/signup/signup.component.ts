import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router, private authService: AuthService) { }

  toLogin() {
    this.router.navigate(['/login']);
  }

  ngOnInit() {
    this.authService.authMessage$.subscribe(message => {
      if (message) this.setError(message);
    });

    this.authService.authSuccess$.subscribe(message => {
      if (message) this.setSuccess(message);
    });
  }

  signup() {

    if ( !this.email || !this.password || !this.confirmPassword) {

      this.setError('All fields are required.');
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.setError('Passwords do not match.');
      return;
    }

    this.authService.signup({
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    }).subscribe((response) => {
      if (response === null) {
        this.setError('Signup failed. Email might already be used.');
      } else {
        this.setSuccess('Account created. Awaiting admin approval.');
      }
    });
  }

  private setError(message: string) {
    this.errorMessage = message;
    setTimeout(() => this.errorMessage = '', 5000);
  }

  private setSuccess(message: string) {
    this.successMessage = message;
    setTimeout(() => this.successMessage = '', 5000);
  }
}
