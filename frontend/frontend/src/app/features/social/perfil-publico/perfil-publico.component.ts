import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UsuarioService, UsuarioPerfil } from '../../../core/services/usuario.service';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';

registerLocaleData(localeEs, 'es');

@Component({
  selector: 'app-perfil-publico',
  imports: [CommonModule, RouterLink],
  templateUrl: './perfil-publico.component.html',
  styleUrl: './perfil-publico.component.css',
  providers: [{ provide: LOCALE_ID, useValue: 'es' }]
})
export class PerfilPublicoComponent implements OnInit {
  perfil: UsuarioPerfil | null = null;
  cargando = true;
  error = '';
  pestanaActiva = 'colecciones'; // 'colecciones', 'obras', 'comunidades'

  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.cargarPerfil(+idParam);
      }
    });
  }

  cargarPerfil(id: number): void {
    this.cargando = true;
    this.error = '';
    this.usuarioService.obtenerPerfilPublico(id).subscribe({
      next: (data) => {
        this.perfil = data;
        if (this.perfil.rol === 'AUTOR') {
          this.pestanaActiva = 'obras';
        }
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando perfil', err);
        this.error = 'No se ha podido cargar el perfil del usuario. Es posible que no exista.';
        this.cargando = false;
      }
    });
  }
}
