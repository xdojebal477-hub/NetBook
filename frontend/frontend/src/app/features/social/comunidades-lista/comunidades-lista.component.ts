import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ComunidadService } from '../../../core/services/comunidad/comunidad.service';
import { ComunidadResponse, Page, ComunidadRequest } from '../../../core/models/comunidad.model';

@Component({
  selector: 'app-comunidades-lista',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './comunidades-lista.component.html',
  styleUrls: ['./comunidades-lista.component.css']
})
export class ComunidadesListaComponent implements OnInit {
  comunidades: ComunidadResponse[] = [];
  comunidadesPropias: ComunidadResponse[] = [];
  comunidadesDisponibles: ComunidadResponse[] = [];
  page: number = 0;
  totalPages: number = 0;
  generoFiltro: string = '';

  // Modo creacion
  creando: boolean = false;
  nuevaComunidad: ComunidadRequest = {
    nombre: '',
    descripcion: '',
    generos: []
  };

  generosList: string[] = ['Ficcion', 'Fantasia', 'Ciencia Ficcion', 'Novela Historica', 'Romance', 'Terror'];

  constructor(
    private comunidadService: ComunidadService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarComunidades();
  }

  cargarComunidades(): void {
    this.comunidadService.listarComunidades(this.generoFiltro, this.page)
      .subscribe({
        next: (data) => {
          this.comunidades = data.content;
          this.comunidadesPropias = data.content.filter(comunidad => comunidad.esMiembro);
          this.comunidadesDisponibles = data.content.filter(comunidad => !comunidad.esMiembro);
          this.totalPages = data.totalPages;
        },
        error: (err) => console.error('Error cargando comunidades', err)
      });
  }

  filtrarPorGenero(genero: string): void {
    this.generoFiltro = this.generoFiltro === genero ? '' : genero;
    this.page = 0;
    this.cargarComunidades();
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < this.totalPages) {
      this.page = nuevaPagina;
      this.cargarComunidades();
    }
  }

  unirseYEntrar(comunidad: ComunidadResponse): void {
    if (comunidad.esMiembro) {
      this.router.navigate(['/comunidades', comunidad.id]);
      return;
    }

    this.comunidadService.unirseComunidad(comunidad.id).subscribe({
      next: () => {
        this.router.navigate(['/comunidades', comunidad.id]);
      },
      error: (err) => {
        if (err.status === 400 || err.status === 200) {
           this.router.navigate(['/comunidades', comunidad.id]);
        } else {
           console.error('Error al unirse', err);
           this.router.navigate(['/comunidades', comunidad.id]);
        }
      }
    });
  }

  toggleCrear(): void {
    this.creando = !this.creando;
  }

  toggleGenero(genero: string): void {
    const idx = this.nuevaComunidad.generos.indexOf(genero);
    if (idx >= 0) {
      this.nuevaComunidad.generos.splice(idx, 1);
    } else {
      this.nuevaComunidad.generos.push(genero);
    }
  }

  guardarNuevaComunidad(): void {
    if (!this.nuevaComunidad.nombre) return;

    this.comunidadService.crearComunidad(this.nuevaComunidad).subscribe({
      next: (resp) => {
        this.creando = false;
        this.nuevaComunidad = { nombre: '', descripcion: '', generos: [] };
        this.router.navigate(['/comunidades', resp.id]);
      },
      error: (err) => console.error('Error creando', err)
    });
  }
}

