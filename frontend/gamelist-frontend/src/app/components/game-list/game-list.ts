import { Component, OnInit } from '@angular/core';
import { Game } from '../../models/game.model';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-game-list',
  imports: [],
  templateUrl: './game-list.html',
  styleUrl: './game-list.css'
})
export class GameListComponent implements OnInit {
  games: Game[] = [];
  page: number = 1; 
  size: number = 6; 
  loading = false;

  constructor(private gameService: GameService){ }

  ngOnInit(): void{
    this.loadPage(1);
  } 

  loadPage(page: number){
    if(page < 1) return;
    this.loading = true;
    this.page = page;
    this.gameService.getGamesPage(page, this.size).subscribe({
      next: res => {
        this.games = res.items;
        this.loading = false;
      },
      error: err => {
        console.error('Erro ao carregar jogos', err);
        this.loading = false;
      }
    }); 
  }

  prev() { 
    if (this.page > 1) this.loadPage(this.page - 1); 
  }

  next() { 
    this.loadPage(this.page + 1); 
  }
}