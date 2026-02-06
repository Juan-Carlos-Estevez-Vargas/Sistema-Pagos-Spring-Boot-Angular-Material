import { Component, OnDestroy, OnInit } from '@angular/core';
import { Rol, Usuario } from '../../../../core/models/auth.model';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../../core/services/auth.service';
import { PagoService } from '../../../../core/services/pago.service';
import { EstudianteService } from '../../../../core/services/estudiante.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';
import { EstadoPago } from '../../../../core/models/pago.model';
import { CurrencyFormatPipe } from '../../../../shared/pipes/currency-format.pipe';
import { EstadoBadgePipe } from '../../../../shared/pipes/estado-badge.pipe';
import { ChartModule } from 'primeng/chart';
import { DateFormatPipe } from '../../../../shared/pipes/date-format.pipe';
import { EstadoPagoPipe } from '../../../../shared/pipes/estado-pago.pipe';

@Component({
  selector: 'app-dashboard',
  imports: [ 
    CurrencyFormatPipe, 
    EstadoBadgePipe,
    ChartModule,
    DateFormatPipe,
    EstadoPagoPipe
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit, OnDestroy {

  currentUser: Usuario | null = null;

  // Estadísticas
  stats = {
    totalPagos: 0,
    pagosPendientes: 0,
    pagosAprobados: 0,
    totalRecaudado: 0,
    totalEstudiantes: 0
  }

  // Charts
  chartData: any;
  chartOptions: any;

  ultimosPagos: any[] = [];

  private subscriptions = new Subscription();

  constructor(
    public authService: AuthService,
    private pagoService: PagoService,
    private estudianteService: EstudianteService,
    private notificationService: NotificationService,
    private loadingService: LoadingService
  ) { }

  ngOnInit(): void {
    this.loadingService.show();
    this.currentUser = this.authService.currentUserValue;
    this.loadDashboardData();
    this.initChart();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadDashboardData(): void {
    // Cargar pagos
    const pagosSub = this.pagoService.listarPagos(1, 100).subscribe({
      next: response => {
        this.stats.totalPagos = response.totalElements;
        this.calculatePaymentStats(response.content);
        this.ultimosPagos = response.content.slice(0, 5);
      },
      error: error => {
        this.notificationService.showError('Error al cargar los pagos', 'Error');
      }
    });

    // Cargar estudiantes (solo para admin/finanzas/profesor)
    if (this.authService.hasAnyRole([Rol.ADMIN, Rol.FINANZAS, Rol.PROFESOR])) {
      const estudiantesSub = this.estudianteService.listarEstudiantes(0, 1).subscribe({
        next: response => {
          this.stats.totalEstudiantes = response.totalElements;
        },
        error: error => {
          console.log('Error al cargar estudiantes', error);
          this.notificationService.showError('Error al cargar estudiantes', 'Error');
        }
      });
      this.subscriptions.add(estudiantesSub);
    }

    this.subscriptions.add(pagosSub);
    this.loadingService.hide();
  }

  calculatePaymentStats(pagos: any[]): void {
    this.stats.pagosPendientes = pagos.filter(p => p.estadoPago === EstadoPago.PENDIENTE).length;
    this.stats.pagosAprobados = pagos.filter(p => p.estadoPago === EstadoPago.APROBADO).length;
    this.stats.totalRecaudado = pagos
      .filter(p => p.estadoPago === EstadoPago.APROBADO)
      .reduce((sum, p) => sum + p.cantidad, 0);
  }

  initChart(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.chartData = {
      labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
      datasets: [
        {
          label: 'Pagos Aprobados',
          data: [65, 59, 80, 81, 56, 55, 40, 48, 56, 67, 78, 89],
          fill: false,
          borderColor: documentStyle.getPropertyValue('--primary-500'),
          tension: 0.4
        },
        {
          label: 'Pagos Pendientes',
          data: [28, 48, 40, 19, 86, 27, 90, 40, 30, 45, 50, 60],
          fill: false,
          borderColor: documentStyle.getPropertyValue('--yellow-500'),
          tension: 0.4
        }
      ]
    };

    this.chartOptions = {
      maintainAspectRatio: false,
      aspectRatio: 0.6,
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        },
        y: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        }
      }
    };
  }

  get greeting(): string {
    const hour = new Date().getHours();
    if (hour < 12) return '¡Buenos días!';
    if (hour < 18) return '¡Buenas tardes!';
    return '¡Buenas noches!';
  }

  get canSeeAllStats(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'FINANZAS']);
  }
 
}
