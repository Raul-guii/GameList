import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { catchError, map, Observable, throwError } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Game } from '../models/game.model';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private base = `${environment.apiUrl}/games`;

  constructor(private http: HttpClient){}
  
  getGamesPage(page: number = 1, size: number = 6): Observable<{ items: Game[]; page: number; size: number }> {

    let params = new HttpParams().set('page', String(page)).set('size', String(size));
    
    return this.http.get<any[]>(this.base, { params }).pipe(
      map(raw => {
        const items: Game[] = (raw || []).map(r => ({
          id: r.id,
          name: r.name,
          summary: r.summary,
          rating: r.rating,
          coverUrl: r.cover?.url ? r.cover.url.replace(/^\/+/,'https://') : r.coverUrl || null,
          genres: r.genres
        }));
        return { items, page, size };
      })
    );
  }

  getGamesById(id: number): Observable<Game>{
    return this.http.get<any>(`${this.base}/${id}`).pipe(
      map(r => ({
        id: r.id,
        name: r.id,
        summary: r.summary,
        rating: r.rating,
        coverUrl: r.cover?.url ? r.cover.url.replace(/^\/+/,'https://') : r.coverUrl || null,
        genres: r.genres
      }))
    );
  }

  searchGames(keyword: string, page = 1, size = 10){
    const params = new HttpParams().set('keyword', keyword).set('page', String(page)).set('size', String(size));
    return this.http.get<any[]>(`${this.base}/search`, { params }).pipe(
      map(raw => ({ items: (raw || []).map(r => ({
        id: r.id, name: r.name, summary: r.summary, rating: r.rating,
        coverUrl: r.cover?.url ? r.cover.url.replace(/^\/+/,'https://') : r.coverUrl || null,
        genres: r.genres
      })), page, size }))
    );
  }

}
