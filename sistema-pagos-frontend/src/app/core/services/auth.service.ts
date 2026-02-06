import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginRequest, LoginResponse, Usuario } from '../models/auth.model';
import { ApiService } from './api.service';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  private currentSubjectUser: BehaviorSubject<Usuario | null>;
  public currentUser: Observable<Usuario | null>;
  private jwtHelper: any;//new JwtHelperService();

  constructor(private apiService: ApiService, private router: Router) {
    this.currentSubjectUser = new BehaviorSubject<Usuario | null>(this.getUserFromStorage());
    this.currentUser = this.currentSubjectUser.asObservable();
  }

  public get currentUserValue(): Usuario | null {
    return this.currentSubjectUser.value;
  }

  public get token(): string | null {
    return localStorage.getItem("token");
  }

  public get isAuthenticated(): boolean {
    const token = this.token;
    return token ? !this.jwtHelper.isTokenExpired(token) : false;
  }

  public get userRole(): string | null {
    const user =  this.currentUserValue;
    return user ? user.rol : null;
  }

  public hasRole(role: string): boolean {
    return this.userRole === role;
  }

  public hasAnyRole(roles: string[]): boolean {
    const userRole = this.userRole;
    return userRole ? roles.includes(userRole) : false;
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
  return this.apiService.post<LoginResponse>('/auth/login', credentials)
    .pipe(
      tap((response: LoginResponse) => {
        if (response.token) {
          this.storeAuthData(response);
          this.decodeAndStoreUser(response.token);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentSubjectUser.next(null);
    this.router.navigate(['/auth/login']);
  }

  getProfile(): Observable<Usuario> {
    return this.apiService.get<Usuario>('/usuarios/perfil');
  }

  refreshUserProfile(): void {
    this.getProfile().subscribe({
      next: (user) => {
        localStorage.setItem('user', JSON.stringify(user));
        this.currentSubjectUser.next(user);
      },
      error: () => {
        this.logout();
      }
    });
  }

  private storeAuthData(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify({
      username: response.username,
      nombre: response.nombre,
      email: response.email,
      rol: response.rol
    }));
  }

  private decodeAndStoreUser(token: string): void {
    const decodedToken = this.jwtHelper.decodeToken(token);
    const user: Usuario = {
      id: decodedToken.sub || '',
      username: decodedToken.sub || '',
      nombre: decodedToken.nombre || 'Usuario',
      email: decodedToken.email || '',
      rol: decodedToken.role || 'ESTUDIANTE',
      activo: true,
      fechaCreacion: new Date(),
      fechaActualizacion: new Date()
    };
    localStorage.setItem('user', JSON.stringify(user));
    this.currentSubjectUser.next(user);
  }

  private getUserFromStorage(): Usuario | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
    return null;
  }

}
