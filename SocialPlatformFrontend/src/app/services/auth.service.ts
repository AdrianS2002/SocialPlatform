import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

interface LoginRequest {
  email: string;
  password: string;
}

interface SignupRequest {
  email: string;
  password: string;
  confirmPassword: string;
}

interface AuthResponse {
  token: string;
  expiresIn: number;
  user: any;
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private API_BASE = 'http://localhost:8080';

  private tokenKey = 'access_token';
  private tokenExpirationKey = 'token_expiration';
  private userKey = 'user_data';

  private userSubject = new BehaviorSubject<any | null>(null);
  public user$ = this.userSubject.asObservable();

  private authMessageSubject = new BehaviorSubject<string | null>(null);
  authMessage$ = this.authMessageSubject.asObservable();

  private authSuccessSubject = new BehaviorSubject<string | null>(null);
  authSuccess$ = this.authSuccessSubject.asObservable();
  role: string | undefined;

  constructor(private http: HttpClient, private router: Router) {
    this.checkTokenValidity();
  }

  login(data: LoginRequest): Observable<any> {
    return this.http.post(`${this.API_BASE}/auth/login`, data, { responseType: 'text' }).pipe(
      tap((token: string) => {
        const decoded = this.decodeToken(token);
        console.log('🔍 JWT payload:', decoded);

        if (!decoded.isValidated) {
          this.authMessageSubject.next('Account not yet validated by admin.');
          return; // nu salva nimic, nu naviga
        }

        const user = {
          id: decoded.id,
          email: decoded.email || decoded.sub,
          isValidated: decoded.isValidated,
          role: decoded.roles
        };

        const response: AuthResponse = {
          token,
          expiresIn: 3600,
          user
        };

        this.storeSession(response);
        this.userSubject.next(user);
        this.role = user.role;
        if (this.role === 'ROLE_ADMIN') {
          this.router.navigate(['/user-management']);
        } else {
          this.router.navigate(['/my-profile', user.id]);
        }
      }),
      catchError(error => {
        console.error('Login failed:', error);
        this.logout();
        return of(null);
      })
    );
  }

  sendRecoveryCode(email: string) {

    return this.http.post(`${this.API_BASE}/email/forgot-password/${email}`, {}, { responseType: 'text' });
  }

  verifyCode(email: string, code: string) {
    return this.http.post(`${this.API_BASE}/email/verify-code`, { email, code }, { responseType: 'text' });
  }

  resetPassword(email: string, newPassword: string) {
    return this.http.post(`${this.API_BASE}/email/reset-password`, { email, newPassword }, { responseType: 'text' });
  }

  private decodeToken(token: string): any {
    try {
      const parts = token.split('.');
      console.log('🧩 Token parts:', parts);
      const payload = parts[1];
      const decoded = JSON.parse(atob(payload));
      console.log('🔍 JWT payload:', decoded);
      return decoded;
    } catch (e) {
      console.error('❌ JWT decode failed:', e);
      return null;
    }
  }

  signup(data: SignupRequest): Observable<any> {
    return this.http.post(`${this.API_BASE}/auth/signup`, data).pipe(
      tap(() => {
        this.authSuccessSubject.next('Account created successfully. Please wait for administrator approval.');
        this.router.navigate(['/login']);
      }),
      catchError(error => {
        console.error('Signup failed:', error);
        return of(null);
      })
    );
  }

  logout(): void {
    const token = this.getToken();

    if (token) {
      this.http.post(`${this.API_BASE}/auth/logout`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        responseType: 'text'
      }).subscribe({
        next: () => console.log('Logged out on backend'),
        error: err => console.warn('Backend logout failed:', err)
      });
    }
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.tokenExpirationKey);
    localStorage.removeItem(this.userKey);
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem(this.tokenKey);
    const expiration = localStorage.getItem(this.tokenExpirationKey);
    if (!token || !expiration) return false;
    return Date.now() < Number(expiration);
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.tokenKey);
    const expiration = localStorage.getItem(this.tokenExpirationKey);
    if (!token || !expiration || Date.now() >= Number(expiration)) {
      return null; 
    }
    return token;
  }

  getUser(): any {
    if (!this.userSubject.value) {
      const stored = localStorage.getItem(this.userKey);
      if (stored) this.userSubject.next(JSON.parse(stored));
    }
    return this.userSubject.value;
  }

  isAdmin(): boolean {
    return this.role === 'ROLE_ADMIN';
  }

  isUser(): boolean {
    return this.role === 'ROLE_USER';
  }

  private storeSession(response: AuthResponse): void {
    const expiration = Date.now() + response.expiresIn * 1000;
    localStorage.setItem(this.tokenKey, response.token);
    localStorage.setItem(this.tokenExpirationKey, expiration.toString());
    localStorage.setItem(this.userKey, JSON.stringify(response.user));
  }

  private checkTokenValidity(): void {
    if (!this.isAuthenticated()) {
      this.logout();
    } else {
      const userData = localStorage.getItem(this.userKey);
      if (userData) {
        const user = JSON.parse(userData);
        this.userSubject.next(JSON.parse(userData));
        this.role = user.role;
      }
    }
  }
}
