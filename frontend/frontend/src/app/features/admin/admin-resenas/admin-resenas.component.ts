import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ResenaService } from '../../../core/services/resena.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

interface ResenaAdmin {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  libroId: number;
  puntuacion: number;
  comentario?: string;
  sentimientoIA?: string;
  fechaCreacion: string;
}

@Component({
  selector: 'app-admin-resenas',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingStateComponent],
  templateUrl: './admin-resenas.component.html',
  styleUrls: ['./admin-resenas.component.css']
})
export class AdminResenasComponent implements OnInit {
  resenas: ResenaAdmin[] = [];
  cargando = true;
  error = '';
  mensajeExito = '';
  eliminando = false;
  resenaEliminando: number | null = null;

  filtroLibroId = '';
  filtroUsuarioId = '';
  resenasFiltradas: ResenaAdmin[] = [];

  private readonly loadingDelayMs = 700;

  constructor(private resenaService: ResenaService) {}

  ngOnInit(): void {
    this.cargarResenas();
  }

  cargarResenas() {
    this.cargando = true;
    const startedAt = Date.now();

    this.resenaService.obtenerTodasResenas().subscribe({
      next: (res) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.resenas = res;
          this.aplicarFiltros();
          this.cargando = false;
        });
      },
      error: (err) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'Error cargando reseñas: ' + (err?.error?.message || err.message);
          this.cargando = false;
        });
      }
    });
  }

  aplicarFiltros() {
    this.resenasFiltradas = this.resenas.filter(r => {
      const coincideLibro = !this.filtroLibroId || r.libroId.toString() === this.filtroLibroId;
      const coincideUsuario = !this.filtroUsuarioId || r.usuarioId.toString() === this.filtroUsuarioId;
      return coincideLibro && coincideUsuario;
    });
  }

  onFiltroChange() {
    this.aplicarFiltros();
  }

  eliminarResena(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar esta reseña?')) {
      this.eliminando = true;
      this.resenaEliminando = id;
      this.error = '';
      this.mensajeExito = '';

      this.resenaService.eliminarResenaAdmin(id).subscribe({
        next: () => {
          this.eliminando = false;
          this.resenaEliminando = null;
          this.mensajeExito = 'Reseña eliminada correctamente.';
          this.cargarResenas();
        },
        error: (err) => {
          this.eliminando = false;
          this.resenaEliminando = null;
          this.error = 'Error eliminando reseña: ' + (err?.error?.message || err.message);
        }
      });
    }
  }

  formatearFecha(fecha: string): string {
    const d = new Date(fecha);
    const hoy = new Date();
    const ayer = new Date(hoy);
    ayer.setDate(ayer.getDate() - 1);

    if (d.toDateString() === hoy.toDateString()) {
      return 'Hoy';
    } else if (d.toDateString() === ayer.toDateString()) {
      return 'Ayer';
    } else {
      return d.toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });
    }
  }

  obtenerEstrellas(puntuacion: number): string {
    const estrella = '★';
    const entero = Math.floor(puntuacion);
    const decimal = puntuacion - entero;
    let resultado = estrella.repeat(entero);
    if (decimal >= 0.5) {
      resultado += '½';
    }
    return resultado;
  }
}
