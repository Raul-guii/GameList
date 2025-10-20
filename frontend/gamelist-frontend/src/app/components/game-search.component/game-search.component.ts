import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { GameDTO } from '../../models/game.model';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-game-search',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './game-search.component.html',
  styleUrl: './game-search.component.css'
})
export class GameSearchComponent {
  search = new FormControl('');
  results: GameDTO[] = [];
  loading = false;

  constructor(private svc: GameService){}

  doSearch(): void{
    const q = this.search.value?.trim();
    if (!q) { this.results = []; return; }
    this.loading = true;
    this.svc.search(q).subscribe({
      next: data => { this.results = data; this.loading = false; },
      error: err => { console.error(err); this.results = []; this.loading = false; } 
    });

  }

}
