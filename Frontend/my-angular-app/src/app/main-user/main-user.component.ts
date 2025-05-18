import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-main-user',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIf, NgFor, HttpClientModule, FormsModule],
  templateUrl: './main-user.component.html',
  styleUrls: ['./main-user.component.css']
})
export class MainUserComponent implements OnInit {
  username: string = '';
  income: number = 0;
  cards: { cardnumber: string; amount: number }[] = [];

  profilePictureUrl: string = '';
  selectedFile: File | null = null;

  showChangePassword: boolean = false;
  newPassword: string = '';
  confirmPassword: string = '';
  passwordMessage: string = '';

  walletOpen: boolean = false;

  constructor(private router: Router, private userService: UserService, private http: HttpClient) {}

  navigateTo(route: string): void {
    const routesMap: { [key: string]: string } = {
      main: '/main-user',
      'manage-expenses': '/manage-expenses',
      'manage-cards': '/manage-cards',
      history: '/history',
      chart: '/chart-expenses',
      chat: '/chat',
      'send-money': '/send-money',
      'spending-trends': '/spending-trends', 
      dashboard: '/dashboard'
    };

    if (route === 'dashboard') {
      this.logout();
    } else {
      this.router.navigate([routesMap[route] || '/dashboard']);
    }
  }

  ngOnInit(): void {
    const storedUser = this.userService.getLoggedInUser();
    if (storedUser) {
      this.username = storedUser.username;
      this.income = storedUser.salary || 0;

      if (storedUser.profilePicture) {
        this.profilePictureUrl = `http://localhost:8080/user/profile-picture/${storedUser.profilePicture}`;
      }

      this.userService.getBudgetsByUsername(this.username).subscribe({
        next: (data: any) => {
          if (Array.isArray(data) && data.length > 0) {
            this.cards = data.map((budget: any) => ({
              cardnumber: budget.cardnumber,
              amount: budget.amount
            }));
          } else {
            this.cards = [];
          }
        },
        error: (err) => {
          console.error('Error fetching user cards:', err);
          this.cards = [];
        }
      });
    } else {
      this.router.navigate(['/login-user']);
    }
  }

  logout(): void {
    if (this.username) {
      this.http.post('http://localhost:8080/user/logout', { username: this.username }).subscribe({
        next: () => {
          console.log('User logout recorded.');
          localStorage.removeItem('user');
          this.router.navigate(['/start']);
        },
        error: (err) => {
          console.error('Error during logout:', err);
          localStorage.removeItem('user');
          this.router.navigate(['/start']);
        }
      });
    } else {
      localStorage.removeItem('user');
      this.router.navigate(['/start']);
    }
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadProfilePicture() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.http.post<any>(`http://localhost:8080/user/${this.username}/upload-profile-picture`, formData).subscribe({
        next: (response) => {
          this.profilePictureUrl = `http://localhost:8080/user/profile-picture/${response.filename}`;

          const storedUser = JSON.parse(localStorage.getItem('user') || '{}');
          storedUser.profilePicture = response.filename;
          localStorage.setItem('user', JSON.stringify(storedUser));
        },
        error: (err) => {
          console.error('Error uploading profile picture:', err);
        }
      });
    }
  }

  toggleChangePassword(): void {
    this.showChangePassword = !this.showChangePassword;
    this.passwordMessage = '';
    this.newPassword = '';
    this.confirmPassword = '';
  }

  updatePassword(): void {
    if (this.newPassword !== this.confirmPassword) {
      this.passwordMessage = "Passwords do not match.";
      return;
    }

    this.userService.changePassword(this.username, this.newPassword).subscribe({
      next: () => {
        this.passwordMessage = "Password updated successfully.";
        this.showChangePassword = false;
      },
      error: (err) => {
        console.error('Error updating password:', err);
        this.passwordMessage = "Error updating password.";
      }
    });
  }

  toggleWallet(): void {
    this.walletOpen = !this.walletOpen;
  }
}
