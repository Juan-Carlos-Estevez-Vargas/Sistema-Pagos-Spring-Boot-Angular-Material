import { Pipe, PipeTransform } from '@angular/core';
import { EstadoPago } from '../../core/models/pago.model';

@Pipe({
  name: 'estadoBadge',
  standalone: true
})
export class EstadoBadgePipe implements PipeTransform {

  transform(value: EstadoPago): string {
    const clases = {
      [EstadoPago.PENDIENTE]: 'bg-warning text-dark',
      [EstadoPago.APROBADO]: 'bg-success',
      [EstadoPago.RECHAZADO]: 'bg-danger',
      [EstadoPago.CANCELADO]: 'bg-secondary'
    };
    return clases[value] || 'bg-secondary';
  }

}
