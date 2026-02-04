import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'warning' | 'danger' | 'info';
}

@Component({
  selector: 'app-confirm-dialog.component',
  imports: [],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss',
})
export class ConfirmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {
    if (!data.confirmText) data.confirmText = 'Confirmar';
    if (!data.cancelText) data.cancelText = 'Cancelar';
    if (!data.type) data.type = 'warning';
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  get iconClass(): string {
    switch (this.data.type) {
      case 'danger': return 'fas fa-exclamation-triangle text-danger';
      case 'info': return 'fas fa-info-circle text-info';
      default: return 'fas fa-exclamation-circle text-warning';
    }
  }

}
