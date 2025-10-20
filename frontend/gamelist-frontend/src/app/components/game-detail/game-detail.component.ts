import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { GameDTO } from '../../models/game.model';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-game-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './game-detail.component.html',
  styleUrl: './game-detail.component.css'
})
export class GameDetailComponent {
  game?: GameDTO;
  loading = true;
  error?: string;

  constructor(private route: ActivatedRoute, private svc: GameService){}

  ngOnInit(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id){
      this.svc.getById(id).subscribe({
        next: g => { this.game = g; this.loading = false; },
        error: err => { this.error = 'Erro ao carregar jogo'; console.error(err); this.loading = false; }
      });
    } else {
      this.error = 'ID inválido';
      this.loading = false;
    }
  }
}
