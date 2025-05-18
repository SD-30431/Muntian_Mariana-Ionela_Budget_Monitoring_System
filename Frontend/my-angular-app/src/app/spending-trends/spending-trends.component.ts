// src/app/spending-trends/spending-trends.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NgChartsModule } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-spending-trends',
  standalone: true,
  imports: [CommonModule, RouterModule, NgChartsModule],
  templateUrl: './spending-trends.component.html',
  styleUrls: ['./spending-trends.component.css']
})
export class SpendingTrendsComponent implements OnInit {
  username: string = '';

  chartData: ChartData<'line'> = {
    labels: [],
    datasets: [
      {
        label: 'Expenses',
        data: [],
        borderColor: '#008080',
        backgroundColor: 'rgba(0,128,128,0.2)',
        fill: true,
        tension: 0.4
      }
    ]
  };

  chartOptions: ChartOptions<'line'> = {
    responsive: true,
    plugins: {
      legend: {
        labels: {
          color: '#008080'
        }
      }
    },
    scales: {
      x: {
        ticks: { color: '#333' }
      },
      y: {
        ticks: { color: '#333' }
      }
    }
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      this.router.navigate(['/login-user']);
      return;
    }

    const user = JSON.parse(userStr);
    this.username = user.username;

    // Fetch dynamic chart data from backend
    this.http.get<any[]>(`http://localhost:8080/user/${this.username}/monthly-expenses`).subscribe({
      next: (response) => {
        // Ensure data is ordered by month (January to December)
        const orderedMonths = [
          'January', 'February', 'March', 'April', 'May', 'June',
          'July', 'August', 'September', 'October', 'November', 'December'
        ];

        const expenseMap: { [month: string]: number } = {};
        response.forEach(item => {
          expenseMap[item.month] = item.total;
        });

        const labels = orderedMonths;
        const values = orderedMonths.map(month => expenseMap[month] || 0);

        this.chartData.labels = labels;
        this.chartData.datasets[0].data = values;
      },
      error: (err) => {
        console.error('Failed to fetch monthly expenses:', err);
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
      localStorage.removeItem('user');
      this.router.navigate(['/start']);
    } else {
      this.router.navigate([routeMap[route]]);
    }
  }
}
