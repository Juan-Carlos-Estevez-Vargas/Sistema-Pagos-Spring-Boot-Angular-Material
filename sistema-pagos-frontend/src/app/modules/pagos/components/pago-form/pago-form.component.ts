import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EstudianteResponse } from '../../../../core/models/estudiante.model';
import { TipoPago } from '../../../../core/models/pago.model';
import { EstudianteService } from '../../../../core/services/estudiante.service';
import { PagoService } from '../../../../core/services/pago.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { Rol } from '../../../../core/models/auth.model';

@Component({
  selector: 'app-pago-form',
  imports: [],
  templateUrl: './pago-form.component.html',
  styleUrl: './pago-form.component.scss',
})
export class PagoFormComponent implements OnInit {

  pagoForm: FormGroup;
  submitted = false;
  isLoading = false;
  archivo: File | null = null;
  archivoPreview: string | ArrayBuffer | null = null;
  estudiantes: EstudianteResponse[] = [];
  tiposPago = Object.values(TipoPago);
  maxFileSize = 10 * 1024 * 1024; // 10MB

  constructor(
    private fb: FormBuilder,
    private estudianteService: EstudianteService,
    private pagoService: PagoService,
    private notificationService: NotificationService,
    private loadingService: LoadingService,
    private authService: AuthService,
    private router: Router
  ) {
    this.pagoForm = this.fb.group({
      codigoEstudiante: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.min(0.01)]],
      tipoPago: ['', Validators.required],
      fecha: [new Date().toISOString().split('T')[0], Validators.required],
      observaciones: ['']
    });

    // Si es un estudiante, autocompletar su código
    if (this.authService.hasRole(Rol.ESTUDIANTE)) {
      this.pagoForm.get('codigoEstudiante')?.disable();
    }
  }

  ngOnInit(): void {
    this.loadEstudiantes();
    this.setDefaultValues();
  }

  setDefaultValues(): void {
    // Si es un estudiante, obtener su código del perfil
    if (this.authService.hasRole(Rol.ESTUDIANTE)) {
      const currentUser = this.authService.currentUserValue;

      if (currentUser?.username) {
        this.estudianteService.obtenerEstudiantePorCodigo(currentUser.username).subscribe({
          next: estudiante => {
            this.pagoForm.patchValue({
              codigoEstudiante: estudiante.codigo
            });
          },
          error: () => {
            this.notificationService.showError('No se encontró información del estudiante', 'Error');
          }
        });
      }
    }
  }

  loadEstudiantes(): void {
    if (this.authService.hasAnyRole([Rol.ADMIN, Rol.FINANZAS])) {
      this.estudianteService.listarEstudiantes(0, 100).subscribe({
        next: response => {
          this.estudiantes = response.content.filter(e => e.activo);
        },
        error: error => {
          console.log('Error obteniendo los estudiantes', error);
          this.notificationService.showError('Error cargando los estudiantes', 'Error');
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    // Validar tamaño del archivo
    const allowedTypes = ['application/pdf', 'image/jpeg', 'image/png', 'image/jpg'];
    if (!allowedTypes.includes(file.type)) {
      this.notificationService.showError('Tipo de archivo no permitido', 'Error');
      return;
    }

    this.archivo = file;

    // Generar preview SI es una imagen
    if (file.type.startsWith('image')) {
      const reader = new FileReader();
      reader.onload = () => {
        this.archivoPreview = reader.result;
      };
      reader.readAsDataURL(file);
    } else {
      this.archivoPreview = null;
    }
  }

  removeFile(): void {
    this.archivo = null;
    this.archivoPreview = null;
    const fileInput = document.getElementById('archivo') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.pagoForm.invalid) {
      this.pagoForm.markAllAsTouched();
      return;
    }

    if (!this.archivo) {
      this.notificationService.showError('Debe adjuntar un comprobante de pago', 'Error');
      return;
    }

    this.isLoading = true;
    this.loadingService.show();

    const pagoRequest = {
      ...this.pagoForm.getRawValue(),
      cantidad: parseFloat(this.pagoForm.get('cantidad')?.value),
      fecha: new Date(this.pagoForm.get('fecha')?.value),
      archivo: this.archivo
    };

    this.pagoService.crearPago(pagoRequest).subscribe({
      next: estudiante => {
        this.notificationService.showSuccess('Pago registrado exitosamente', 'Éxito');
        this.router.navigate(['/pagos', estudiante.id]);
      },
      error: error => {
        let errorMessage = 'Error al registrar el pago';
        if (error.status === 400) {
          errorMessage = error.error?.message || 'Datos inválidos';
        } else if (error.status === 404) {
          errorMessage = 'Estudiante NO encontrado';
        }
        this.notificationService.showError(errorMessage, 'Error');
      },
      complete: () => {
        this.isLoading = false;
        this.loadingService.hide();
      }
    });
  }

  get f() { return this.pagoForm.controls; }

  get fileName(): string {
    return this.archivo?.name || 'No se ha seleccionado ningún archivo';
  }

  get fileSize(): string {
    if (!this.archivo) return '';
    const sizeInMB = this.archivo.size / (1024 * 1024);
    return sizeInMB.toFixed(2) + ' MB'; 
  }
}
