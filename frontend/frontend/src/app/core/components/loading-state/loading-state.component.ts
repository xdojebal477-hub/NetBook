import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div [ngClass]="overlay ? 'fixed inset-0 z-50 bg-gray-50/90 backdrop-blur-sm px-4' : containerClass" class="flex items-center justify-center">
      <div class="flex flex-col items-center justify-center text-center">
        <div class="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent mb-4"></div>
        <p class="text-gray-600 font-medium animate-pulse">{{ message }}</p>
        <p *ngIf="subtitle" class="text-sm text-gray-400 mt-1 max-w-md">{{ subtitle }}</p>
      </div>
    </div>
  `
})
export class LoadingStateComponent {
  @Input() message = 'Cargando...';
  @Input() subtitle = '';
  @Input() overlay = false;
  @Input() containerClass = 'py-20';
}
