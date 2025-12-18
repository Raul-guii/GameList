import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-clean-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './clean-layout.component.html',
  styleUrl: './clean-layout.component.css'
})
export class CleanLayoutComponent {

}
