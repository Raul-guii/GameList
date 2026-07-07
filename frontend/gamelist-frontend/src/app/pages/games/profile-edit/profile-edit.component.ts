import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile-edit.component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile-edit.component.html',
  styleUrl: './profile-edit.component.css',
})
export class ProfileEditComponent implements OnInit {

  name = '';
  loadingProfile = true;
  saving = false;
  errorMessage = '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getProfile().subscribe({
      next: (profile) => {
        this.name = profile.name;
        this.loadingProfile = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar perfil';
        this.loadingProfile = false;
      }
    });
  }

  save() {
    this.errorMessage = '';
    this.saving = true;

    this.userService.updateUserName(this.name).subscribe({
      next: () => {
        this.saving = false;
        this.router.navigate(['/profile']);
      },
      error: () => {
        this.saving = false;
        this.errorMessage = 'Erro ao salvar perfil. Tente novamente.';
      }
    });
  }

  cancel() {
    this.router.navigate(['/profile']);
  }
}