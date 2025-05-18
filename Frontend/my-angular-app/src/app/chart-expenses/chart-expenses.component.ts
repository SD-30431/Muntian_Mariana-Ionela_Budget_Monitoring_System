import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NgChartsModule } from 'ng2-charts';
import { ChartData } from 'chart.js';
import { ProductService, CategoryExpenseRequest } from '../services/product.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-chart-expenses',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, NgChartsModule],
  templateUrl: './chart-expenses.component.html',
  styleUrls: ['./chart-expenses.component.css']
})
export class ChartExpensesComponent implements OnInit {
  chartData: ChartData<'pie'> = {
    labels: [],
    datasets: [{ data: [] }]
  };

  username: string = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private productService: ProductService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const storedUser = this.userService.getLoggedInUser();
    if (storedUser) {
      this.username = storedUser.username;
    } else {
      this.router.navigate(['/login-user']);
    }

    this.loadExpensesChart();
  }

  loadExpensesChart(): void {
    this.productService.getExpensesGroupedByCategory().subscribe({
      next: (response: CategoryExpenseRequest[]) => {
        const labels: string[] = response.map(entry => entry.categoryName || 'Uncategorized');
        const data: number[] = response.map(entry => entry.total);
        this.chartData = {
          labels,
          datasets: [{ data }]
        };
      },
      error: (err) => {
        console.error('Failed to load chart data', err);
      }
    });
  }

  navigateTo(route: string): void {
    const routeMap: { [key: string]: string } = {
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
      this.router.navigate([routeMap[route] || '/dashboard']);
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
}
