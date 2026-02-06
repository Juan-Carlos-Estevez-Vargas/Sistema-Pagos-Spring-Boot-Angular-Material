import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {

  transform(value: Date | string | null | undefined, format: string = 'DD/MM/YYYY'): string {
    if (!value) return '';  
    const date = typeof value === 'string' ? new Date(value) : value;
    if (!(date instanceof Date) || isNaN(date.getTime())) return '';
    
    return this.formatDate(date, format);
  }

  private formatDate(date: Date, format: string): string {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    const replacements: { [key: string]: string } = {
      'DD': day,
      'MM': month,
      'YYYY': String(year),
      'YY': String(year).slice(-2),
      'HH': hours,
      'mm': minutes,
      'ss': seconds
    };

    let formattedDate = format;
    for (const [token, value] of Object.entries(replacements)) {
      formattedDate = formattedDate.replace(new RegExp(token, 'g'), value);
    }

    return formattedDate;
  }
}