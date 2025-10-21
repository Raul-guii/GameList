
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { GameSearchComponent } from './components/game-search.component/game-search.component';
import { GameListComponent } from './components/game-list/game-list.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, GameSearchComponent, GameListComponent, SidebarComponent],   
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
   title = 'GameList';

   sidebarVisible = true;

   toggleSidebar(){
    this.sidebarVisible = !this.sidebarVisible;
   }
}
