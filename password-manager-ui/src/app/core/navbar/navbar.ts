import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent {

  constructor(private router: Router) {}

 goDashboard() {
   this.router.navigateByUrl('/dashboard');
 }
  goProfile() {
    this.router.navigate(['/profile']);
  }

 goVault() {
   this.router.navigateByUrl('/vault');
 }
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

}
