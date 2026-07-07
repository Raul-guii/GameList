import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment, CreateCommentRequest, UpdateCommentRequest } from '../models/comment.model';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private http = inject(HttpClient);

  private readonly api = `${environment.apiUrl}/comments`;

  getCommentsByGame(gameExternalId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.api}/games/${gameExternalId}`);
  }

createComment(gameId: number, request: CreateCommentRequest): Observable<Comment> {
    return this.http.post<Comment>(
        `${this.api}/games/${gameId}`,
        request
    );
}

updateComment(commentId: number, request: UpdateCommentRequest): Observable<Comment> {
    return this.http.patch<Comment>(
        `${this.api}/${commentId}`,
        request
    );
}

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${commentId}`);
  }

  getMyComments(): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.api}/me`);
  }

}