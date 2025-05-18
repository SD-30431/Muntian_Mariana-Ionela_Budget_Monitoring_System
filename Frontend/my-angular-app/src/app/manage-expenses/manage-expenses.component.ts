import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ProductService, Product, Category as ProductCategory } from '../services/product.service';
import { CategoryService } from '../services/category.service';
import { UserService } from '../services/user.service';
import { BudgetService, Budget } from '../services/budget.service';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-manage-expenses',
  standalone: true,
  templateUrl: './manage-expenses.component.html',
  styleUrls: ['./manage-expenses.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ]
})
export class ManageExpensesComponent implements OnInit {
  expenseForm!: FormGroup;
  currentAction: 'add' | 'edit' | 'delete' | 'none' = 'none';
  cardNumbers: string[] = [];
  categories: ProductCategory[] = [];
  private oldPrice: number = 0;
  username: string = '';
  formErrorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService,
    private router: Router,
    private userService: UserService,
    private budgetService: BudgetService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    const user = this.userService.getLoggedInUser();
    if (user) {
      this.username = user.username;
      this.loadUserCards();
    } else {
      this.router.navigate(['/login-user']);
    }

    this.loadCategories();

    const todayStr = new Date().toISOString().substring(0, 10);
    this.expenseForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0.01)]],
      cardNo: ['', Validators.required],
      date: [todayStr, Validators.required],
      category: [null, Validators.required]
    });
  }

 setAction(action: 'add' | 'edit' | 'delete' | 'none') {
  this.currentAction = action;
  this.formErrorMessage = '';

  if (action === 'delete') {
    this.expenseForm.reset(); 
  } else {
    this.expenseForm.reset();
    this.expenseForm.patchValue({ date: new Date().toISOString().substring(0, 10) });

    if (action === 'edit') {
      const priceVal = this.expenseForm.get('price')?.value;
      this.oldPrice = priceVal ? parseFloat(priceVal) : 0;
    }
  }
}


  getActionButtonText(): string {
    switch (this.currentAction) {
      case 'add': return 'Save';
      case 'edit': return 'Update';
      case 'delete': return 'Delete';
      default: return '';
    }
  }

  onSubmit() {
    if (this.expenseForm.invalid) {
      this.formErrorMessage = 'Please correct the errors in the form.';
      this.expenseForm.markAllAsTouched();
      return;
    }

    const data = this.expenseForm.value;
    const user = this.userService.getLoggedInUser();

    if (!user || !user.id) {
      alert('User not found. Please log in again.');
      this.router.navigate(['/login-user']);
      return;
    }

    const productId = data.id && data.id.toString().trim() !== '' ? data.id : undefined;

    const product: Product = {
      id: productId,
      name: data.name,
      price: data.price,
      date: data.date,
      category: data.category,
      user: {
        id: user.id,
        username: user.username
      }
    };

    const selectedCard = data.cardNo;

    if (this.currentAction === 'add') {
      this.productService.addProduct(product).subscribe(
        addedProduct => {
          alert('Product added!');
          this.updateBudget(selectedCard, addedProduct.price, 'subtract');
          this.setAction('none');
        },
        err => {
          console.error('Failed to add product:', err);
          this.formErrorMessage = err.error || 'Failed to add product.';
        }
      );
    } else if (this.currentAction === 'edit') {
      this.productService.updateProduct(product).subscribe(
        updatedProduct => {
          alert('Product updated!');
          const diff = updatedProduct.price - this.oldPrice;
          const operation = diff >= 0 ? 'subtract' : 'add';
          this.updateBudget(selectedCard, Math.abs(diff), operation);
          this.setAction('none');
        },
        err => {
          console.error('Failed to update product:', err);
          this.formErrorMessage = err.error || 'Failed to update product.';
        }
      );
    } else if (this.currentAction === 'delete') {
      if (product.id) {
        this.productService.deleteProduct(product.id).subscribe(
          () => {
            alert('Product deleted!');
            this.updateBudget(selectedCard, product.price, 'add');
            this.setAction('none');
          },
          err => {
            console.error('Failed to delete product:', err);
            this.formErrorMessage = err.error || 'Failed to delete product.';
          }
        );
      } else {
        this.formErrorMessage = 'Please enter a valid Product ID for deletion.';
      }
    }
  }

  updateBudget(cardNumber: string, amount: number, operation: 'add' | 'subtract') {
    if (!cardNumber) return;
    this.budgetService.getBudgetByCardNumber(cardNumber).subscribe({
      next: (budget: Budget) => {
        if (budget) {
          budget.amount = operation === 'subtract'
            ? budget.amount - amount
            : budget.amount + amount;
          this.budgetService.updateBudget(budget.id!, budget).subscribe();
        }
      }
    });
  }

  loadUserCards() {
    const user = this.userService.getLoggedInUser();
    if (user?.username) {
      this.userService.getBudgetsByUsername(user.username).subscribe({
        next: (data: any) => {
          this.cardNumbers = data.map((b: any) => b.cardnumber);
        }
      });
    }
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data as ProductCategory[];
      },
      error: (err) => {
        console.error('Failed to load categories:', err);
        alert('Failed to load categories.');
      }
    });
  }

  navigateTo(route: string) {
    const map: { [key: string]: string } = {
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
      this.router.navigate([map[route] || '/dashboard']);
    }
  }
  
  isInvalid(controlName: string): boolean {
  const control = this.expenseForm.get(controlName);
  return control ? control.invalid && control.touched : false;
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
