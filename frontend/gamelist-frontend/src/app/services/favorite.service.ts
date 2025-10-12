import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Game } from '../models/game.model';

@Injectable({ providedIn: 'root' })
export class FavoriteService {
  private STORAGE_KEY = 'my_favorites_v1';
  private _favorites = new BehaviorSubject<Game[]>(this.load());
  favorites$ = this._favorites.asObservable();

  private load(): Game[] {
    try {
      const raw = localStorage.getItem(this.STORAGE_KEY);
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }

  private save(list: Game[]) {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(list));
  }

  add(game: Game) {
    const list = this._favorites.getValue();
    if (!list.find(g => g.id === game.id)) {
      const next = [...list, game];
      this._favorites.next(next);
      this.save(next);
    }
  }

  remove(gameId: number) {
    const next = this._favorites.getValue().filter(g => g.id !== gameId);
    this._favorites.next(next);
    this.save(next);
  }

  isFavorite(gameId: number) {
    return this._favorites.getValue().some(g => g.id === gameId);
  }
}
