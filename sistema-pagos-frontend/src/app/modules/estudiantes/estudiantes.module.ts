import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { EstudiantesRoutingModule } from './estudiantes-routing.module';
import { SharedModule } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    EstudiantesListComponent,
    EstudianteDetailComponent,
    EstudianteFormComponent,
    ReactiveFormsModule,
    EstudiantesRoutingModule,
    SharedModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule,
    ToolbarModule
  ]
})
export class EstudiantesModule { }
