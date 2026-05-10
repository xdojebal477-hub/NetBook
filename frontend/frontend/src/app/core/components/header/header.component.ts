import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isMenuOpen = false;

  constructor(public authService: AuthService, private router: Router) {}

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  irAlPerfil() {
    const userId = this.authService.getId();
    if (userId) {
      this.router.navigate(['/usuario', userId]);
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}

