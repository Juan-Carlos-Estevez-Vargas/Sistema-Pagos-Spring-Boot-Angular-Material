import { Injectable } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';

@Injectable({
  providedIn: 'root',
})
export class LoadingService {
  
  private loadingCount = 0;

  constructor(private spinner: NgxSpinnerService) { }

  show(message?: string): void {
    this.loadingCount ++;

    if (this.loadingCount === 1) {
      this.spinner.show(undefined, {
        type: 'ball-scale-multiple',
        bdColor: 'rgba(0, 0, 0, 0.8)',
        color: '#fff',
        size: 'medium'
      });
    }
  }

  hide(): void {
    this.loadingCount = Math.max(0, this.loadingCount - 1);
    if (this.loadingCount === 0) {
      this.spinner.hide();
    }
  }

  reset(): void {
    this.loadingCount = 0;
    this.spinner.hide();
  }

}
