import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyFormat',
  standalone: true
})
export class CurrencyFormatPipe implements PipeTransform {

 transform(value: number, currencyCode: string = 'USD'): string {
    if (value == null ) return '';
    const numericValue = typeof value === 'string' ? parseFloat(value) : Number(value);
    if (isNaN(numericValue)) return '';
    
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: currencyCode,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(numericValue);
  }

}
