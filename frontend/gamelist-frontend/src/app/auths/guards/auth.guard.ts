import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivateChild {
  constructor(private authService: AuthService, private router: Router) {}

canActivateChild(): boolean | UrlTree {
  const token = this.authService.getToken();

  if (!token) {
    return this.router.createUrlTree(['/login']);
  }

  return true;
}


}
