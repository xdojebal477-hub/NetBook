import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
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
      token: 'mock-jwt-token-h4sh3d',
      nombre: 'Usuario Mock',
      email: request.email,
      rol: 'LECTOR'
    };
    return of(mockResponse); // Simula una respuesta asíncrona
  }

  //  Llamada real al backend Sprint 1 que ya tenemos funcionando
  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request);
  }

  registrar(request: RegistroRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/registro`, request);
  }
}
