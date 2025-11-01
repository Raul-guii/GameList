import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login.component',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: (res) => {
      console.log('login response ->', res);
      this.authService.saveToken(res.token); 
      console.log('after save -> localStorage.token =', localStorage.getItem('token'));
      this.router.navigate(['/games']);
      },
      error: () => {
        this.errorMessage = 'Invalid credentials';
      }
    });
  }
}
