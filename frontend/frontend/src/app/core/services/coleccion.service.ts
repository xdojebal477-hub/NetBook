import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LibroResponse } from './libro.service';
import { environment } from '../../../environments/environment';

export interface ColeccionResponse {
  id: number;
  nombre: string;
  esPublica: boolean;
  propietarioNombre: string;
  numeroLibros: number;
  libros: LibroResponse[];
}

export interface ColeccionRequest {
  nombre: string;
  esPublica: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ColeccionService {
  private apiUrl = `${environment.apiUrl}/colecciones`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return { headers };
  }

  obtenerMisColecciones(): Observable<ColeccionResponse[]> {
    return this.http.get<ColeccionResponse[]>(this.apiUrl, this.getAuthHeaders());
  }

  crearColeccion(request: ColeccionRequest): Observable<ColeccionResponse> {
    return this.http.post<ColeccionResponse>(this.apiUrl, request, this.getAuthHeaders());
  }

  actualizarColeccion(id: number, request: ColeccionRequest): Observable<ColeccionResponse> {
    return this.http.put<ColeccionResponse>(`${this.apiUrl}/${id}`, request, this.getAuthHeaders());
  }

  eliminarColeccion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getAuthHeaders());
  }

  anadirLibroAColeccion(coleccionId: number, libroId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${coleccionId}/libros/${libroId}`, {}, this.getAuthHeaders());
  }

  quitarLibroDeColeccion(coleccionId: number, libroId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${coleccionId}/libros/${libroId}`, this.getAuthHeaders());
  }
}