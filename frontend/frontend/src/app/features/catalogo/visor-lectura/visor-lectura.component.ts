import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { LibroService } from '../../../core/services/libro.service';
import { CommonModule } from '@angular/common';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-visor-lectura',
  standalone: true,
  imports: [CommonModule, LoadingStateComponent],
  templateUrl: './visor-lectura.component.html',
  styleUrls: ['./visor-lectura.component.css']
})
export class VisorLecturaComponent implements OnInit, OnDestroy {
  pdfUrl: SafeResourceUrl | null = null;
  cargando = true;
  error = '';
  archivoUrlBase = '';

  private readonly loadingDelayMs = 700;

  constructor(
    private route: ActivatedRoute,
    private libroService: LibroService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarVisor(Number(id));
    }
  }

  cargarVisor(id: number): void {
    this.cargando = true;
    const startedAt = Date.now();
    this.libroService.descargarLibro(id).subscribe({
      next: (blob) => {
        // Creamos una URL de tipo blob para que el navegador confie en el PDF
        this.archivoUrlBase = window.URL.createObjectURL(blob);
        // El sanitizer de angular debe permitir esta resource URL para usarla en un iframe
        this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.archivoUrlBase);
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.cargando = false;
        });
      },
      error: (err) => {
        console.error('Error al cargar el archivo de lectura', err);
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'No se pudo cargar el visor de lectura aségurese de estar conectado o de que el fichero existe.';
          this.cargando = false;
        });
      }
    });
  }

  ngOnDestroy(): void {
    // Limpiar la URL base cuando el componente se destruya para liberar mem
    if (this.archivoUrlBase) {
      window.URL.revokeObjectURL(this.archivoUrlBase);
    }
  }

  volver(): void {
    history.back();
  }
}
