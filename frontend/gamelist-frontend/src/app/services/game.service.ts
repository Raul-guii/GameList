import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GameDTO } from '../models/game.model';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private base = environment.apiUrl;

  private gameCache = new Map<number, Observable<GameDTO>>();

  constructor(private http : HttpClient){}
  
  getAll(): Observable<GameDTO[]>{
    return this.http.get<GameDTO[]>(`${this.base}/games`);
  }

  getById(id: number):Observable<GameDTO>{
    return this.http.get<GameDTO>(`${this.base}/games/${id}`);
  }

  searchGames(keyword: string): Observable<GameDTO[]>{
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<GameDTO[]>(`${this.base}/games/search`, { params });
  }

  getByGenres(genresIds: number[] = []): Observable<GameDTO[]>{
    const params = new HttpParams().set('genres', genresIds.join(','));
    return this.http.get<GameDTO[]>(`${this.base}/games/genres`,{ params });
  }
}
