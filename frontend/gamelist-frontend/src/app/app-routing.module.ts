import { provideRouter, RouterModule, Routes } from '@angular/router';
import { GameListComponent } from './components/game-list/game-list.component';
import { GameDetailComponent } from './components/game-detail/game-detail.component';

const routes: Routes = [
  { path: '', component: GameListComponent },
  { path: 'games/:id', component: GameDetailComponent },
];

export const appRouterProviders = [ provideRouter(routes)];
