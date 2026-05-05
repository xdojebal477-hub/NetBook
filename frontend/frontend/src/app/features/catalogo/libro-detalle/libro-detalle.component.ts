import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LibroService, LibroResponse } from '../../../core/services/libro.service';
import { ColeccionService, ColeccionResponse } from '../../../core/services/coleccion.service';
import { AuthService } from '../../../core/services/auth.service';
import { ResenaService } from '../../../core/services/resena.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { FormResenaComponent } from '../resenias/form-resena.component';
import { ListaResenasComponent } from '../resenias/lista-resenas.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-libro-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingStateComponent, FormResenaComponent, ListaResenasComponent],
  templateUrl: './libro-detalle.component.html',
  styleUrls: ['./libro-detalle.component.css']
})
export class LibroDetalleComponent implements OnInit {
  libro: LibroResponse | null = null;
  colecciones: ColeccionResponse[] = [];
  coleccionSeleccionada: number | null = null;
  cargando = true;
  error = '';
  mensajeExito = '';
  ratingPromedio = 0;
  countResenas = 0;

  private readonly loadingDelayMs = 700;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private libroService: LibroService,
    private coleccionService: ColeccionService,
    private resenaService: ResenaService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarLibro(Number(id));
      this.cargarColecciones();
    }
  }

  cargarLibro(id: number): void {
    this.cargando = true;
    const startedAt = Date.now();
    this.libroService.obtenerLibroPorId(id).subscribe({
      next: (data) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.libro = data;
          this.ratingPromedio = data.avgRating || 0;
          this.countResenas = data.reviewCount || 0;
          this.cargando = false;
        });
      },
      error: (err) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'Error al cargar los detalles del libro.';
          this.cargando = false;
        });
      }
    });
  }

  cargarColecciones(): void {
    if (localStorage.getItem('token')) {
      this.coleccionService.obtenerMisColecciones().subscribe({
        next: (data) => {
          this.colecciones = data;
        },
        error: (err) => console.error('Error al cargar colecciones', err)
      });
    }
  }

  anadirAColeccion(): void {
    if (!this.coleccionSeleccionada || !this.libro) return;

    this.coleccionService.anadirLibroAColeccion(this.coleccionSeleccionada, this.libro.id).subscribe({
      next: () => {
        this.mensajeExito = '¡Libro añadido a la colección con éxito!';
        setTimeout(() => this.mensajeExito = '', 3000);
      },

      error: (err) => {
        console.error(err);
        alert('Error al añadir el libro a la colección o ya existe.');
      }
    });
  }

  volver(): void {
    history.back();
  }

  empezarALeer(): void {
    if (this.libro) {
      if (this.authService.isLoggedIn()) {
        this.router.navigate(['/visor', this.libro.id]);
      } else {
        this.router.navigate(['/login']);
      }
    }
  }

  onResenaGuardada(): void {
    // Recargar datos de reseñas cuando se guarda una nueva
    if (this.libro) {
      this.resenaService.obtenerRatingPromedio(this.libro.id).subscribe({
        next: (rating) => {
          this.ratingPromedio = rating;
        }
      });
      this.resenaService.contarResenas(this.libro.id).subscribe({
        next: (count) => {
          this.countResenas = count;
        }
      });
    }
  }
}
