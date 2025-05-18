import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';


export interface Budget {
  id?: number;
  cardnumber: string;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  // Update the URL to match the backend mapping: "/budget"
  private apiUrl = `${environment.apiUrl}/budget`;
  

  constructor(private http: HttpClient) {}

  addBudget(budget: Budget): Observable<Budget> {
    return this.http.post<Budget>(`${this.apiUrl}/create`, budget);
  }

  getBudgetByCardNumber(cardnumber: string): Observable<Budget> {
    return this.http.get<Budget>(`${this.apiUrl}/${cardnumber}`);
  }

  getAllBudgets(): Observable<Budget[]> {
    return this.http.get<Budget[]>(`${this.apiUrl}`);
  }

  updateBudget(id: number, budget: Budget): Observable<Budget> {
    return this.http.put<Budget>(`${this.apiUrl}/${id}`, budget);
  }

  deleteBudget(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
