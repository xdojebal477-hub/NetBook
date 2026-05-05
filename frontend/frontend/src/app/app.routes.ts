import { Routes } from '@angular/router';  
import { LoginComponent } from './features/auth/login/login.component';  
import { RegistroComponent } from './features/auth/registro/registro.component';  
import { PanelAutorComponent } from './features/autor/panel-autor/panel-autor.component';  
import { PanelAdminComponent } from './features/admin/panel-admin/panel-admin.component';
import { CatalogoGeneralComponent } from './features/catalogo/catalogo-general/catalogo-general.component';  
import { HomeComponent } from './features/home/home.component';
import { LibroDetalleComponent } from './features/catalogo/libro-detalle/libro-detalle.component';
import { VisorLecturaComponent } from './features/catalogo/visor-lectura/visor-lectura.component';
import { MisColeccionesComponent } from './features/colecciones/mis-colecciones/mis-colecciones.component';  
export const routes: Routes = [  
  { path: '', component: HomeComponent, pathMatch: 'full' },  
  { path: 'login', component: LoginComponent },  
  { path: 'registro', component: RegistroComponent },  
  { path: 'panel-autor', component: PanelAutorComponent },  
  { path: 'catalogo', component: CatalogoGeneralComponent },
  { path: 'libro/:id', component: LibroDetalleComponent },
  { path: 'visor/:id', component: VisorLecturaComponent },
  { path: 'colecciones', component: MisColeccionesComponent },  
  { path: 'panel-admin', component: PanelAdminComponent },
  { path: '**', redirectTo: '' }  
]; 
