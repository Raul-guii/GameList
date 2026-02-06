import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
}) 

export class AuthService{
    private apiUrl = 'http://localhost:8080/auth';

    constructor(private http: HttpClient){}

    login(credentials: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }

    register(user: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/register`, user);
    }

    saveToken(token: string): void{
        localStorage.setItem('token', token);
    }

    getToken(): string | null{
        return localStorage.getItem('token');
    }

    isLoggedIn(): boolean {
        return !this.getToken();
    }

    logout(): void{
        localStorage.removeItem('token');
    }

}