import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles = route.data['roles'] as string[];

  // Si no se especifican roles, permitir acceso
  if (!expectedRoles || expectedRoles.length === 0) {
    return true;
  }

  // Verificar si el usuario tiene alguno de los roles requeridos
  const hasRole = authService.hasAnyRole(expectedRoles);

  if (!hasRole) {
    // Sin permisos, redirigir al Dashboard
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};
