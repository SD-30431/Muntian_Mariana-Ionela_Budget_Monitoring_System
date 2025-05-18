import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ProductService, Product } from '../services/product.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, HttpClientModule],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  categories: string[] = [];
  selectedCategory: string = 'All';
  selectedDate: string = '';
  username: string = '';
  userId: number | null = null;

  constructor(
    private router: Router,
    private productService: ProductService,
    private userService: UserService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const user = this.userService.getLoggedInUser();
    if (user && user.id) {
      this.username = user.username;
      this.userId = user.id;
      this.loadProducts();
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

  loadProducts(): void {
    if (!this.userId) {
      console.error('User ID not available.');
      return;
    }

    this.productService.getProductsByUser(this.userId).subscribe({
      next: (data) => {
        this.products = data;
        this.filteredProducts = data;
        this.categories = Array.from(
          new Set(data.map(p => p.category?.name).filter((name): name is string => !!name))
        );
      },
      error: (err) => {
        console.error('Failed to load user-specific products:', err);
      }
    });
  }

  applyFilters(): void {
    this.filteredProducts = this.products.filter(p => {
      const categoryMatch = this.selectedCategory === 'All' || p.category?.name === this.selectedCategory;
      const dateMatch = !this.selectedDate || p.date === this.selectedDate;
      return categoryMatch && dateMatch;
    });

    console.log('Filtered products:', this.filteredProducts);
  }

  exportAsXml(): void {
    if (!this.username) {
      console.error('Username not available for export.');
      return;
    }

    this.http.get(`http://localhost:8080/user/${this.username}/export-history`, {
      responseType: 'blob'
    }).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `product-history-${this.username}.xml`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Failed to export XML:', err);
      }
    });
  }
}
