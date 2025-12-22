import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { map, Observable, shareReplay } from 'rxjs';
import { FavoriteDTO } from '../models/favorite.model';
import { GameDTO } from '../models/game.model';
import { GameService } from './game.service';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
 
  private base = environment.apiUrl;
  
  //evitar multiplas requisições
  private gameCache = new Map<number, Observable<GameDTO>>();

  constructor(private http : HttpClient, private gameService : GameService){}

  getFavorite(): Observable<FavoriteDTO[]>{
    return this.http.get<FavoriteDTO[]>(`${this.base}/favorites`);
  }

  addFavorite(game_id : number): Observable<void>{
    return this.http.post<void>(`${this.base}/favorites/${game_id}`, {});
  }

  removeFavorite(game_id: number): Observable<void>{
    this.gameCache.delete(game_id);
    return this.http.delete<void>(`${this.base}/favorites/${game_id}`)
  }

  getGameCached(game_id: number): Observable<GameDTO>{
    const cached = this.gameCache.get(game_id);
    if (cached) return cached;
    const obs$ = this.gameService.getById(game_id).pipe(
      //shareReplay para evitar re-exucação por alta demanda
      shareReplay({ bufferSize: 1, refCount: true})
    );
    this.gameCache.set(game_id, obs$);
    return obs$;
  }

  isFavorite(game_id: number): Observable<boolean>{
    return this.getFavorite().pipe(
      map(favorites => favorites.some(f => f.game_id == game_id))
    );
  }
}
