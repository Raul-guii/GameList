import { Component } from '@angular/core';
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
export class ProfileEditComponent {

  name = '';
  loading = false;

  constructor(private userService : UserService, private router : Router){}

  ngOnInit(): void{
    this.userService.getProfile().subscribe(profile => {
      this.name = profile.name;
    });
  }

  save(){
     this.loading = true;

    this.userService.updateUserName(this.name).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/profile']);
      },
      error: err => {
        this.loading = false;
        console.error(err);
        alert('Erro ao salvar perfil');
      }
    });
  }

  cancel() {
    this.router.navigate(['/profile']);
  }
}
