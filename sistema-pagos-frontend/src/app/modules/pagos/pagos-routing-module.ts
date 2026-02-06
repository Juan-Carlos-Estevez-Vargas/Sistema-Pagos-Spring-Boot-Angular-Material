import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { Rol } from '../../core/models/auth.model';
import { PagoFormComponent } from './components/pago-form/pago-form.component';
import { authGuard } from '../../core/guards/auth.guard';

const routes: Routes = [
  {
    path: "",
    component: PagosListComponent,
    canActivate: [ authGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.ESTUDIANTE, Rol.FINANZAS]}
  },
  {
    path: "nuevo",
    component: PagoFormComponent,
    canActivate: [ authGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.FINANZAS, Rol.ESTUDIANTE]}
  },
  {
    path: ":id",
    component: PagoDetailComponent,
    canActivate: [ authGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.ESTUDIANTE, Rol.FINANZAS]}
  }
]


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [ RouterModule ]
})
export class PagosRoutingModule { }
