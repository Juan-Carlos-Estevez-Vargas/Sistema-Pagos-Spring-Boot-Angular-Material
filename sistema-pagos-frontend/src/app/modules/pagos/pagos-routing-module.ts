import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { Rol } from '../../core/models/auth.model';

const routes: Routes = [
  {
    path: "",
    component: PagosListComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.ESTUDIANTE, Rol.FINANZAS]}
  },
  {
    path: "nuevo",
    component: PagoFormComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.FINANZAS, Rol.ESTUDIANTE]}
  },
  {
    path: ":id",
    component: PagoDetailComponent,
    canActivate: [ AuthGuard, RoleGuard ],
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
