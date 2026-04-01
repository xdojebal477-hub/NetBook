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
          // A futuro: guardar el token en localStorage y redirigir
          // localStorage.setItem('token', res.token);
          // this.router.navigate(['/catalogo']);
        },
        error: (err) => {
          console.error('Error en login', err);
        }
      });
    }
  }
}
