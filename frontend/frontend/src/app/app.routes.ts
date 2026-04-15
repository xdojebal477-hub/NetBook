import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegistroComponent } from './features/auth/registro/registro.component';
import { PanelAutorComponent } from './features/autor/panel-autor/panel-autor.component';
import { CatalogoGeneralComponent } from './features/catalogo/catalogo-general/catalogo-general.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'panel-autor', component: PanelAutorComponent },
  { path: 'catalogo', component: CatalogoGeneralComponent },
  // De momento redirigimos a catalogo como portada principal
  { path: '', redirectTo: '/catalogo', pathMatch: 'full' }
];
