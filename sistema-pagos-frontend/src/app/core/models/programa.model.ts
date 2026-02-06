export interface Programa {
  id: string;
  nombre: string;
  codigo: string;
  activo: boolean;
  totalEstudiantes: number;
  estudiantes: EstudianteSimple[];
}

export interface EstudianteSimple {
  id: string;
  nombre: string;
  apellido: string;
  codigo: string;
  activo: boolean;
}

export interface ProgramaRequest {
  id: string;
  nombre: string;
  codigo: string;
  activo: boolean;
}