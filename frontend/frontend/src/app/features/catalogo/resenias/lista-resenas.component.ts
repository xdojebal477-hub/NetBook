import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ResenaService, ResenaResponse } from '../../../core/services/resena.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-lista-resenas',
  standalone: true,
  imports: [CommonModule, LoadingStateComponent, RouterLink],
  templateUrl: './lista-resenas.component.html',
  styleUrls: ['./lista-resenas.component.css']
})
export class ListaResenasComponent implements OnInit {
  @Input() libroId!: number;
  @Input() ratingPromedio = 0;
  @Input() countResenas = 0;

  resenas: ResenaResponse[] = [];
  cargando = false;
  error = '';

  private readonly loadingDelayMs = 700;

  constructor(private resenaService: ResenaService) {}

  ngOnInit(): void {
    this.cargarResenas();
  }

  cargarResenas(): void {
    if (!this.libroId) {
      this.error = 'ID de libro no disponible';
      this.cargando = false;
      return;
    }

    this.cargando = true;
    const startedAt = Date.now();
    this.resenaService.obtenerResenasPorLibro(this.libroId).subscribe({
      next: (data) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.resenas = Array.isArray(data) ? data : [];
          this.cargando = false;
          this.error = '';
          console.log(` Reseñas cargadas: ${this.resenas.length}`, this.resenas);
        });
      },
      error: (err) => {
        this.cargando = false;
        console.error(' Error cargando reseñas:', err);
        // Si es 404 o el libro no tiene reseñas, mostrar lista vacía
        if (err.status === 404) {
          this.resenas = [];
          this.error = '';
        } else {
          this.error = 'Error al cargar las reseñas';
        }
      }
    });
  }

  obtenerEstrellas(valor: number): string[] {
    const estrellas = [];
    for (let i = 0; i < 10; i++) {
      if (i < valor * 2) {
        estrellas.push('full');
      } else if (i < Math.ceil(valor * 2)) {
        estrellas.push('half');
      } else {
        estrellas.push('empty');
      }
    }
    return estrellas;
  }

  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    const hoy = new Date();
    const ayer = new Date(hoy);
    ayer.setDate(ayer.getDate() - 1);

    if (date.toDateString() === hoy.toDateString()) {
      return 'Hoy';
    } else if (date.toDateString() === ayer.toDateString()) {
      return 'Ayer';
    } else {
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    }
  }
}
