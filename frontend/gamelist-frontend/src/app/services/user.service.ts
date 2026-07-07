import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/me`;

  constructor(private http: HttpClient) {}

  getProfile(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/profile`);
  }

  updateUserName(name: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/profile/name`, { name });
  }

  deactivateAccount(): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/deactivate`, {});
  }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}`);
  }
}