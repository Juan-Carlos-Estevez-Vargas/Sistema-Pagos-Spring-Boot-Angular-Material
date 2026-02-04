import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login.component',
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  hidePassword: boolean = true;
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notificationService: NotificationService,
    private loadingService: LoadingService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: false
    })
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.loadingService.show();

    const credentials = this.loginForm.value;
    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.notificationService.showSuccess('¡Inicio de sesión exitoso!', 'Bienvenido');
        this.router.navigate(['/dashboard']);
      },
      error: error => {
        let errorMessage = 'Error en el inicio de sesión';
        if (error.status === 401) {
          errorMessage = 'Usuario o contraseña incorrectos'
        } else if(error.estatus  === 0) {
          errorMessage = 'No se pudo conectar con el servidor';
        }
        this.notificationService.showError(errorMessage, error);
      },
      complete: () => {
        this.isLoading = false;
        this.loadingService.hide();
      }
    });
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

}
