import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  mensajeExito: string = '';
  mensajeError: string = '';
  cargando: boolean = false;
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.cargando = true;
      this.mensajeError = '';
      this.mensajeExito = '';

      // Usamos el servicio real. Si quisieras el mock: this.authService.mockLogin(...)
      this.authService.login(this.loginForm.value).subscribe({
        next: (res) => {
          this.cargando = false;
          this.mensajeExito = '¡Inicio de sesión correcto! Redirigiendo...';

          //  Guardar token y datos del usuario en localStorage
          localStorage.setItem('token', res.token);
          if (res.rol) localStorage.setItem('rol', res.rol);
          if (res.nombre) localStorage.setItem('nombre', res.nombre);
          if (res.email) localStorage.setItem('email', res.email);

          setTimeout(() => {
            if(res.rol === 'AUTOR') {
              this.router.navigate(['/panel-autor']);
            } else {
               this.router.navigate(['/catalogo']);
            }
          }, 1000);
        },
        error: (err) => {
          this.cargando = false;
          this.mensajeError = 'Credenciales incorrectas. Por favor, inténtalo de nuevo.';
          console.error('Error en login', err);
        }
      });
    }
  }
}
