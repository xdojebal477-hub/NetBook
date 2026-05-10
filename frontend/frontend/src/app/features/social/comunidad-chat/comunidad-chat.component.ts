import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ComunidadService } from '../../../core/services/comunidad/comunidad.service';
import { ComunidadResponse, MensajeChatResponse, UsuarioResponse } from '../../../core/models/comunidad.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-comunidad-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comunidad-chat.component.html',
  styleUrls: ['./comunidad-chat.component.css']
})
export class ComunidadChatComponent implements OnInit, OnDestroy, AfterViewChecked {

  @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

  comunidadId!: number;
  comunidad?: ComunidadResponse;
  miembros: UsuarioResponse[] = [];
  mensajes: MensajeChatResponse[] = [];
  nuevoMensajeTexto: string = '';
  miNombre: string | null = ''; // Para comparar

  private sseSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private comunidadService: ComunidadService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/comunidades']);
      return;
    }
    this.comunidadId = +id;
    this.miNombre = this.authService.getNombre();

    this.cargarInfoComunidad();
    this.cargarMiembros();
    this.cargarHistorialMensajes();
    this.conectarSSE();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }

  cargarInfoComunidad(): void {
    this.comunidadService.obtenerComunidad(this.comunidadId).subscribe(data => this.comunidad = data);
  }

  cargarMiembros(): void {
    this.comunidadService.obtenerMiembros(this.comunidadId, 0, 100).subscribe(data => this.miembros = data.content);
  }

  cargarHistorialMensajes(): void {
    this.comunidadService.historialMensajes(this.comunidadId, 0, 100).subscribe(data => {
      // API lo devuelve ordenado ASC (más antiguo al más nuevo)
      this.mensajes = data.content;
      setTimeout(() => this.scrollToBottom(), 100);
    });
  }

  conectarSSE(): void {
    this.sseSubscription = this.comunidadService.escucharNuevosMensajes(this.comunidadId)
      .subscribe({
        next: (msj) => {
          // Evitar duplicados porque el autor ya lo recibe del POST y luego del SSE.
          // Comprobar si ya existe por ID.
          const existe = this.mensajes.find(m => m.id === msj.id);
          if (!existe) {
            this.mensajes.push(msj);
          }
        },
        error: (err) => console.log('Reconectando SSE...') // Silencioso en front
      });
  }

  enviarMensaje(): void {
    if (!this.nuevoMensajeTexto.trim()) return;

    this.comunidadService.enviarMensaje(this.comunidadId, { contenido: this.nuevoMensajeTexto })
      .subscribe({
        next: (msjGuardado) => {
          this.nuevoMensajeTexto = '';
          const existe = this.mensajes.find(m => m.id === msjGuardado.id);
          if (!existe) {
            this.mensajes.push(msjGuardado);
          }
        },
        error: (err) => console.error('Error enviando msj', err)
      });
  }

  ngOnDestroy(): void {
    if (this.sseSubscription) {
      this.sseSubscription.unsubscribe();
    }
  }

  volver(): void {
    this.router.navigate(['/comunidades']);
  }

  irAlPerfil(usuarioId: number): void {
    this.router.navigate(['/usuario', usuarioId]);
  }
}

