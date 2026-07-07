import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GameDTO } from '../../../models/game.model';
import { GameService } from '../../../services/game.service';
import { FormsModule } from '@angular/forms';
import { getHighResCoverUrl } from '../../../shared/utils/cover-url.util';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent implements OnInit {
  games: GameDTO[] = [];
  loading = true;
  error?: string;
  keyword: string = '';

  constructor(private gameService: GameService) {}

  load(): void {
    this.loading = true;
    this.gameService.getAll().subscribe({
      next: data => { this.games = data; this.loading = false; },
      error: err => { this.error = 'Erro ao carregar jogos'; console.error(err); this.loading = false; }
    });
  }

  onCoverError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = '/assets/placeholder.svg';
    img.onerror = null;
  }

  getHighResImage(url: string | undefined): string {
    return getHighResCoverUrl(url) ?? '/assets/placeholder.svg';
  }

  search(): void {
    if (!this.keyword.trim()) {
      this.load();
      return;
    }

    this.gameService.searchGames(this.keyword).subscribe({
      next: (dados) => this.games = dados,
      error: (erro) => console.error(erro)
    });
  }

  ngOnInit(): void {
    this.load();
  }
}