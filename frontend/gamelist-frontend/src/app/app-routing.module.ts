import { provideRouter, Routes } from '@angular/router';
import { GameListComponent } from './pages/games/game-list/game-list.component';
import { GameDetailComponent } from './pages/games/game-detail/game-detail.component';
import { LoginComponent } from './pages/auth/login/login.page';
import { RegisterComponent } from './pages/auth/register/register.component';
import { AuthGuard } from './auths/guards/auth.guard';
import { CleanLayoutComponent } from './layouts/clean-layout/clean-layout.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { FavoriteComponent } from './pages/games/favorite/favorite.component';
import { ProfileComponent } from './pages/games/profile/profile.component';
import { ProfileEditComponent } from './pages/games/profile-edit/profile-edit.component';

const routes: Routes = [

{
  path: '',
  pathMatch: 'full',
  redirectTo: 'login'
},

//rotas sem sidebar
{
  path: "login",
  component: CleanLayoutComponent,
  children: [
    { path: '', component: LoginComponent }
  ]
},
{
  path: "register",
  component: CleanLayoutComponent,
  children: [
    { path: '', component: RegisterComponent }
  ]
},

//rotas com sidebar
{
 path: '',
 component: MainLayoutComponent,
 canActivateChild: [AuthGuard],
 children: [
   { path: 'games', component: GameListComponent },
   { path: 'games/:id', component: GameDetailComponent },
   { path: 'favorites', component: FavoriteComponent }, 
   { path: 'profile', component: ProfileComponent },
   { path: 'profile-edit', component: ProfileEditComponent}
 ]
},

//rota padrão
{
  path: '**', redirectTo: 'login' 
}
];

export const appRouterProviders = [provideRouter(routes)];
