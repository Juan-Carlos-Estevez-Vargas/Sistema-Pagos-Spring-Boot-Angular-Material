import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { EstudianteRequest, EstudianteResponse } from '../models/estudiante.model';
import { Observable } from 'rxjs';

export interface PaginatedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number,
    pageSize: number,
    sort: {
      sorted: boolean,
      unsorted: boolean,
      empty: boolean
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class EstudianteService {

  constructor(private apiService: ApiService) { }

  // CRUD Operations
  crearEstudiante(estudiante: EstudianteRequest): Observable<EstudianteResponse> {
    return this.apiService.post<EstudianteResponse>('/estudiantes', estudiante);
  }

  obtenerEstudiante(id: string): Observable<EstudianteResponse> {
    return this.apiService.get<EstudianteResponse>(`/estudiantes/${id}`);
  }

  obtenerEstudiantePorCodigo(codigo: string): Observable<EstudianteResponse> {
    return this.apiService.get<EstudianteResponse>(`/estudiantes/codigo/${codigo}`);
  }

  listarEstudiantes(page: number = 0, size: number = 10): Observable<PaginatedResponse<EstudianteResponse>> {
    return this.apiService.get<PaginatedResponse<EstudianteResponse>>('/estudiantes', {
      page,
      size,
      sort: 'apellido,nombre'
    });
  }

  buscarEstudiantes(
    nombre?: string,
    apellido?: string,
    codigo?: string,
    programaId?: string,
    page: number = 0,
    size: number = 10
  ): Observable<PaginatedResponse<EstudianteResponse>> {
    const params: any = { page, size };
    if (nombre) params.nombre = nombre;
    if (apellido) params.apellido = apellido;
    if (codigo) params.codigo = codigo;
    if (programaId) params.programaId = programaId;

    return this.apiService.get<PaginatedResponse<EstudianteResponse>>('/estudiantes/buscar', params);
  }

  actualizarEstudiante(id: string, estudiante: EstudianteRequest): Observable<EstudianteResponse> {
    return this.apiService.put<EstudianteResponse>(`/estudiantes/${id}`, estudiante);
  }

  desactivarEstudiante(id: string): Observable<void> {
    return this.apiService.patch<void>(`/estudiantes/${id}/desactivar`);
  }

  activarEstudiante(id: string): Observable<void> {
    return this.apiService.patch<void>(`/estudiantes/${id}/activar`);
  }

}
