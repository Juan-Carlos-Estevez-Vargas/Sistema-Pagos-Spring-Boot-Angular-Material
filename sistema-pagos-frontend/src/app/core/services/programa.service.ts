import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Programa, ProgramaRequest } from '../models/programa.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProgramaService {
  
  constructor(private apiService: ApiService) {}

  crearPrograma(programa: ProgramaRequest): Observable<Programa> {
    return this.apiService.post<Programa>('/programas', programa);
  }

  obtenerPrograma(id: string): Observable<Programa> {
    return this.apiService.get<Programa>(`/programas/${id}`);
  }

  obtenerProgramaPorCodigo(codigo: string): Observable<Programa> {
    return this.apiService.get<Programa>(`/programas/codigo/${codigo}`);
  }

  listarProgramas(): Observable<Programa[]> {
    return this.apiService.get<Programa[]>('/programas');
  }

  listarProgramasActivos(): Observable<Programa[]> {
    return this.apiService.get<Programa[]>('/programas/activos');
  }

  actualizarPrograma(id: string, programa: ProgramaRequest): Observable<Programa> {
    return this.apiService.put<Programa>(`/programas/${id}`, programa);
  }

  desactivarPrograma(id: string): Observable<void> {
    return this.apiService.patch<void>(`/programas/${id}/desactivar`);
  }

  activarPrograma(id: string): Observable<void> {
    return this.apiService.patch<void>(`/programas/${id}/activar`);
  }

}
