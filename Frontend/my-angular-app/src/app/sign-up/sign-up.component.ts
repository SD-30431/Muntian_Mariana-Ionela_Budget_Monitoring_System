// src/app/sign-up/sign-up.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NgIf],
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  username = '';
  password = '';
  confirmPassword = '';
  // This is the value the user enters as monthly income.
  income: number | null = null;
  errorMessage = '';

  constructor(private router: Router, private userService: UserService) {}

  onSignUp() {
  if (!this.username || !this.password || !this.confirmPassword || this.income === null) {
    this.errorMessage = 'All fields are required!';
    return;
  }

  if (this.password !== this.confirmPassword) {
    this.errorMessage = 'Passwords do not match!';
    return;
  }

  this.errorMessage = '';

  const newUser = {
    username: this.username.trim(),
    password: this.password,
    income: this.income
  };

  this.userService.signUp(newUser).subscribe({
    next: (user) => {
      this.userService.setLoggedInUser(user);
      this.router.navigate(['/main-user']);
    },
    error: (error) => {
      this.errorMessage = error.error?.message || 'Sign up failed. Please try again.';
    }
  });
}


  onGoBack() {
    this.router.navigate(['/dashboard']);
  }
}
