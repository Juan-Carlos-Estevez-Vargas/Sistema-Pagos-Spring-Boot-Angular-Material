import { Pipe, PipeTransform } from '@angular/core';
import { EstadoPago } from '../../core/models/pago.model';

@Pipe({
  name: 'estadoPago',
  standalone: true
})
export class EstadoPagoPipe implements PipeTransform {

  transform(value: EstadoPago): string {
    const estados = {
      [EstadoPago.PENDIENTE]: 'Pendiente',
      [EstadoPago.APROBADO]: 'Aprobado',
      [EstadoPago.RECHAZADO]: 'Rechazado',
      [EstadoPago.CANCELADO]: 'Cancelado'
    };
    return estados[value] || value;
  }

}
