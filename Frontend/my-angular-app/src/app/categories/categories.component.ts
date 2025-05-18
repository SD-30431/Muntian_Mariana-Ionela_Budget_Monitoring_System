import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CategoryService, Category } from '../services/category.service';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent {
  categoryName: string = '';
  categories: Category[] = [];
  showForm: boolean = false;
  formMessage: string = '';

  constructor(private router: Router, private categoryService: CategoryService) {}

  navigateTo(route: string): void {
    const routesMap: { [key: string]: string } = {
      'main-admin': '/main-admin',
      'categories': '/categories',
      'activity-log': '/activity-log'
    };
    this.router.navigate([routesMap[route] || '/main-admin']);
  }

  showCategoryForm(): void {
    this.showForm = true;
    this.formMessage = '';
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data: Category[]) => {
        console.log('Categories loaded:', data);
        this.categories = data;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.formMessage = 'Failed to load categories.';
      }
    });
  }

  saveCategory(): void {
    if (!this.categoryName.trim()) {
      this.formMessage = 'Category name is required.';
      return;
    }

    const newCategory: Category = { name: this.categoryName };

    this.categoryService.createCategory(newCategory).subscribe({
      next: (createdCategory) => {
        console.log('Category saved:', createdCategory);
        this.formMessage = 'Category saved successfully!';
        this.categoryName = '';
        this.loadCategories();
      },
      error: (error) => {
        console.error('Error saving category:', error);
        this.formMessage = 'Failed to save category.';
      }
    });
  }
}
