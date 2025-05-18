import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-start',
  standalone: true, 
  imports: [CommonModule], 
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent {
  constructor(private router: Router) { }

  loginUser(): void {
    this.router.navigate(['/login-user']);
  }

  loginAdmin(): void {
    this.router.navigate(['/login-admin']);
  }

  createNewAccount(): void {
    this.router.navigate(['/sign-up']);
  }
}
