import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { LibroService, LibroResponse } from '../../../core/services/libro.service';

@Component({
  selector: 'app-catalogo-general',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './catalogo-general.component.html',
  styleUrl: './catalogo-general.component.css'
})
export class CatalogoGeneralComponent implements OnInit {
  libros: LibroResponse[] = [];
  librosFiltrados: LibroResponse[] = [];

  terminoBusqueda: string = '';
  generoSeleccionado: string = '';
  generosDisponibles: string[] = ['Fantasía', 'Terror', 'Romance', 'Ciencia Ficción', 'Poesía', 'Ensayo'];

  cargando: boolean = true;
  error: string | null = null;

  constructor(private libroService: LibroService) {}

  ngOnInit(): void {
    this.cargarCatalogo();
  }

  cargarCatalogo(): void {
    this.cargando = true;
    this.libroService.obtenerCatalogoGeneral().subscribe({
      next: (data) => {
        this.libros = data;
        this.librosFiltrados = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar el catálogo', err);
        this.error = 'Ocurrió un error al intentar cargar los libros. Inténtalo más tarde.';
        this.cargando = false;
      }
    });
  }

  filtrarLibros(): void {
    this.librosFiltrados = this.libros.filter(libro => {
      const coincideBusqueda = this.terminoBusqueda 
        ? libro.titulo.toLowerCase().includes(this.terminoBusqueda.toLowerCase()) || 
          (libro.autorNombre && libro.autorNombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase()))
        : true;
      
      const coincideGenero = this.generoSeleccionado
        ? libro.genero === this.generoSeleccionado
        : true;

      return coincideBusqueda && coincideGenero;
    });
  }

  limpiarFiltros(): void {
    this.terminoBusqueda = '';
    this.generoSeleccionado = '';
    this.librosFiltrados = this.libros;
  }
}
