import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs/operators';
import { GameService } from '../../services/game.service';
import { Game } from '../../models/game.model';

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.html',
  styleUrls: ['./game-search.css']
})
export class GameSearchComponent implements OnInit, OnDestroy {
  searchControl = new FormControl('');   
  results: Game[] = [];                   
  private destroy$ = new Subject<void>();

  constructor(private gameService: GameService) {}

  ngOnInit(): void {

    this.searchControl.valueChanges.pipe(
      debounceTime(300),              
      distinctUntilChanged(),           
      switchMap(q => this.gameService.searchGames(q || '', 1, 10)), 
      takeUntil(this.destroy$) 
    ).subscribe({
      next: res => {
        this.results = res.items;
      },
      error: err => {
        console.error('Erro na busca', err);
        this.results = []; 
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
