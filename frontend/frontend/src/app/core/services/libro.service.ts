import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LibroResponse {
  id: number;
  titulo: string;
  sinopsis: string;
  genero: string;
  archivoUrl: string;
  portadaUrl: string;
  estado: string;
  autorNombre: string;
  fechaSubida: string;
}

export interface LibroUpdateRequest {
  titulo?: string;
  sinopsis?: string;
  genero?: string;
  estado?: string;
}

@Injectable({
  providedIn: 'root'
})
export class LibroService {

  // Apunta a nuestro backend de Spring Boot (Puerto 8081 que configuramos)
  private apiUrl = 'http://localhost:8081/api/libros';

  constructor(private http: HttpClient) { }

  // Helper para generar los headers de Autorización con JWT
  private getAuthHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return { headers };
  }

  /**
   * Método para subir un libro (multipart/form-data)
   * el JWT tiene que ir en la cabecera
   */
  subirLibro(titulo: string, sinopsis: string, genero: string, archivo: File): Observable<LibroResponse> {
    const formData = new FormData();
    formData.append('titulo', titulo);
    if (sinopsis) formData.append('sinopsis', sinopsis);
    if (genero) formData.append('genero', genero);

    formData.append('archivo', archivo);

    return this.http.post<LibroResponse>(this.apiUrl, formData, this.getAuthHeaders());
  }

  // Método para obtener el catálogo público
  obtenerCatalogoGeneral(): Observable<LibroResponse[]> {
    // endpoint publico qu eno necesita token
    return this.http.get<LibroResponse[]>(this.apiUrl);
  }

  // Método para obtener las obras del autor logueado
  obtenerMisObras(): Observable<LibroResponse[]> {
    return this.http.get<LibroResponse[]>(`${this.apiUrl}/mis-obras`, this.getAuthHeaders());
  }

  // Método para obtener el detalle de un libro
  obtenerLibroPorId(id: number): Observable<LibroResponse> {
    return this.http.get<LibroResponse>(`${this.apiUrl}/${id}`);
  }

  // Método para actualizar los metadatos de un libro (Solo autor)
  actualizarLibro(id: number, request: LibroUpdateRequest): Observable<LibroResponse> {
    return this.http.put<LibroResponse>(`${this.apiUrl}/${id}`, request, this.getAuthHeaders());
  }

  // Método para eliminar un libro (Solo autor)
  eliminarLibro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getAuthHeaders());
  }
}
