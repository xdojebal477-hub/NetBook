import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

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

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8081/api/admin';

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
}
