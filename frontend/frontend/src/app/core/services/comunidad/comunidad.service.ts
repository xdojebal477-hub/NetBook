import { Injectable, NgZone } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import {
  ComunidadResponse,
  ComunidadRequest,
  UsuarioResponse,
  MensajeChatResponse,
  MensajeChatRequest,
  Page
} from '../../models/comunidad.model';

@Injectable({
  providedIn: 'root'
})
export class ComunidadService {
  private apiUrl = 'http://localhost:8081/api/comunidades';

  constructor(private http: HttpClient, private zone: NgZone) { }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
  }

  listarComunidades(genero?: string, page: number = 0, size: number = 10): Observable<Page<ComunidadResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (genero) {
      params = params.set('genero', genero);
    }

    return this.http.get<Page<ComunidadResponse>>(this.apiUrl, { params, headers: this.getAuthHeaders() });
  }

  obtenerComunidad(id: number): Observable<ComunidadResponse> {
    return this.http.get<ComunidadResponse>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  crearComunidad(req: ComunidadRequest): Observable<ComunidadResponse> {
    return this.http.post<ComunidadResponse>(this.apiUrl, req, { headers: this.getAuthHeaders() });
  }

  unirseComunidad(id: number): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/${id}/unirse`, {}, { headers: this.getAuthHeaders(), responseType: 'text' as 'json' });
  }

  obtenerMiembros(id: number, page: number = 0, size: number = 20): Observable<Page<UsuarioResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<UsuarioResponse>>(`${this.apiUrl}/${id}/miembros`, { params, headers: this.getAuthHeaders() });
  }

  historialMensajes(id: number, page: number = 0, size: number = 50): Observable<Page<MensajeChatResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<MensajeChatResponse>>(`${this.apiUrl}/${id}/mensajes`, { params, headers: this.getAuthHeaders() });
  }

  enviarMensaje(id: number, req: MensajeChatRequest): Observable<MensajeChatResponse> {
    return this.http.post<MensajeChatResponse>(`${this.apiUrl}/${id}/mensajes`, req, { headers: this.getAuthHeaders() });
  }

  // --- SSE Logic ---
  escucharNuevosMensajes(comunidadId: number): Observable<MensajeChatResponse> {
    return new Observable(observer => {
      const token = localStorage.getItem('token') || '';
      const sseUrl = `${this.apiUrl}/${comunidadId}/stream?token=${token}`;
      const eventSource = new EventSource(sseUrl);

      eventSource.onmessage = (event) => {
        // Necesitamos usar NgZone para asegurar que Angular detecte los cambios de SSE
        this.zone.run(() => {
          const mensaje: MensajeChatResponse = JSON.parse(event.data);
          observer.next(mensaje);
        });
      };

      eventSource.onerror = (error) => {
        this.zone.run(() => {
          // Generalmente se reconectará automáticamente
          console.error('Error SSE', error);
        });
      };

      return () => {
        eventSource.close();
      };
    });
  }
}
