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

  constructor(private http : HttpClient){}
  
  getAll(): Observable<GameDTO[]>{
    return this.http.get<GameDTO[]>(this.base);
  }

  getById(id: number):Observable<GameDTO>{
    return this.http.get<GameDTO>(`${this.base}/${id}`);
  }

  searchGames(keyword: string): Observable<GameDTO[]>{
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<GameDTO[]>(`${this.base}/search`, { params });
  }

  getByGenres(genresIds: number[] = []): Observable<GameDTO[]>{
    const params = new HttpParams().set('genres', genresIds.join(','));
    return this.http.get<GameDTO[]>(`${this.base}/genres`,{ params });
  }
}
