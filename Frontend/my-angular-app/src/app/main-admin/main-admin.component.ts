import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService } from '../services/admin.service';
import { HttpClient } from '@angular/common/http'; 

@Component({
  selector: 'app-main-admin',
  templateUrl: './main-admin.component.html',
  styleUrls: ['./main-admin.component.css']
})
export class MainAdminComponent implements OnInit {
  username: string = '';
  income: string = '';

  constructor(
    private router: Router, 
    private adminService: AdminService,
    private http: HttpClient 
  ) {}

  ngOnInit(): void {
    const admin = this.adminService.getLoggedInAdmin();
    if (admin) {
      this.username = admin.username;
      this.income = admin.income ? admin.income.toString() : '0';
    } else {
      this.router.navigate(['/login-admin']);
    }
  }

  onHome(): void {
    this.router.navigate(['/main-admin']);
  }

  onManageCategories(): void {
    this.router.navigate(['/categories']);
  }

  onActivityLog(): void {
    this.router.navigate(['/activity-log']);
  }

  onGoBack(): void {
    if (this.username) {
      const logEntry = { username: this.username, action: 'logged out' };
      this.http.post('http://localhost:8080/user/save-activity', logEntry).subscribe({
        next: () => {
          console.log('Logout activity saved successfully');
        },
        error: (error) => {
          console.error('Error saving logout activity:', error);
        }
      });
    }

    localStorage.removeItem('admin');

    this.router.navigate(['/start']);
  }
}
