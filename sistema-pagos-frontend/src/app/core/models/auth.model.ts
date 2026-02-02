export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  nombre: string;
  email: string;
  rol: string;
  fechaExpiracion: string;
}

export interface Usuario {
  id: string;
  username: string;
  nombre: string;
  email: string;
  rol: Rol;
  activo: boolean;
  fechaCreacion: Date;
  fechaActualizacion: Date;
}

export enum Rol {
  ADMIN = 'ADMIN',
  FINANZAS = 'FINANZAS',
  ESTUDIANTE = 'ESTUDIANTE',
  PROFESOR = 'PROFESOR'
}