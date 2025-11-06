import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GameDTO } from '../../models/game.model';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent {
  games: GameDTO[] = [];
  loading = true;
  error?: string;

  constructor(private svc: GameService) {}

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: data => { this.games = data; this.loading = false; console.log('Games recebidos da API: ', this.games)},
      error: err => { this.error = 'Erro carregar jogos'; console.error(err); this.loading = false; }
    });
  }

  onCoverError(event: Event){
    const img = event.target as HTMLImageElement;
    img.src = '/assets/placeholder.svg';
    img.onerror = null;
  }

getHighResImage(url: string | undefined): string {
  if (!url) return 'assets/placeholder.svg';
  if (url.startsWith('//')) url = 'https:' + url;
  if (url.startsWith('/')) url = 'https://media.rawg.io' + url;
  if (url.startsWith('media.')) url = 'https://' + url;
  if (url.includes('media.rawg.io')) {
    url = url
      .replace('/crop/200/', '/resize/640/')
      .replace('/crop/300/', '/resize/640/')
      .replace('/crop/400/', '/resize/800/');
  }

  if (url.includes('images.igdb.com')) {
    url = url.replace('t_thumb', 't_cover_big');
  }

  return url;
  }

  ngOnInit(): void{
    this.load();
  }

  
}
