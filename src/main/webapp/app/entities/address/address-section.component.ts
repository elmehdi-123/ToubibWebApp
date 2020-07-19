import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'jhi-address-section',
  templateUrl: './address-section.component.html',
  styleUrls: ['./address-section.component.scss'],
})
export class AddressSectionComponent {
  @Input('group')
  public addressForm!: FormGroup;
  @Input()
  public index!: number;
  @Input()
  public isMultiple!: boolean;
  @Input()
  public disableButton!: boolean;
  @Output()
  public removeAtIndex = new EventEmitter<number>();

  public sendPrincipalHabitationCheckBox(event : any) {
    if (event && !event.value) {
      this.resetAddressFields();
    }
  }

  resetAddressFields() {
    this.addressForm.patchValue({
      nomRue: undefined,
      ville: undefined,
      commun: undefined,
      codePostal: undefined,
      willaya: undefined
    });
  }

  public sendIndexOfRemovedAddress() {
    this.removeAtIndex.emit(this.index);
  }
}
