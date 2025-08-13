import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  errorMessage: string = '';


  constructor(private router: Router, private authService: AuthService) { }

  toSignUp(){
    this.router.navigate(['/signup']);
  }

  forgotPassword(){
    this.router.navigate(['/forgot-password']);
  }

  ngOnInit() {
    this.authService.authMessage$.subscribe(message => {
      if (message) this.setError(message);
    });
  }


  login() {
    if (!this.email || !this.password) {
      this.setError('Email and password are required.');
      return;
    }

    this.authService.login({ email: this.email, password: this.password }).subscribe((response) => {
      if (!response) {
        this.setError('Invalid credentials or account not approved.');
      }
    });
  }

  private setError(message: string) {
    this.errorMessage = message;

    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
  }
}

