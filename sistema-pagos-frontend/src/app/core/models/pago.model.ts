export enum EstadoPago {
  PENDIENTE = 'PENDIENTE',
  APROBADO = 'APROBADO',
  RECHAZADO = 'RECHAZADO',
  CANCELADO = 'CANCELADO'
}

export enum TipoPago {
  MATRICULA = 'MATRICULA',
  MENSUALIDAD = 'MENSUALIDAD',
  EXAMEN = 'EXAMEN',
  MATERIAL = 'MATERIAL',
  OTROS = 'OTROS'
}

export interface Pago {
  id: number;
  cantidad: number;
  fecha: Date;
  tipoPago: TipoPago;
  estadoPago: EstadoPago;
  observaciones: string;
  archivoNombre: string;
  fechaCreacion: Date;
  estudianteNombre: string;
  estudianteCodigo: string;
  programaNombre: string;
  procesadoPor: string;
}

export interface PagoRequest {
  cantidad: number;
  tipoPago: TipoPago;
  fecha: Date;
  codigoEstudiante: string;
  observaciones: string;
  archivo: File;
}

export interface PagoResponse {
  id: number;
  cantidad: number;
  fecha: Date;
  tipoPago: TipoPago;
  estadoPago: EstadoPago;
  observaciones: string;
  archivoNombre: string;
  fechaCreacion: Date;
  estudianteNombre: string;
  estudianteCodigo: string;
  programaNombre: string;
  procesadoPor: string;
}