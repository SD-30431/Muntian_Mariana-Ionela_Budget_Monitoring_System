import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AdminService } from '../services/admin.service';

@Component({
  selector: 'app-login-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.css']
})
export class LoginAdminComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router, private adminService: AdminService) {}

  onLogin(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Please fill in both fields.';
      return;
    }

    this.adminService.login(this.username, this.password).subscribe({
      next: (response) => {
        if (response && response.success) {
          // Save the admin object to localStorage
          this.adminService.setLoggedInAdmin(response.admin);
          this.router.navigate(['/main-admin']);
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
    this.router.navigate(['/dashboard']);
  }
}
