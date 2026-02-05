import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { Rol } from '../../core/models/auth.model';

const routes: Routes = [
  {
    path: '',
    component: EstudiantesListComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [ Rol.ADMIN, Rol.FINANZAS, Rol.PROFESOR ]}
  },
  {
    path: 'nuevo',
    component: EstudianteFormComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN]}
  },
  {
    path: ':id',
    component: EstudianteDetailComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN, Rol.FINANZAS, Rol.PROFESOR]}
  },
  {
    path: ':id/editar',
    component: EstudianteFormComponent,
    canActivate: [ AuthGuard, RoleGuard ],
    data: { roles: [Rol.ADMIN]}
  }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [ RouterModule ]
})
export class EstudiantesRoutingModule { }
