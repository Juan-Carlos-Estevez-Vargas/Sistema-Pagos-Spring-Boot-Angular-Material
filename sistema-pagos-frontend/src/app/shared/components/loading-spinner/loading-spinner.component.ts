import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  imports: [],
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.scss',
})
export class LoadingSpinnerComponent {

  @Input() message: string = 'Cargando...';
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() type: 'inline' | 'overlay' = 'inline';

}
