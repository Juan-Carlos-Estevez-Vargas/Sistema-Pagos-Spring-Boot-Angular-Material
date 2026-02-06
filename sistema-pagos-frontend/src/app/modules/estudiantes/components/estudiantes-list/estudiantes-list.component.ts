import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, PrimeTemplate } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { EstudianteResponse } from '../../../../core/models/estudiante.model';
import { Programa } from '../../../../core/models/programa.model';
import { EstudianteService } from '../../../../core/services/estudiante.service';
import { ProgramaService } from '../../../../core/services/programa.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';
import { Router, RouterLink } from '@angular/router';
import { ButtonDirective } from "primeng/button";
import { DateFormatPipe } from '../../../../shared/pipes/date-format.pipe';
import { EmptyStateComponent } from "../../../../shared/components/empty-state/empty-state.component";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-estudiantes-list',
  imports: [ButtonDirective, RouterLink, PrimeTemplate, DateFormatPipe, TableModule, EmptyStateComponent, FormsModule],
  templateUrl: './estudiantes-list.component.html',
  styleUrl: './estudiantes-list.component.scss',
  providers: [ConfirmationService]
})
export class EstudiantesListComponent implements OnInit {

  @ViewChild('dt') dt!: Table;

  estudiantes: EstudianteResponse[] = [];
  programas: Programa[] = [];
  selectedEstudiantes: EstudianteResponse[] = [];
  totalRecords = 0;
  loading = true;
  rows = 10;
  first = 0;

  // Filtros
  filters = {
    nombre: '',
    apellido: '',
    codigo: '',
    programaId: '',
    activo: null as boolean | null
  };

  // Columnas de la tabla
  cols = [
    { field: 'codigo', header: 'Código' },
    { field: 'nombre', header: 'Nombre' },
    { field: 'apellido', header: 'Apellido' },
    { field: 'email', header: 'Email' },
    { field: 'programaNombre', header: 'Programa' },
    { field: 'activo', header: 'Estado' },
    { field: 'fechaRegistro', header: 'Registro' }
  ];

  constructor(
    private estudianteService: EstudianteService,
    private programaService: ProgramaService,
    private notificationService: NotificationService,
    private loadingService: LoadingService,
    private confirmationService: ConfirmationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProgramas();
    this.loadEstudiantes();
  }

  loadProgramas(): void {
    this.programaService.listarProgramasActivos().subscribe({
      next: programas => {
        this.programas = programas;
      },
      error: error => {
        console.error('Error cargando los programas', error);
        this.notificationService.showError("Error cargando los programas");
      }
    });
  }

  loadEstudiantes(page: number = 0): void {
    this.loading = true;
    this.loadingService.show();

    this.estudianteService.buscarEstudiantes(
      this.filters.nombre,
      this.filters.apellido,
      this.filters.codigo,
      this.filters.programaId,
      page,
      this.rows
    ).subscribe({
      next: response => {
        this.estudiantes = response.content;
        this.totalRecords = response.totalElements;
        this.loading = false;
        this.loadingService.hide();
      },
      error: error => {
        this.notificationService.showError('Error al cargar los estudiantes', 'Error');
        this.loading = false;
        this.loadingService.hide();
      }
    });
  }

  onPageChanged(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    const page = event.first / event.rows;
    this.loadEstudiantes(page);
  }

  onFilter(): void {
    this.first = 0;
    this.loadEstudiantes();
  }

  onClearFilters(): void {
    this.filters = {
      nombre: '',
      apellido: '',
      codigo: '',
      programaId: '',
      activo: null
    };

    this.loadEstudiantes();
  }

  onExport(): void {
    // Implementar la exportación a Excel/PDF
    this.notificationService.showInfo('Exportando datos ...', 'Exportar');
  }

  onDeleteEstudiantes(estudiante: EstudianteResponse): void {
    this.confirmationService.confirm({
      message: `¿Estás seguro de eliminar al estudiante ${estudiante.nombre} ${estudiante.apellido}?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Si, eliminar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.loadingService.show();
        this.estudianteService.desactivarEstudiante(estudiante.id).subscribe({
          next: () => {
            this.notificationService.showSuccess('¡Estudiante eliminado!', 'Éxito');
            this.loadEstudiantes(Math.floor(this.first / this.rows));
          },
          error: error => {
            this.notificationService.showError('Error al eliminar el estudiante', 'Error');
          },
          complete: () => {
            this.loadingService.hide();
          }
        });
      }
    });
  }

  onActivateEstudiante(estudiante: EstudianteResponse): void {
    this.confirmationService.confirm({
      message: `¿Desea activar el estudiante ${estudiante.nombre} ${estudiante.apellido}?`,
      header: 'Confirmar Activacion',
      icon: 'pi pi-check-circle',
      accept: () => {
        this.loadingService.show();
        this.estudianteService.activarEstudiante(estudiante.id).subscribe({
          next: () => {
            this.notificationService.showSuccess('Estudiante activado', 'Éxito');
            this.loadEstudiantes(Math.floor(this.first / this.rows));
          },
          error: (error) => {
            this.notificationService.showError('Error al activar el estudiante', 'Error');
          },
          complete: () => {
            this.loadingService.hide();
          }
        });
      }
    });
  }

  onViewDetail(estudiante: EstudianteResponse): void {
    this.router.navigate(['/estudiantes', estudiante.id]);
  }

  onEditEstudiante(estudiante: EstudianteResponse): void {
    this.router.navigate(['/estudiantes', estudiante.id, 'editar']);
  }

  getEstadoBadgeClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }
}
