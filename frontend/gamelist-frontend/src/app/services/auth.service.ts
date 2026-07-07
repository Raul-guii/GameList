import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environments";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = `${environment.apiUrl}/auth`;

    constructor(private http: HttpClient) {}

    login(credentials: { email: string; password: string }): Observable<{ token: string }> {
        return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials);
    }

    register(user: { username: string; password: string; name: string; email: string }): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/register`, user);
    }

    saveToken(token: string): void {
        localStorage.setItem('token', token);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    logout(): void {
        localStorage.removeItem('token');
    }

    getCurrentUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub ?? null;
    } catch {
        return null;
    }
    }
}