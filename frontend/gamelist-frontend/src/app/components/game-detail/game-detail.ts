import { Component, OnInit } from '@angular/core';
import { Game } from '../../models/game.model';
import { GameService } from '../../services/game.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-game-detail',
  imports: [],
  templateUrl: './game-detail.html',
  styleUrl: './game-detail.css'
})
export class GameDetailComponent implements OnInit{
  game?: Game;
  loading = false;

  constructor(private route: ActivatedRoute, private gameService: GameService){}

  load(id: number){
    this.loading = true;
    this.gameService.getGamesById(id).subscribe({
      next: g => { this.game = g; this.loading = false; },
      error: err => { console.error('Erro ao carregar detalhes', err); this.loading = false; }
    });
  }

  ngOnInit(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.load(id);
  }
}
