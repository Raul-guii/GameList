import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { GameDTO } from '../../../models/game.model';
import { GameService } from '../../../services/game.service';
import { FavoriteComponent } from '../favorite/favorite.component';
import { FavoriteService } from '../../../services/favorite.service';

@Component({
  selector: 'app-game-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './game-detail.component.html',
  styleUrl: './game-detail.component.css'
})
export class GameDetailComponent {
  game?: GameDTO;
  loading = true;
  error?: string;
  isFavorite = false;

  constructor(private route: ActivatedRoute,
              private gameService: GameService,
              private favoriteService: FavoriteService){}

  getHighResCover(url: string | undefined) : string | undefined{
    if (!url) return undefined;

    if (url?.includes('t_thumb')){
      return url.replace('t_thumb', 't_cover_big');
    }

    if (url.includes('/crop/')){
      return url.replace(/\/crop\/\d+\//, '/resize/640/');
    }

    return url;
  }

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

  ngOnInit(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if  (!id) {
      this.error = 'ID Inválido';
      this.loading = false;
      return;
    }

    this.gameService.getById(id).subscribe({
      next: game => {
        this.game =  game;
        this.loading = false;

        
      this.favoriteService.isFavorite(id).subscribe(isFav => {
        this.isFavorite = isFav;
      });
      },
      error: err => {
        console.error(err);
        this.error = 'Erro ao carregar jogo';
        this.loading = false;
      }
    });
  }

}
