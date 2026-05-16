import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface LibroResumen {
  id: number;
  titulo: string;
  portadaUrl?: string;
  genero: string;
}

export interface ColeccionResumen {
  id: number;
  nombre: string;
  fechaCreacion: string;
  libros: LibroResumen[];
}

export interface ComunidadResumen {
  id: number;
  nombre: string;
  descripcion: string;
  imagenUrl?: string;
  fechaCreacion: string;
  propietarioNombre: string;
  numeroMiembros: number;
}

export interface UsuarioPerfil {
  id: number;
  nombre: string;
  avatarUrl?: string;
  rol: string;
  fechaRegistro: string;
  coleccionesPublicas: ColeccionResumen[];
  obrasPublicadas?: LibroResumen[];
  comunidades: ComunidadResumen[];
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) { }

  obtenerPerfilPublico(id: number): Observable<UsuarioPerfil> {
    return this.http.get<UsuarioPerfil>(`${this.apiUrl}/${id}/perfil`);
  }
}
