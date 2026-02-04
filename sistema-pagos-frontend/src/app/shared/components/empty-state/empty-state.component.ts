import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state.component',
  imports: [],
  templateUrl: './empty-state.component.html',
  styleUrl: './empty-state.component.scss',
})
export class EmptyStateComponent {

  @Input() icon: string = 'fas fa-inbox';
  @Input() title: string = 'No hay datos';
  @Input() message: string = 'No se encontraron registros para mostrar.';
  @Input() actionText?: string;
  @Input() actionIcon?: string;
  @Input() actionLink?: string;

}
