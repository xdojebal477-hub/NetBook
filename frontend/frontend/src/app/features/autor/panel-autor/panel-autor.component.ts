import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LibroService, LibroResponse, LibroUpdateRequest } from '../../../core/services/libro.service';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-panel-autor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LoadingStateComponent],
  templateUrl: './panel-autor.component.html',
  styleUrl: './panel-autor.component.css'
})
export class PanelAutorComponent implements OnInit {

  private fb = inject(FormBuilder);
  private libroService = inject(LibroService);

  public libroForm: FormGroup;
  public archivoSeleccionado: File | null = null;
  public portadaSeleccionada: File | null = null;
  public misObras: LibroResponse[] = [];
  public modoEdicion: boolean = false;
  public idLibroEditando: number | null = null;

  public mensajeExito: string = '';
  public mensajeError: string = '';
  public cargando: boolean = false;

  private readonly loadingDelayMs = 700;

  constructor() {
    // Definición de los campos que recibe el back
    this.libroForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(200)]],
      sinopsis: [''],
      genero: [''],
      portadaUrl: [''], // Lo mantenemos para mostrar que ya existe una si estamos en edicion, pero en frontend será ignarado al submitir, enviaremos la File
      estado: ['BORRADOR'] // para gestionar los estados de las obras de un autor
    });
  }

  ngOnInit(): void {//al renderizar el componente, se cargan las obras del autor para mostrarlas en la tabla
    this.cargarMisObras();
  }

  // Cargar libros del autor
  cargarMisObras(): void {
    this.cargando = true;
    const startedAt = Date.now();
    this.libroService.obtenerMisObras().subscribe({
      next: (data) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.misObras = data;
          this.cargando = false;
        });
      },
      error: (err) => completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
        console.error('Error al cargar obras:', err);
        this.cargando = false;
      })
    });
  }

  // Detecta el momento en el que el autor selecciona el archivo de la portada
  onPortadaSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      if (file.type.startsWith('image/')) {
        this.portadaSeleccionada = file;
        this.mensajeError = '';
      } else {
        this.portadaSeleccionada = null;
        this.mensajeError = 'Formato de imagen inválido. Solo se admiten archivos PNG, JPG o WEBP.';
      }
    }
  }

  // Detecta el momento en el que el autor selecciona el archivo s
  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      // Validamos el tipo de archivo
      if (file.type === 'application/pdf' || file.type === 'text/plain') {
        this.archivoSeleccionado = file;
        this.mensajeError = ''; // Borramos si antes hubo error
      } else {
        this.archivoSeleccionado = null;
        this.mensajeError = 'Formato inválido. Solo se admiten archivos .pdf o .txt';
      }
    }
  }

  // Para modo edición: cargar los datos de un libro en el formulario
  cargarParaEditar(libro: LibroResponse): void {
    this.modoEdicion = true;
    this.idLibroEditando = libro.id;
    this.mensajeExito = '';
    this.mensajeError = '';

    this.libroForm.patchValue({
      titulo: libro.titulo,
      sinopsis: libro.sinopsis,
      genero: libro.genero,
      portadaUrl: libro.portadaUrl,
      estado: libro.estado
    });

    // Scrollear hacia arriba (al formulario) de manera simple
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  // Cancelar la edición y volver a modo subida
  cancelarEdicion(): void {
    this.modoEdicion = false;
    this.idLibroEditando = null;
    this.libroForm.reset({ estado: 'BORRADOR' });
    this.archivoSeleccionado = null;
    this.portadaSeleccionada = null;
    this.mensajeExito = '';
    this.mensajeError = '';
  }

  // Borrar un libro con ventana de confirmación
  borrarLibro(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar permanentemente esta obra?')) {
      this.libroService.eliminarLibro(id).subscribe({
        next: () => {
          this.mensajeExito = 'Obra eliminada correctamente.';
          this.cargarMisObras(); // Refrescar la tabla
        },
        error: (err) => {
          this.mensajeError = 'No se pudo eliminar la obra. ' + (err.status === 403 ? 'No tienes permiso.' : '');
        }
      });
    }
  }

  public onSubmit(): void {
    // Si estamos editando
    if (this.modoEdicion && this.idLibroEditando !== null) {
      if (this.libroForm.invalid) {
        this.mensajeError = 'Revisa los campos requeridos.';
        return;
      }

      const request: LibroUpdateRequest = {
        titulo: this.libroForm.get('titulo')?.value,
        sinopsis: this.libroForm.get('sinopsis')?.value,
        genero: this.libroForm.get('genero')?.value,
        estado: this.libroForm.get('estado')?.value
      };

      this.cargando = true;
      this.libroService.actualizarLibro(this.idLibroEditando, request, this.portadaSeleccionada).subscribe({
        next: (response) => {
          this.cargando = false;
          this.mensajeExito = `Libro '${response.titulo}' actualizado con éxito.`;
          this.cargarMisObras();
          this.cancelarEdicion();
        },
        error: (err) => {
          this.cargando = false;
          this.mensajeError = 'Error al actualizar el libro.';
        }
      });
      return;
    }

    // SI ESTAMOS CREANDO
    // si el contenido del formulario no es valido o no se ha seleccionado un archivo
    if (this.libroForm.invalid || !this.archivoSeleccionado) {
      this.mensajeError = 'Revisa los campos del formulario y asegúrate de añadir un archivo.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    const titulo = this.libroForm.get('titulo')?.value;
    const sinopsis = this.libroForm.get('sinopsis')?.value;
    const genero = this.libroForm.get('genero')?.value;

    // Llamada al backend
    this.libroService.subirLibro(titulo, sinopsis, genero, this.portadaSeleccionada, this.archivoSeleccionado)
      .subscribe({
        next: (response: LibroResponse) => {
          this.cargando = false;
          this.mensajeExito = `¡Libro '${response.titulo}' subido correctamente en estado ${response.estado}!`;
          this.libroForm.reset({ estado: 'BORRADOR' });
          this.archivoSeleccionado = null;
          this.cargarMisObras(); // Refrescar la tabla
        },
        error: (err) => {
          this.cargando = false;
          if (err.status === 429) {
            this.mensajeError = 'Has superado el límite visual de subidas por minuto. Espera un rato.';
          } else if (err.status === 403) {
            this.mensajeError = 'Acceso denegado: Solo los autores pueden subir libros.';
          } else {
            this.mensajeError = 'Error al subir el archivo al servidor. Inténtalo de nuevo.';
          }
        }
      });
  }
}
