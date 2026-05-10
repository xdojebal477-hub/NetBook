import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService, UsuarioAdminResponse, LibroAdminResponse, CrearUsuarioAdminRequest, ComunidadAdminResponse, CrearComunidadAdminRequest } from '../../../core/services/admin.service';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { LoadingStateComponent } from '../../../core/components/loading-state/loading-state.component';
import { AdminResenasComponent } from '../admin-resenas/admin-resenas.component';
import { completeAfterMinimumDelay } from '../../../core/utils/loading-delay';

@Component({
  selector: 'app-panel-admin',
  imports: [CommonModule, FormsModule, LoadingStateComponent, AdminResenasComponent],
  templateUrl: './panel-admin.component.html',
  styleUrls: ['./panel-admin.component.css']
})
export class PanelAdminComponent implements OnInit {
  activeTab: 'usuarios' | 'libros' | 'resenas' | 'comunidades' = 'usuarios';

  usuarios: UsuarioAdminResponse[] = [];
  libros: LibroAdminResponse[] = [];
  comunidades: ComunidadAdminResponse[] = [];
  miembrosComunidad: UsuarioAdminResponse[] = [];
  usuariosDisponiblesComunidad: UsuarioAdminResponse[] = [];
  comunidadSeleccionadaId: number | null = null;

  mostrarFormularioUsuario = false;
  mostrarFormularioComunidad = false;
  editandoComunidadId: number | null = null;
  nuevoUsuario: CrearUsuarioAdminRequest = {
    nombre: '',
    email: '',
    password: '',
    rol: 'LECTOR'
  };
  nuevaComunidad: CrearComunidadAdminRequest = {
    nombre: '',
    descripcion: '',
    imagenUrl: '',
    generos: []
  };
  generosDisponibles = ['Ficcion', 'Fantasia', 'Ciencia Ficcion', 'Novela Historica', 'Romance', 'Terror'];

  cargando = true;
  error = '';
  mensajeExito = '';
  creandoUsuario = false;
  creandoComunidad = false;
  operandoMiembro = false;

  private readonly loadingDelayMs = 700;

  constructor(private adminService: AdminService, private router: Router) {}

  ngOnInit(): void {
    if (localStorage.getItem('rol') !== 'ADMIN') {
      this.router.navigate(['/catalogo']);
      return;
    }
    this.cargarDatosIniciales();
  }

  cargarDatosIniciales() {
    this.cargando = true;
    const startedAt = Date.now();

    forkJoin({
      usuarios: this.adminService.getUsuarios(),
      libros: this.adminService.getLibros(),
      comunidades: this.adminService.getComunidades()
    }).subscribe({
      next: ({ usuarios, libros, comunidades }) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.usuarios = usuarios;
          this.libros = libros;
          this.comunidades = comunidades;
          this.cargando = false;
        });
      },
      error: (err) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'Error cargando datos del panel de administración.';
          this.cargando = false;
        });
      }
    });
  }

  cargarUsuarios() {
    this.cargando = true;
    const startedAt = Date.now();
    this.adminService.getUsuarios().subscribe({
      next: (res) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.usuarios = res;
          this.cargando = false;
        });
      },
      error: (err) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.error = 'Error cargando usuarios: ' + err.message;
          this.cargando = false;
        });
      }
    });
  }

  cargarLibros() {
    this.cargando = true;
    const startedAt = Date.now();
    this.adminService.getLibros().subscribe({
      next: (res) => completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
        this.libros = res;
        this.cargando = false;
      }),
      error: (err) => completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
        console.error(err);
        this.cargando = false;
      })
    });
  }

  cargarComunidades() {
    this.cargando = true;
    const startedAt = Date.now();
    this.adminService.getComunidades().subscribe({
      next: (res) => completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
        this.comunidades = res;
        this.cargando = false;
      }),
      error: (err) => completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
        console.error(err);
        this.error = 'Error cargando comunidades.';
        this.cargando = false;
      })
    });
  }

  setTab(tab: 'usuarios' | 'libros' | 'resenas' | 'comunidades') {
    this.activeTab = tab;
    if (tab === 'comunidades' && this.comunidades.length === 0) {
      this.cargarComunidades();
    }
  }

  abrirFormularioUsuario() {
    this.mensajeExito = '';
    this.error = '';
    this.mostrarFormularioUsuario = true;
  }

  cancelarFormularioUsuario() {
    this.mostrarFormularioUsuario = false;
    this.nuevoUsuario = {
      nombre: '',
      email: '',
      password: '',
      rol: 'LECTOR'
    };
  }

  abrirFormularioComunidad(comunidad?: ComunidadAdminResponse) {
    this.error = '';
    this.mensajeExito = '';
    this.mostrarFormularioComunidad = true;
    this.editandoComunidadId = comunidad?.id ?? null;

    this.nuevaComunidad = comunidad ? {
      nombre: comunidad.nombre,
      descripcion: comunidad.descripcion ?? '',
      imagenUrl: comunidad.imagenUrl ?? '',
      generos: [...(comunidad.generos ?? [])]
    } : {
      nombre: '',
      descripcion: '',
      imagenUrl: '',
      generos: []
    };
  }

  cancelarFormularioComunidad() {
    this.mostrarFormularioComunidad = false;
    this.editandoComunidadId = null;
    this.nuevaComunidad = {
      nombre: '',
      descripcion: '',
      imagenUrl: '',
      generos: []
    };
  }

  toggleGenero(genero: string) {
    const index = this.nuevaComunidad.generos.indexOf(genero);
    if (index >= 0) {
      this.nuevaComunidad.generos.splice(index, 1);
    } else {
      this.nuevaComunidad.generos.push(genero);
    }
  }

  crearUsuario() {
    this.error = '';
    this.mensajeExito = '';

    if (!this.nuevoUsuario.nombre.trim() || !this.nuevoUsuario.email.trim() || !this.nuevoUsuario.password.trim()) {
      this.error = 'Completa nombre, email y contraseña antes de crear la cuenta.';
      return;
    }

    this.creandoUsuario = true;
    this.adminService.crearUsuario(this.nuevoUsuario).subscribe({
      next: (res) => {
        this.creandoUsuario = false;
        this.mensajeExito = `Cuenta de ${res.nombre} creada correctamente.`;
        this.cancelarFormularioUsuario();
        this.cargarUsuarios();
      },
      error: (err) => {
        this.creandoUsuario = false;
        this.error = err?.error?.message || 'No se pudo crear la cuenta.';
      }
    });
  }

  guardarComunidad() {
    this.error = '';
    this.mensajeExito = '';

    if (!this.nuevaComunidad.nombre.trim()) {
      this.error = 'La comunidad necesita un nombre.';
      return;
    }

    this.creandoComunidad = true;
    const peticion = this.editandoComunidadId
      ? this.adminService.actualizarComunidad(this.editandoComunidadId, this.nuevaComunidad)
      : this.adminService.crearComunidad(this.nuevaComunidad);

    peticion.subscribe({
      next: (res) => {
        this.creandoComunidad = false;
        this.mensajeExito = this.editandoComunidadId
          ? `Comunidad ${res.nombre} actualizada correctamente.`
          : `Comunidad ${res.nombre} creada correctamente.`;
        this.cancelarFormularioComunidad();
        this.cargarComunidades();
      },
      error: (err) => {
        this.creandoComunidad = false;
        this.error = err?.error?.message || 'No se pudo guardar la comunidad.';
      }
    });
  }

  borrarComunidad(id: number) {
    if (confirm('¿Estás seguro de borrar esta comunidad?')) {
      this.adminService.borrarComunidad(id).subscribe({
        next: () => {
          this.mensajeExito = 'Comunidad eliminada correctamente.';
          this.cargarComunidades();
          if (this.comunidadSeleccionadaId === id) {
            this.comunidadSeleccionadaId = null;
            this.miembrosComunidad = [];
            this.usuariosDisponiblesComunidad = [];
          }
        },
        error: () => this.error = 'No se pudo eliminar la comunidad.'
      });
    }
  }

  seleccionarComunidad(id: number) {
    this.comunidadSeleccionadaId = id;
    this.cargarMiembrosYDisponibles(id);
  }

  cargarMiembrosYDisponibles(id: number) {
    this.adminService.getMiembrosComunidad(id).subscribe({
      next: (miembros) => this.miembrosComunidad = Array.isArray(miembros) ? miembros : [],
      error: () => this.miembrosComunidad = []
    });

    this.adminService.getUsuariosDisponiblesParaComunidad(id).subscribe({
      next: (usuarios) => this.usuariosDisponiblesComunidad = usuarios,
      error: () => this.usuariosDisponiblesComunidad = []
    });
  }

  agregarUsuarioAComunidad(usuarioId: number) {
    if (!this.comunidadSeleccionadaId) return;
    this.operandoMiembro = true;
    this.adminService.anadirMiembroAComunidad(this.comunidadSeleccionadaId, usuarioId).subscribe({
      next: (msg) => {
        this.operandoMiembro = false;
        this.mensajeExito = msg;
        this.cargarMiembrosYDisponibles(this.comunidadSeleccionadaId!);
        this.cargarComunidades();
      },
      error: (err) => {
        this.operandoMiembro = false;
        this.error = err?.error || 'No se pudo añadir el usuario.';
      }
    });
  }

  quitarUsuarioDeComunidad(usuarioId: number) {
    if (!this.comunidadSeleccionadaId) return;
    if (!confirm('¿Quitar este usuario de la comunidad?')) return;
    this.operandoMiembro = true;
    this.adminService.eliminarMiembroDeComunidad(this.comunidadSeleccionadaId, usuarioId).subscribe({
      next: (msg) => {
        this.operandoMiembro = false;
        this.mensajeExito = msg;
        this.cargarMiembrosYDisponibles(this.comunidadSeleccionadaId!);
        this.cargarComunidades();
      },
      error: (err) => {
        this.operandoMiembro = false;
        this.error = err?.error || 'No se pudo quitar el usuario.';
      }
    });
  }

  // --- Usuarios ---
  borrarUsuario(id: number) {
    if(confirm('¿Estás seguro de borrar este usuario? Esta acción es irreversible.')) {
      this.adminService.borrarUsuario(id).subscribe({
        next: () => this.cargarUsuarios(),
        error: (err) => alert('No se pudo borrar el usuario')
      });
    }
  }

  cambiarRol(id: number, nuevoRol: string) {
    if(confirm(`¿Estás seguro de cambiar el rol a ${nuevoRol}?`)){
      this.adminService.cambiarRol(id, nuevoRol).subscribe({
        next: () => this.cargarUsuarios(),
        error: (err) => alert('Error al cambiar rol')
      });
    }
  }

  // --- Libros ---
  borrarLibro(id: number) {
    if(confirm('¿Estás seguro de borrar este libro del sistema?')) {
       this.adminService.borrarLibro(id).subscribe({
         next: () => this.cargarLibros(),
         error: (err) => alert('No se pudo borrar el libro')
       });
    }
  }
}
