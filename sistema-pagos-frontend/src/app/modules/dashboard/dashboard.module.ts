import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { SharedModule } from 'primeng/api';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ChartModule,
    CardModule,
    SharedModule,
    DashboardComponent
  ]
})
export class DashboardModule { }
