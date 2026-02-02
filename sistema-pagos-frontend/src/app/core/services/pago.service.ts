import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { EstadoPago, PagoRequest, PagoResponse, TipoPago } from '../models/pago.model';
import { Observable } from 'rxjs';
import { PaginatedResponse } from './estudiante.service';

@Injectable({
  providedIn: 'root',
})
export class PagoService {
  
  constructor(private apiService: ApiService) { }

  crearPago(pagoRequest: PagoRequest): Observable<PagoResponse> {
    const formData = new FormData();

    formData.append('data', JSON.stringify({
      cantidad: pagoRequest.cantidad,
      tipoPago: pagoRequest.tipoPago,
      fecha: pagoRequest.fecha,
      codigoEstudiante: pagoRequest.codigoEstudiante,
      observaciones: pagoRequest.observaciones
    }));

    formData.append('archivo', pagoRequest.archivo);
    
    return this.apiService.postMultipart<PagoResponse>('/pagos', formData);
  }

  obtenerPago(id: number): Observable<PagoResponse> {
    return this.apiService.get<PagoResponse>(`/pagos/${id}`);
  }

  listarPagos(
    page: number = 0,
    size: number = 10
  ): Observable<PaginatedResponse<PagoResponse>> {
    return this.apiService.get<PaginatedResponse<PagoResponse>>('/pagos', {
      page,
      size,
      sort: 'fecha,desc'
    });
  }

  buscarPagos(
    codigoEstudiante?: string,
    estado?: EstadoPago,
    tipo?: TipoPago,
    fechaDesde?: Date,
    fechaHasta?: Date,
    page: number = 0,
    size: number = 10
  ): Observable<PaginatedResponse<PagoResponse>> {
    const params: any = { page, size };
    if (codigoEstudiante) params.codigoEstudiante = codigoEstudiante;
    if (estado) params.estado = estado;
    if (tipo) params.tipo = tipo;
    if (fechaDesde) params.fechaDesde = fechaDesde;
    if (fechaHasta) params.fechaHasta = fechaHasta;

    return this.apiService.get<PaginatedResponse<PagoResponse>>('/pagos/buscar', params);
  }

  listarPagosPorEstudiante(
    codigo: string,
    page: number = 0,
    size: number = 10
  ): Observable<PaginatedResponse<PagoResponse>> {
    return this.apiService.get<PaginatedResponse<PagoResponse>>(`/pagos/estudiante/${codigo}`, {
      page,
      size
    });
  }

  actualizarEstado(
    id: number,
    estado: EstadoPago,
    observaciones?: string
  ): Observable<PagoResponse> {
    return this.apiService.patch<PagoResponse>(`/pagos/${id}/estado`, {
      estado,
      observaciones
    });
  }

  descargarArchivo(id: number): Observable<Blob> {
    return this.apiService.downloadFile(`/pagos/${id}/archivo`);
  }

  obtenerTotalPagado(codigoEstudiante: string): Observable<number> {
    return this.apiService.get<number>(`/pagos/estudiante/${codigoEstudiante}/total`);
  }

}
