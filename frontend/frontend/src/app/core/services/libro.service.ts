import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface LibroResponse {
  id: number;
  titulo: string;
  sinopsis: string;
  genero: string;
  archivoUrl: string;
  portadaUrl: string;
  estado: string;  autorId: number;  autorNombre: string;
  fechaSubida: string;
  avgRating?: number;
  reviewCount?: number;
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
  private apiUrl = 'https://netbook-backend-production.up.railway.app/api/libros';

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
  subirLibro(titulo: string, sinopsis: string, genero: string, portada: File | null, archivo: File): Observable<LibroResponse> {
    console.log(`[LibroService] subirLibro: ${titulo}`);
    // ...

    const formData = new FormData();
    formData.append('titulo', titulo);
    if (sinopsis) formData.append('sinopsis', sinopsis);
    if (genero) formData.append('genero', genero);

    if (portada) {
      formData.append('portada', portada);
    }
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
  actualizarLibro(id: number, request: LibroUpdateRequest, portada: File | null): Observable<LibroResponse> {
    const formData = new FormData();
    if (request.titulo) formData.append('titulo', request.titulo);
    if (request.sinopsis) formData.append('sinopsis', request.sinopsis);
    if (request.genero) formData.append('genero', request.genero);
    if (request.estado) formData.append('estado', request.estado);
    if (portada) formData.append('portada', portada);

    return this.http.put<LibroResponse>(`${this.apiUrl}/${id}`, formData, this.getAuthHeaders());
  }


  // Obtener URL de lectura del visor
  getLeerLibroUrl(id: number): string {
    return `${this.apiUrl}/${id}/leer`;
  }

  // Obtener blob directo por si necesitamos pasar el JWT al leer
  descargarLibro(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/leer`, {
      headers: this.getAuthHeaders().headers,
      responseType: 'blob'
    });
  }

  // Método para eliminar un libro (Solo autor)
  eliminarLibro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getAuthHeaders());
  }
}
