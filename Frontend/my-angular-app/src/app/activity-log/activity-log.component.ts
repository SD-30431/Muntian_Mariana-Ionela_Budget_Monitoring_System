import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../services/admin.service';

@Component({
  selector: 'app-activity-log',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, NgIf, NgFor, FormsModule],
  templateUrl: './activity-log.component.html',
  styleUrls: ['./activity-log.component.css']
})
export class ActivityLogComponent implements OnInit {
  activityLogs: any[] = [];
  username: string = '';

  constructor(private http: HttpClient, private router: Router, private adminService: AdminService) {}

  ngOnInit(): void {
    const storedAdmin = this.adminService.getLoggedInAdmin();
    if (storedAdmin) {
      this.username = storedAdmin.username;
    } else {
      this.router.navigate(['/login-admin']);
    }

    this.fetchActivityLogs();
  }

  fetchActivityLogs(): void {
    this.http.get<any[]>('http://localhost:8080/user/activity-logs').subscribe({
      next: (data) => {
        this.activityLogs = data;
      },
      error: (err) => {
        console.error('Error fetching activity logs:', err);
      }
    });
  }

  navigateTo(route: string): void {
    const routesMap: { [key: string]: string } = {
      'main-admin': '/main-admin',
      'categories': '/categories',
      'activity-log': '/activity-log'
    };
    this.router.navigate([routesMap[route] || '/main-admin']);
  }
}
