import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Admin {
  id?: number;
  username: string;
  password?: string;
  income: number;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  getLoggedInAdmin(): Admin | null {
    const adminJson = sessionStorage.getItem('admin');
    return adminJson ? JSON.parse(adminJson) : null;
  }

  setLoggedInAdmin(admin: Admin): void {
    sessionStorage.setItem('admin', JSON.stringify(admin));
  }

  logout(): void {
    sessionStorage.removeItem('admin');
  }
}
