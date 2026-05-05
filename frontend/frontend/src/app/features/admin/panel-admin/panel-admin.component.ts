import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService, UsuarioAdminResponse, LibroAdminResponse, CrearUsuarioAdminRequest } from '../../../core/services/admin.service';
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
  activeTab: 'usuarios' | 'libros' | 'resenas' = 'usuarios';

  usuarios: UsuarioAdminResponse[] = [];
  libros: LibroAdminResponse[] = [];

  mostrarFormularioUsuario = false;
  nuevoUsuario: CrearUsuarioAdminRequest = {
    nombre: '',
    email: '',
    password: '',
    rol: 'LECTOR'
  };

  cargando = true;
  error = '';
  mensajeExito = '';
  creandoUsuario = false;

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
      libros: this.adminService.getLibros()
    }).subscribe({
      next: ({ usuarios, libros }) => {
        completeAfterMinimumDelay(startedAt, this.loadingDelayMs, () => {
          this.usuarios = usuarios;
          this.libros = libros;
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

  setTab(tab: 'usuarios' | 'libros' | 'resenas') {
    this.activeTab = tab;
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
