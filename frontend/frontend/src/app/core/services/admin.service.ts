import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface UsuarioAdminResponse {
  id: number;
  nombre: string;
  email: string;
  rol: string;
  fechaRegistro: string;
}

export interface CrearUsuarioAdminRequest {
  nombre: string;
  email: string;
  password: string;
  rol: 'LECTOR' | 'AUTOR';
}

export interface LibroAdminResponse {
  id: number;
  titulo: string;
  sinopsis: string;
  genero: string;
  estado: string;
  autorNombre: string;
  fechaSubida: string;
}

export interface ComunidadAdminResponse {
  id: number;
  nombre: string;
  descripcion: string;
  imagenUrl: string;
  fechaCreacion: string;
  propietarioId: number;
  propietarioNombre: string;
  numeroMiembros: number;
  generos: string[];
}

export interface CrearComunidadAdminRequest {
  nombre: string;
  descripcion: string;
  imagenUrl: string;
  generos: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'https://netbook-backend-production.up.railway.app/api/admin';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
  }

  getUsuarios(): Observable<UsuarioAdminResponse[]> {
    return this.http.get<UsuarioAdminResponse[]>(`${this.apiUrl}/usuarios`, { headers: this.getAuthHeaders() });
  }

  crearUsuario(request: CrearUsuarioAdminRequest): Observable<UsuarioAdminResponse> {
    return this.http.post<UsuarioAdminResponse>(`${this.apiUrl}/usuarios`, request, { headers: this.getAuthHeaders() });
  }

  borrarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/usuarios/${id}`, { headers: this.getAuthHeaders() });
  }

  cambiarRol(id: number, nuevoRol: string): Observable<UsuarioAdminResponse> {
    return this.http.put<UsuarioAdminResponse>(`${this.apiUrl}/usuarios/${id}/rol?rol=${nuevoRol}`, {}, { headers: this.getAuthHeaders() });
  }

  getLibros(): Observable<LibroAdminResponse[]> {
    return this.http.get<LibroAdminResponse[]>(`${this.apiUrl}/libros`, { headers: this.getAuthHeaders() });
  }

  borrarLibro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/libros/${id}`, { headers: this.getAuthHeaders() });
  }

  getComunidades(): Observable<ComunidadAdminResponse[]> {
    return this.http.get<ComunidadAdminResponse[]>(`${this.apiUrl}/comunidades`, { headers: this.getAuthHeaders() });
  }

  crearComunidad(request: CrearComunidadAdminRequest): Observable<ComunidadAdminResponse> {
    return this.http.post<ComunidadAdminResponse>(`${this.apiUrl}/comunidades`, request, { headers: this.getAuthHeaders() });
  }

  actualizarComunidad(id: number, request: CrearComunidadAdminRequest): Observable<ComunidadAdminResponse> {
    return this.http.put<ComunidadAdminResponse>(`${this.apiUrl}/comunidades/${id}`, request, { headers: this.getAuthHeaders() });
  }

  borrarComunidad(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/comunidades/${id}`, { headers: this.getAuthHeaders() });
  }

  getUsuariosDisponiblesParaComunidad(id: number): Observable<UsuarioAdminResponse[]> {
    return this.http.get<UsuarioAdminResponse[]>(`${this.apiUrl}/comunidades/${id}/usuarios-disponibles`, { headers: this.getAuthHeaders() });
  }

  getMiembrosComunidad(id: number): Observable<UsuarioAdminResponse[]> {
    return this.http.get<{ content: UsuarioAdminResponse[] }>(`https://netbook-backend-production.up.railway.app/api/comunidades/${id}/miembros`, { headers: this.getAuthHeaders() })
      .pipe(map(response => response.content));
  }

  anadirMiembroAComunidad(comunidadId: number, usuarioId: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/comunidades/${comunidadId}/miembros/${usuarioId}`, {}, { headers: this.getAuthHeaders(), responseType: 'text' });
  }

  eliminarMiembroDeComunidad(comunidadId: number, usuarioId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/comunidades/${comunidadId}/miembros/${usuarioId}`, { headers: this.getAuthHeaders(), responseType: 'text' });
  }
}
