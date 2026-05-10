import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthResponse, LoginRequest, RegistroRequest } from '../models/auth.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) { }

  // MOCK: Generado a partir del OpenAPI
  mockLogin(request: LoginRequest): Observable<AuthResponse> {
    const mockResponse: AuthResponse = {
      id: 1,
      token: 'mock-jwt-token-h4sh3d',
      nombre: 'Usuario Mock',
      email: request.email,
      rol: 'LECTOR'
    };
    return of(mockResponse); // Simula una respuesta asíncrona
  }

  //  Llamada real al backend Sprint 1 que ya tenemos funcionando
  login(request: LoginRequest): Observable<AuthResponse> {
    console.log(`[AuthService] Haciendo login para: ${request.email}`);
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(
        res => console.log(`[AuthService] Login SUCCESS para ${res.email}, Rol: ${res.rol}`),
        err => console.error(`[AuthService] Login ERROR:`, err)
      )
    );
  }

  registrar(request: RegistroRequest): Observable<AuthResponse> {
    console.log(`[AuthService] Realizando registro de: ${request.email}`);
    return this.http.post<AuthResponse>(`${this.apiUrl}/registro`, request).pipe(
      tap(
        res => console.log(`[AuthService] Registro SUCCESS para ${res.email}`),
        err => console.error(`[AuthService] Registro ERROR:`, err)
      )
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('rol');
  }

  getNombre(): string | null {
    return localStorage.getItem('nombre');
  }

  getId(): number | null {
    const id = localStorage.getItem('id');
    return id ? parseInt(id, 10) : null;
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    localStorage.removeItem('rol');
    localStorage.removeItem('nombre');
    localStorage.removeItem('email');
  }
}

