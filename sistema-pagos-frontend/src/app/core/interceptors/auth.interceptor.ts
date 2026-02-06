import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { NotificationService } from "../services/notification.service";
import { Router } from "@angular/router";
import { catchError, Observable, throwError } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private authService: AuthService,
        private notificationService: NotificationService,
        private router: Router
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Clonar la request y agregar headers
        let authRequest = request;
        const token = this.authService.token;

        if (token) {
            authRequest = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }

        return next.handle(authRequest).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    // Token expirado o inválido
                    this.authService.logout();
                    this.notificationService.showError('Sesión expirada. Por favor inicie sesión nuevamente', 'Sesión expirada');
                    this.router.navigate(['/auth/login']);
                } else if (error.status === 403) {
                    // Acceso denegado
                    this.notificationService.showError('No tiene permisos para realizar esta acción', 'Acceso denegado');
                    this.router.navigate(['/dashboard']);
                } else if (error.status === 0) {
                    // Error de conexión
                    this.notificationService.showError('No se pudo conectar con el servidor', 'Error de conexión');
                } else if (error.status >= 500) {
                    // Error del servidor
                    this.notificationService.showError('Error interno del servidor', 'Error');
                }

                return throwError(() => error);
            })
        );
    }
}