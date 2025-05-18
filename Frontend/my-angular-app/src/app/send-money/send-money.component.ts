import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-send-money',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIf, NgFor, HttpClientModule, FormsModule],
  templateUrl: './send-money.component.html',
  styleUrls: ['./send-money.component.css']
})
export class SendMoneyComponent implements OnInit {
  userCards: { cardnumber: string; amount: number }[] = [];
  selectedFromCard: string = '';
  recipientCardNumber: string = '';
  amountToSend: number | null = null;
  message: string = '';

  username: string = '';

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
    this.router.navigate([routesMap[route] || '/dashboard']);
  }

  ngOnInit(): void {
    const storedUser = this.userService.getLoggedInUser();
    if (storedUser) {
      this.username = storedUser.username;

      this.userService.getBudgetsByUsername(this.username).subscribe({
        next: (data: any) => {
          if (Array.isArray(data)) {
            this.userCards = data.map((budget: any) => ({
              cardnumber: budget.cardnumber,
              amount: budget.amount
            }));
          }
        },
        error: (err) => {
          console.error('Error fetching user cards:', err);
        }
      });
    }
  }

  sendMoney(): void {
    if (!this.selectedFromCard || !this.recipientCardNumber || !this.amountToSend) {
      this.message = '❗ Please fill out all fields.';
      return;
    }

    const transferData = {
      fromCard: this.selectedFromCard,
      toCard: this.recipientCardNumber,
      amount: this.amountToSend
    };

    this.http.post<any>('http://localhost:8080/user/transfer', transferData).subscribe({
      next: (response) => {
        if (response && response.success) {
          this.message = '✅ ' + (response.message || 'Transfer successful!');
          this.amountToSend = null;
          this.recipientCardNumber = '';
          this.selectedFromCard = '';
        } else {
          this.message = '❗ ' + (response.message || 'Unexpected error during transfer.');
        }
      },
      error: (err) => {
        console.error('Error sending money:', err);
        if (err.error && err.error.message) {
          this.message = '❗ ' + err.error.message;
        } else {
          this.message = '❗ Error sending money.';
        }
      }
    });
  }
}
