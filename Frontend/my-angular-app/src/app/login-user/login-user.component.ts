import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login-user',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
export class LoginUserComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router, private userService: UserService) {}

  onLogin(): void {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Both fields are required.';
      return;
    }

    if (this.username.trim().length < 3 || this.username.trim().length > 30) {
      this.errorMessage = 'Username must be between 3 and 30 characters.';
      return;
    }

    if (this.password.trim().length < 3) {
      this.errorMessage = 'Password must be at least 3 characters long.';
      return;
    }

    this.userService.login(this.username.trim(), this.password.trim()).subscribe({
      next: (response) => {
        if (response && response.success && response.user) {
          this.userService.setLoggedInUser(response.user);
          localStorage.setItem('user', JSON.stringify(response.user));
          this.router.navigate(['/main-user']);
        } else {
          this.errorMessage = response.message || 'Login failed.';
        }
      },
      error: () => {
        this.errorMessage = 'Server error. Please try again later.';
      }
    });
  }

  onGoBack(): void {
    this.router.navigate(['/start']);
  }
}
