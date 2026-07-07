import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { GameDTO } from '../../../models/game.model';
import { GameService } from '../../../services/game.service';
import { FavoriteService } from '../../../services/favorite.service';
import { getHighResCoverUrl } from '../../../shared/utils/cover-url.util';
import { CommentsComponent } from '../../../components/comments/comments.component';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-game-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, CommentsComponent],
  templateUrl: './game-detail.component.html',
  styleUrl: './game-detail.component.css'
})
export class GameDetailComponent implements OnInit {
  game?: GameDTO;
  loading = true;
  error?: string;
  isFavorite = false;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private favoriteService: FavoriteService,
    private authService: AuthService
  ) {}

  getHighResCover = getHighResCoverUrl;

  getGenreNames(game: GameDTO): string {
    if (!game.genres) return '—';
    return game.genres.map(g => g.name).join(', ');
  }

  toggleFavorite() {
    if (!this.game?.id) return;

    const gameId = this.game.id;

    const action$ = this.isFavorite
      ? this.favoriteService.removeFavorite(gameId)
      : this.favoriteService.addFavorite(gameId);

    action$.subscribe({
      next: () => {
        this.isFavorite = !this.isFavorite;
      },
      error: (err: any) => {
        console.error('Erro ao favoritar:', err);
      }
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.error = 'ID Inválido';
      this.loading = false;
      return;
    }

    this.gameService.getById(id).subscribe({
      next: game => {
        this.game = game;
        this.loading = false;

        if (this.authService.isLoggedIn()) {
          this.favoriteService.isFavorite(id).subscribe(isFav => {
            this.isFavorite = isFav;
          });
        }
      },
      error: err => {
        console.error(err);
        this.error = 'Erro ao carregar jogo';
        this.loading = false;
      }
    });
  }
}