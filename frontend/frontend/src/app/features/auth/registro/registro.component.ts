import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent {
  registroForm: FormGroup;
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
      this.authService.registrar(this.registroForm.value).subscribe({
        next: (res) => {
          console.log('Registro exitoso. Token:', res.token);
          // Guardamos el token para hacer la prueba del Panel de Autor
          localStorage.setItem('token', res.token);
          
          if(this.registroForm.value.rol === 'AUTOR'){
             this.router.navigate(['/panel-autor']);
          } else {
             // Redirigir al inicio o catálogo
          }
        },
        error: (err) => {
          console.error('Error en registro', err);
        }
      });
    }
  }
}
