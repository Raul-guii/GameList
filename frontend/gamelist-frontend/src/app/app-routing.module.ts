import { provideRouter, Routes } from '@angular/router';
import { GameListComponent } from './components/game-list/game-list.component';
import { GameDetailComponent } from './components/game-detail/game-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'games',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: GameListComponent },
      { path: ':id', component: GameDetailComponent },
    ],
  },
  { path: '**', redirectTo: 'login' }
];

export const appRouterProviders = [provideRouter(routes)];
