import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ColeccionService, ColeccionResponse, ColeccionRequest } from '../../../core/services/coleccion.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-mis-colecciones',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LoadingStateComponent],
  templateUrl: './mis-colecciones.component.html',
  styleUrls: ['./mis-colecciones.component.css']
})
export class MisColeccionesComponent implements OnInit {
  colecciones: ColeccionResponse[] = [];
  cargando = true;
  error = '';

  nuevaColeccion: ColeccionRequest = { nombre: '', esPublica: false };
  mostrandoFormulario = false;

  coleccionEnEdicion: number | null = null;
  editNombre = '';
  editPublica = false;

  private readonly loadingDelayMs = 700;

  constructor(private coleccionService: ColeccionService) {}

  ngOnInit(): void {
    this.cargarColecciones();
  }

  cargarColecciones(): void {
    this.cargando = true;
    const startedAt = Date.now();
    this.coleccionService.obtenerMisColecciones().subscribe({
      next: (data) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.colecciones = data;
          this.cargando = false;
        });
      },
      error: (err) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'Error al cargar tus colecciones';
          this.cargando = false;
        });
      }
    });
  }

  crearColeccion(): void {
    if (!this.nuevaColeccion.nombre) return;
    this.coleccionService.crearColeccion(this.nuevaColeccion).subscribe({
      next: () => {
        this.nuevaColeccion = { nombre: '', esPublica: false };
        this.mostrandoFormulario = false;
        this.cargarColecciones();
      },
      error: (err) => console.error(err)
    });
  }

  eliminarColeccion(id: number): void {
    if (confirm('¿Seguro que deseas eliminar esta colección?')) {
      this.coleccionService.eliminarColeccion(id).subscribe({
        next: () => this.cargarColecciones(),
        error: (err) => console.error(err)
      });
    }
  }

  iniciarEdicion(c: ColeccionResponse): void {
    this.coleccionEnEdicion = c.id;
    this.editNombre = c.nombre;
    this.editPublica = c.esPublica;
  }

  guardarEdicion(): void {
    if (this.coleccionEnEdicion !== null) {
      this.coleccionService.actualizarColeccion(this.coleccionEnEdicion, { nombre: this.editNombre, esPublica: this.editPublica }).subscribe({
        next: () => {
          this.coleccionEnEdicion = null;
          this.cargarColecciones();
        }
      });
    }
  }

  quitarLibro(coleccionId: number, libroId: number): void {
    if(confirm('¿Eliminar libro de la colección?')) {
      this.coleccionService.quitarLibroDeColeccion(coleccionId, libroId).subscribe({
        next: () => this.cargarColecciones()
      });
    }
  }
}
