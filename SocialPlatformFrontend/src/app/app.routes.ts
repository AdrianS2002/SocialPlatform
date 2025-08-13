import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { MyProfileComponent } from './components/my-profile/my-profile.component';
import {UserManagementComponent} from './components/user-management/user-management.component';
import {AdminGuard} from './guards/admin.guard';
import {FriendsComponent} from './components/friends/friends.component';
import {FeedComponent} from './components/feed/feed.component';
import {PublicProfileComponent} from './components/public-profile/public-profile.component';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {ResetPasswordComponent} from './components/reset-password/reset-password.component';
import {ChatComponent} from './components/chat/chat.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'reset-password', component: ResetPasswordComponent },
    { path: 'my-profile/:id', component: MyProfileComponent },
    { path: 'public-profile/:id', component: PublicProfileComponent },
    { path: 'friends', component: FriendsComponent },
    { path: 'feed', component: FeedComponent },
    { path: 'chat/:id', component: ChatComponent },
    { path: 'user-management', component: UserManagementComponent, canActivate: [AdminGuard] },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];
