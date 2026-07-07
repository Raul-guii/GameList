import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { forkJoin, Subject, takeUntil } from 'rxjs';
import { FavoriteService } from '../../../services/favorite.service';
import { GameDTO } from '../../../models/game.model';
import { FavoriteDTO } from '../../../models/favorite.model';
import { getHighResCoverUrl } from '../../../shared/utils/cover-url.util';

@Component({
  selector: 'app-favorite.component',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.css'
})
export class FavoriteComponent implements OnInit, OnDestroy {

  contextMenuVisible = false;
  contextMenuX = 0;
  contextMenuY = 0;
  selectedFavorite?: GameDTO;
  favorites: GameDTO[] = [];
  favoriteLinks: FavoriteDTO[] = [];
  loading = false;
  error?: string;
  private destroy$ = new Subject<void>();

  constructor(private favoriteService: FavoriteService) {}

  getHighResCover = getHighResCoverUrl;

  onRightClick(event: MouseEvent, game: GameDTO) {
    event.preventDefault();

    this.selectedFavorite = game;
    this.contextMenuX = event.clientX;
    this.contextMenuY = event.clientY;
    this.contextMenuVisible = true;
  }

  removeSelectedFavorite() {
    if (!this.selectedFavorite) return;

    const fav = this.favoriteLinks.find(
      f => f.gameId === this.selectedFavorite!.id
    );
    if (!fav) return;

    this.favoriteService.removeFavorite(fav.gameId).subscribe(() => {
      this.favorites = this.favorites.filter(
        g => g.id !== this.selectedFavorite!.id
      );
      this.contextMenuVisible = false;
      this.selectedFavorite = undefined;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.loading = true;

    this.favoriteService.getFavorite().pipe(takeUntil(this.destroy$))
      .subscribe({
        next: favs => {
          this.favoriteLinks = favs;

          const requests = favs.map(f => this.favoriteService.getGameCached(f.gameId));

          forkJoin(requests).subscribe({
            next: games => {
              this.favorites = games;
              this.loading = false;
            },
            error: () => {
              this.error = 'Erro ao carregar jogos favoritos';
              this.loading = false;
            }
          });
        },
        error: () => {
          this.error = 'Erro ao carregar favoritos';
          this.loading = false;
        }
      });

    document.addEventListener('click', () => {
      this.contextMenuVisible = false;
    });
  }
}