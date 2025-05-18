import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Category {
  id: number;
  name: string;
}

export interface Product {
  id?: number;
  name: string;
  price: number;
  date: string;
  category: Category;
  user?: {
    id: number;
    username?: string;
  };
}

export interface CategoryExpenseRequest {
  categoryName: string;
  total: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/product`;

  constructor(private http: HttpClient) {}

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}/create`, product);
  }

  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/update/${product.id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/all`);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  getProductsByCategory(categoryName: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${categoryName}`);
  }

  getProductsByDate(date: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/date/${date}`);
  }

  getExpensesGroupedByCategory(): Observable<CategoryExpenseRequest[]> {
    return this.http.get<CategoryExpenseRequest[]>(`${this.apiUrl}/grouped-by-category`);
  }

  exportProductHistoryAsXML(username: string): Observable<Blob> {
    return this.http.get(`http://localhost:8080/user/${username}/export-history`, {
      responseType: 'blob'
    });
  }

  getProductsByUser(userId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/user/${userId}`);
  }
}
