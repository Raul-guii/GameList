import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { FavoriteService } from '../../../services/favorite.service';

@Component({
  selector: 'app-favorite.component',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.css'
})
export class FavoriteComponent {

  favorites: FavoriteComponent[] = [];
  loading = false;
  error?: string;
  private destroy$ = new Subject<void>();

  constructor(private httpClient : HttpClient, private favoriteService : FavoriteService){}
  
  
}
