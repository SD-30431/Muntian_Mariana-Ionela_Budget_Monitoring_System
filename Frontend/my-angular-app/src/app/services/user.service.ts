import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface User {
  id?: number;
  username: string;
  password?: string;
  salary: number;
  userBudgets?: any[];
  profilePicture?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/user`;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  signUp(userData: { username: string; password: string; income: number }): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, userData);
  }

  getUserDetails(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${username}`);
  }

  getBudgetsByUsername(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${username}/budgets`);
  }

  setLoggedInUser(user: User): void {
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  getLoggedInUser(): User | null {
    const userJson = sessionStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  logout(): void {
    sessionStorage.removeItem('user');
  }

  changePassword(username: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/changePassword`, { username, newPassword });
  }
}
