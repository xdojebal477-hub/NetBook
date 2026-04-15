import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
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
      // Usamos el servicio real. Si quisieras el mock: this.authService.mockLogin(...)
      this.authService.login(this.loginForm.value).subscribe({
        next: (res) => {
          console.log('Login exitoso. Token:', res.token);
          //  Guardar el token en localStorage para que el panel de autor pueda usarlo
          localStorage.setItem('token', res.token);

          if(res.rol === 'AUTOR') {
            this.router.navigate(['/panel-autor']);
          } else {
             // Redirigir al inicio o catálogo
          }
        },
        error: (err) => {
          console.error('Error en login', err);
        }
      });
    }
  }
}
