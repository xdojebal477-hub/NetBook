import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface ResenaResponse {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  libroId: number;
  puntuacion: number;
  comentario: string;
  sentimientoIA: string;
  fechaCreacion: string;
  fechaActualizacion: string;
}

export interface CreateResenaRequest {
  puntuacion: number;
  comentario?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ResenaService {

  private apiUrl = `${environment.apiUrl}/resenas`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return { headers };
  }

  /**
   * Crear o actualizar una reseña
   */
  crearOActualizarResena(libroId: number, request: CreateResenaRequest): Observable<ResenaResponse> {
    console.log(`[ResenaService] crearOActualizarResena(libroId=${libroId}) con datos:`, request);
    return this.http.post<ResenaResponse>(
      `${this.apiUrl}/${libroId}`,
      request,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] crearOActualizarResena SUCCESS:`, res),
        err => console.error(`[ResenaService] crearOActualizarResena ERROR:`, err)
      )
    );
  }

  /**
   * Obtener todas las reseñas de un libro
   */
  obtenerResenasPorLibro(libroId: number): Observable<ResenaResponse[]> {
    console.log(`[ResenaService] obtenerResenasPorLibro(libroId=${libroId})`);
    return this.http.get<ResenaResponse[]>(
      `${this.apiUrl}/libro/${libroId}`,
      this.getAuthHeaders()
    ).pipe(
        tap(
          res => console.log(`[ResenaService] obtenerResenasPorLibro SUCCESS, encontradas: ${res?.length ?? 0}`, res),
          err => console.error(`[ResenaService] obtenerResenasPorLibro ERROR:`, err)
        )
      );
  }

  /**
   * Obtener mi reseña sobre un libro
   */
  obtenerMiResena(libroId: number): Observable<ResenaResponse> {
    console.log(`[ResenaService] obtenerMiResena(libroId=${libroId})`);
    return this.http.get<ResenaResponse>(
      `${this.apiUrl}/mia/${libroId}`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] obtenerMiResena SUCCESS:`, res),
        err => console.error(`[ResenaService] obtenerMiResena ERROR:`, err)
      )
    );
  }

  /**
   * Obtener todas mis reseñas
   */
  obtenerMisResenas(): Observable<ResenaResponse[]> {
    console.log(`[ResenaService] obtenerMisResenas()`);
    return this.http.get<ResenaResponse[]>(
      `${this.apiUrl}/usuario/mis-resenas`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] obtenerMisResenas SUCCESS, encontradas: ${res?.length ?? 0}`, res),
        err => console.error(`[ResenaService] obtenerMisResenas ERROR:`, err)
      )
    );
  }

  /**
   * Eliminar una reseña
   */
  eliminarResena(libroId: number): Observable<void> {
    console.log(`[ResenaService] eliminarResena(libroId=${libroId})`);
    return this.http.delete<void>(
      `${this.apiUrl}/${libroId}`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        () => console.log(`[ResenaService] eliminarResena SUCCESS`),
        err => console.error(`[ResenaService] eliminarResena ERROR:`, err)
      )
    );
  }

  /**
   * Obtener rating promedio de un libro
   */
  obtenerRatingPromedio(libroId: number): Observable<number> {
    console.log(`[ResenaService] obtenerRatingPromedio(libroId=${libroId})`);
    return this.http.get<number>(
      `${this.apiUrl}/libro/${libroId}/rating`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] obtenerRatingPromedio SUCCESS:`, res),
        err => console.error(`[ResenaService] obtenerRatingPromedio ERROR:`, err)
      )
    );
  }

  /**
   * Contar reseñas de un libro
   */
  contarResenas(libroId: number): Observable<number> {
    console.log(`[ResenaService] contarResenas(libroId=${libroId})`);
    return this.http.get<number>(
      `${this.apiUrl}/libro/${libroId}/count`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] contarResenas SUCCESS:`, res),
        err => console.error(`[ResenaService] contarResenas ERROR:`, err)
      )
    );
  }

  /**
   * Obtener todas las reseñas (admin)
   */
  obtenerTodasResenas(): Observable<ResenaResponse[]> {
    console.log(`[ResenaService] ADMIN obtenerTodasResenas()`);
    return this.http.get<ResenaResponse[]>(
      `${environment.apiUrl}/admin/resenas`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        res => console.log(`[ResenaService] ADMIN obtenerTodasResenas SUCCESS:`, res?.length),
        err => console.error(`[ResenaService] ADMIN obtenerTodasResenas ERROR:`, err)
      )
    );
  }

  /**
   * Eliminar una reseña (admin)
   */
  eliminarResenaAdmin(resenaId: number): Observable<void> {
    console.log(`[ResenaService] ADMIN eliminarResenaAdmin(resenaId=${resenaId})`);
    return this.http.delete<void>(
      `${environment.apiUrl}/admin/resenas/${resenaId}`,
      this.getAuthHeaders()
    ).pipe(
      tap(
        () => console.log(`[ResenaService] ADMIN eliminarResenaAdmin SUCCESS`),
        err => console.error(`[ResenaService] ADMIN eliminarResenaAdmin ERROR:`, err)
      )
    );
  }
}
