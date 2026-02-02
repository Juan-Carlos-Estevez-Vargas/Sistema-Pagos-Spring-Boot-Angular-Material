import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  
  constructor(private toastr: ToastrService) { }

  showSuccess(message: string, title: string = 'Éxito'): void {
    this.toastr.success(message, title, {
      positionClass: 'toast-top-right',
      timeOut: 3000
    });
  }

  showError(message: string, title: string = 'Error'): void {
    this.toastr.error(message, title, {
      positionClass: 'toast-top-right',
      timeOut: 5000
    });
  }

  showWarning(message: string, title: string = 'Advertencia'): void {
    this.toastr.warning(message, title, {
      positionClass: 'toast-top-right',
      timeOut: 4000
    });
  }

  showInfo(message: string, title: string = 'Información'): void {
    this.toastr.info(message, title, {
      positionClass: 'toast-top-right',
      timeOut: 3000
    });
  }

}
