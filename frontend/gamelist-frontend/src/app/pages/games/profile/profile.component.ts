import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Route, Router, RouterModule } from '@angular/router';
import { UserDTO } from '../../../models/user.model';
import { UserService } from '../../../services/user.service';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-profile.component',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit{

  user?: UserDTO;
  loading = false;
  error = '';
  newName = '';

   constructor(private userService : UserService, private router: Router, private authService : AuthService, private http : HttpClient){}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(){
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Erro ao carregar perfil';
        this.loading = false;
      }
    });
  }

  saveName(){
    this.loading = true;

    this.userService.updateUserName(this.newName).subscribe({
      next: () => {
        this.loading = false;
        alert('Nome atualizado com sucesso!');
      },
      error: err => {
        this.loading = false;
        console.error(err);
        alert('Erro ao atualizar nome')
      }
    });
  }

  deactivateAccount() {
    const confirmacao = confirm(
      'Tem certeza que deseja desativar sua conta?'
    );

    if (!confirmacao) return;

    this.userService.deactivateAccount().subscribe({
      next: () => {
        alert('Conta desativada com sucesso');
        this.router.navigate(['/login']);
      }
    });
  }

deleteAccount() {
  this.userService.deleteAccount().subscribe({
    next: () => {
      this.authService.logout(); 
      this.router.navigate(['/login']);
    },
    error: err => {
      console.error(err);
    }
  });
}


}
