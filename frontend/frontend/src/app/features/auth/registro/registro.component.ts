import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent {
  registroForm: FormGroup;
  mensajeExito: string = '';
  mensajeError: string = '';
  cargando: boolean = false;
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rol: ['LECTOR', Validators.required] // Añadido el selector de Rol
    });
  }

  onSubmit() {
    if (this.registroForm.valid) {
      this.cargando = true;
      this.mensajeError = '';
      this.mensajeExito = '';

      this.authService.registrar(this.registroForm.value).subscribe({
        next: (res) => {
          this.cargando = false;
          this.mensajeExito = '¡Registro completado! Redirigiendo a tu panel...';

          // Guardamos el token y datos para la sesión
          localStorage.setItem('token', res.token);
          localStorage.setItem('rol', res.rol || this.registroForm.value.rol);
          if (res.nombre) localStorage.setItem('nombre', res.nombre);
          if (res.email) localStorage.setItem('email', res.email);

          setTimeout(() => {
            if(this.registroForm.value.rol === 'AUTOR'){
               this.router.navigate(['/panel-autor']);
            } else {
               this.router.navigate(['/catalogo']);
            }
          }, 1000);
        },
        error: (err) => {
          this.cargando = false;
          this.mensajeError = 'No se pudo completar el registro. El correo ya podría estar en uso.';
          console.error('Error en registro', err);
        }
      });
    }
  }
}
