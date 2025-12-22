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
              private svc: GameService,
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
    const game_id = this.game?.id;

    if (!game_id) return;

    const action$ = this.isFavorite
      ? this.favoriteService.removeFavorite(game_id)
      : this.favoriteService.addFavorite(game_id);

      action$.subscribe(() => {
        this.isFavorite = !this.isFavorite;
      });
  }

  ngOnInit(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id){
      this.svc.getById(id).subscribe({
        next: g => { this.game = g; this.loading = false; },
        error: err => { this.error = 'Erro ao carregar jogo'; console.error(err); this.loading = false; }
      });
    } else {
      this.error = 'ID inválido';
      this.loading = false;
    }

    this.favoriteService.isFavorite(id).subscribe(isFav => {
      this.isFavorite = isFav;
    })
  }
}
