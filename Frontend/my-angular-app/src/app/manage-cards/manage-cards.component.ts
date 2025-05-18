import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { BudgetService, Budget } from '../services/budget.service';
import { UserBudgetService } from '../services/user-budget.service';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-manage-cards',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, HttpClientModule],
  templateUrl: './manage-cards.component.html',
  styleUrls: ['./manage-cards.component.css'],
})
export class ManageCardsComponent implements OnInit {
  cardNo: string = '';
  amount: number = 0;
  showForm: boolean = false;
  username: string = '';

  constructor(
    private router: Router,
    private budgetService: BudgetService,
    private userBudgetService: UserBudgetService,
    private userService: UserService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const user = this.userService.getLoggedInUser();
    if (user) {
      this.username = user.username;
    } else {
      this.router.navigate(['/login-user']);
    }
  }

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

  logout(): void {
    if (this.username) {
      this.http.post('http://localhost:8080/user/logout', { username: this.username }).subscribe({
        next: () => {
          console.log('Logout recorded.');
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

  saveCard(): void {
  const cardNoInput = document.querySelector('input[name="cardNo"]') as HTMLInputElement;
  const amountInput = document.querySelector('input[name="amount"]') as HTMLInputElement;

  // Touch inputs to trigger validation
  if (cardNoInput) cardNoInput.dispatchEvent(new Event('blur'));
  if (amountInput) amountInput.dispatchEvent(new Event('blur'));

  if (
    !this.cardNo ||
    this.cardNo.length < 1 ||
    this.cardNo.length > 20 ||
    this.amount == null ||
    this.amount < 0
  ) {
    alert('Please enter a valid card number and a non-negative amount.');
    return;
  }

  const user: User | null = this.userService.getLoggedInUser();
  if (!user || !user.username) {
    alert('You must be logged in.');
    return;
  }

  const newBudget: Budget = {
    cardnumber: this.cardNo,
    amount: this.amount,
  };

  this.budgetService.addBudget(newBudget).subscribe({
    next: (budgetResponse) => {
      if (!budgetResponse.cardnumber) {
        alert('Failed to save card.');
        return;
      }

      this.userBudgetService.linkUserToBudget(user.username, budgetResponse.cardnumber).subscribe({
        next: () => {
          alert('Card added and linked!');
          this.cardNo = '';
          this.amount = 0;
          this.showForm = false;
        },
        error: (err) => {
          console.error('Failed to link card to user:', err);
        }
      });
    },
    error: (err) => {
      console.error('Error saving card:', err);
    }
  });
}


}
