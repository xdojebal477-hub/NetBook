export interface Usuario {
  id?: number;
  nombre: string;
  email: string;
  rol: string;
  avatarUrl?: string;
  fechaRegistro?: string;
}

export interface LoginRequest {
  email: string;
  password?: string;
}

export interface RegistroRequest {
  nombre: string;
  email: string;
  password?: string;
}

export interface AuthResponse {
  id: number;
  token: string;
  nombre: string;
  email: string;
  rol: string;
}
