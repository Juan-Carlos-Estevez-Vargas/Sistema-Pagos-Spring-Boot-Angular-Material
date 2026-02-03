import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../core/models/auth.model';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header.component',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {

  currentUser: Usuario | null = null;
  isMenuCollapsed = true;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  get userInitials(): string {
    if (!this.currentUser?.nombre) return 'U';

    const names = this.currentUser.nombre.split(' ');
    if (names.length >= 2) return `${names[0][0]}${names[1][0]}`.toUpperCase();

    return names[0][0].toUpperCase();
  }

  logout(): void {
    return this.authService.logout();
  }

  toggleMenu(): void {
    this.isMenuCollapsed = !this.isMenuCollapsed;
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ADMIN');
  }

  get isFinanzas(): boolean {
    return this.authService.hasRole('FINANZAS');
  }

  get isEstudiante(): boolean {
    return this.authService.hasRole('ESTUDIANTE');
  }

  get isProfesor(): boolean {
    return this.authService.hasRole('PROFESOR');
  }
  
}
