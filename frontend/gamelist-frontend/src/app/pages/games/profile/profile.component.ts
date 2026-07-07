import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { UserDTO } from '../../../models/user.model';
import { UserService } from '../../../services/user.service';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-profile.component',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {

  user?: UserDTO;
  loading = false;
  error = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    this.loading = true;

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

  get initials(): string {
    if (!this.user?.name) return '?';
    return this.user.name
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map(part => part[0]?.toUpperCase())
      .join('');
  }

  deactivateAccount() {
    const confirmacao = confirm(
      'Tem certeza que deseja desativar sua conta? Você poderá reativá-la depois entrando em contato com o suporte.'
    );

    if (!confirmacao) return;

    this.userService.deactivateAccount().subscribe({
      next: () => {
        alert('Conta desativada com sucesso');
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: err => {
        console.error(err);
        alert('Erro ao desativar conta');
      }
    });
  }

  deleteAccount() {
    const confirmacao = confirm(
      'Tem certeza que deseja excluir sua conta permanentemente? Esta ação não pode ser desfeita.'
    );

    if (!confirmacao) return;

    this.userService.deleteAccount().subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: err => {
        console.error(err);
        alert('Erro ao excluir conta');
      }
    });
  }
}