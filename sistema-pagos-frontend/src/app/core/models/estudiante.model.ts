import { Pago, PagoResponse } from "./pago.model";

export interface Estudiante {
  id: string;
  nombre: string;
  apellido: string;
  codigo: string;
  email: string;
  telefono: string;
  activo: boolean;
  programaId: string;
  programaNombre: string;
  fechaRegistro: Date;
  pagos: Pago[];
}

export interface EstudianteRequest {
  nombre: string;
  apellido: string;
  codigo: string;
  email: string;
  telefono: string;
  programaId: string;
  activo: boolean;
}

export interface EstudianteResponse {
  id: string;
  nombre: string;
  apellido: string;
  codigo: string;
  email: string;
  telefono: string;
  activo: boolean;
  programaId: string;
  programaNombre: string;
  fechaRegistro: Date;
  pagos: PagoResponse[];
}