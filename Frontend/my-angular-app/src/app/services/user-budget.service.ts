import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserBudget {
  id?: number;
  userId: number;
  budgetId: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserBudgetService {
  // Update the URL to match the backend mapping: "/userbudget"
  private apiUrl = `${environment.apiUrl}/userbudget`;

  constructor(private http: HttpClient) {}

  getAllUserBudgets(): Observable<UserBudget[]> {
    return this.http.get<UserBudget[]>(`${this.apiUrl}`);
  }

  getUserBudgetsByUserId(userId: number): Observable<UserBudget[]> {
    return this.http.get<UserBudget[]>(`${this.apiUrl}/user/${userId}`);
  }

  getUserBudgetsByBudgetId(budgetId: number): Observable<UserBudget[]> {
    return this.http.get<UserBudget[]>(`${this.apiUrl}/budget/${budgetId}`);
  }

  createUserBudget(userBudget: UserBudget): Observable<UserBudget> {
    return this.http.post<UserBudget>(`${this.apiUrl}`, userBudget);
  }

  deleteUserBudget(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // The endpoint for linking should match your UserBudgetController mapping
   // Updated linking method: sends query parameters 'username' and 'cardnumber'
   linkUserToBudget(username: string, cardnumber: string): Observable<UserBudget> {
    const url = `${this.apiUrl}/link?username=${encodeURIComponent(username)}&cardnumber=${encodeURIComponent(cardnumber)}`;
    return this.http.post<UserBudget>(url, {});
  }
}
