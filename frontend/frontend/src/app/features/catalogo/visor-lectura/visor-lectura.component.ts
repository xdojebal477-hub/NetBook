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
  esMovil = false;
  private documentoAbiertoExterno = false;

  private readonly loadingDelayMs = 700;

  constructor(
    private route: ActivatedRoute,
    private libroService: LibroService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.esMovil = this.detectarDispositivoMovil();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarVisor(Number(id));
    }
  }

  private detectarDispositivoMovil(): boolean {
    if (typeof window === 'undefined') {
      return false;
    }

    return window.matchMedia('(max-width: 768px)').matches || /Android|iPhone|iPad|iPod/i.test(navigator.userAgent);
  }

  abrirEnVisorExterno(enMismaPestana = false): void {
    if (!this.archivoUrlBase) {
      return;
    }

    this.documentoAbiertoExterno = true;

    if (enMismaPestana) {
      window.location.href = this.archivoUrlBase;
      return;
    }

    const ventana = window.open(this.archivoUrlBase, '_blank', 'noopener,noreferrer');

    // Fallback when popup is blocked or the browser ignores blob URLs in new tabs.
    if (!ventana) {
      const enlace = document.createElement('a');
      enlace.href = this.archivoUrlBase;
      enlace.target = '_self';
      enlace.rel = 'noopener noreferrer';
      enlace.click();
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

        if (this.esMovil) {
          this.abrirEnVisorExterno(true);
        }

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
    if (this.archivoUrlBase && !this.documentoAbiertoExterno) {
      window.URL.revokeObjectURL(this.archivoUrlBase);
    }
  }

  volver(): void {
    history.back();
  }
}
