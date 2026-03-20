import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { NavbarComponent } from '../../core/navbar/navbar';

@Component({
  selector: 'app-layout',
  standalone: true,
  templateUrl: './layout.html',
  styleUrls: ['./layout.css'],
  imports: [
    CommonModule,
    RouterOutlet,
    NavbarComponent
  ]
})
export class LayoutComponent {

  constructor(private router: Router) {}

}
