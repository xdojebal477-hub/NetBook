import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ResenaService, CreateResenaRequest, ResenaResponse } from '../../../core/services/resena.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-form-resena',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, LoadingStateComponent],
  templateUrl: './form-resena.component.html',
  styleUrls: ['./form-resena.component.css']
})
export class FormResenaComponent implements OnInit {
  @Input() libroId!: number;
  @Output() resenaGuardada = new EventEmitter<void>();

  formulario!: FormGroup;
  miResena: ResenaResponse | null = null;
  cargando = false;
  error = '';
  exito = '';
  calificacionActual = 0;

  private readonly loadingDelayMs = 700;

  constructor(
    private fb: FormBuilder,
    private resenaService: ResenaService
  ) {
    this.formulario = this.fb.group({
      puntuacion: [null, [Validators.required, Validators.min(0.5), Validators.max(5.0)]],
      comentario: ['', [Validators.maxLength(1000)]]
    });
  }

  ngOnInit(): void {
    this.cargarMiResena();
  }

  cargarMiResena(): void {
    this.cargando = true;
    const startedAt = Date.now();
    this.resenaService.obtenerMiResena(this.libroId).subscribe({
      next: (data: ResenaResponse) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.miResena = data;
          this.formulario.patchValue({
            puntuacion: data.puntuacion,
            comentario: data.comentario
          });
          this.calificacionActual = data.puntuacion;
          this.cargando = false;
        });
      },
      error: () => {
        // No existe reseña previa, es normal
        this.cargando = false;
      }
    });
  }

  seleccionarCalificacion(valor: number): void {
    this.calificacionActual = valor;
    this.formulario.patchValue({ puntuacion: valor });
  }

  guardarResena(): void {
    if (this.formulario.invalid) {
      this.error = 'La puntuación es requerida';
      return;
    }

    const request: CreateResenaRequest = {
      puntuacion: this.formulario.get('puntuacion')?.value,
      comentario: this.formulario.get('comentario')?.value
    };

    this.cargando = true;
    const startedAt = Date.now();
    this.resenaService.crearOActualizarResena(this.libroId, request).subscribe({
      next: (data: ResenaResponse) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.miResena = data;
          this.exito = 'Reseña guardada exitosamente';
          this.error = '';
          this.cargando = false;
          this.resenaGuardada.emit();

          // Limpiar mensaje de éxito después de 3 segundos
          setTimeout(() => {
            this.exito = '';
          }, 3000);
        });
      },
      error: () => {
        this.error = 'Error al guardar la reseña';
        this.cargando = false;
      }
    });
  }

  eliminarResena(): void {
    if (!confirm('¿Estás seguro de que deseas eliminar tu reseña?')) {
      return;
    }

    this.cargando = true;
    const startedAt = Date.now();
    this.resenaService.eliminarResena(this.libroId).subscribe({
      next: () => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.miResena = null;
          this.formulario.reset();
          this.calificacionActual = 0;
          this.exito = 'Reseña eliminada exitosamente';
          this.error = '';
          this.cargando = false;
          this.resenaGuardada.emit();

          // Limpiar mensaje de éxito después de 3 segundos
          setTimeout(() => {
            this.exito = '';
          }, 3000);
        });
      },
      error: () => {
        this.error = 'Error al eliminar la reseña';
        this.cargando = false;
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
}
