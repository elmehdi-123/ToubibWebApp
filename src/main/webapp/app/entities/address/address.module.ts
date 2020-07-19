import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ToubibRdvWebAppSharedModule } from 'app/shared/shared.module';
import { AddressComponent } from './address.component';
import { AddressDetailComponent } from './address-detail.component';
import { AddressUpdateComponent } from './address-update.component';
import { AddressDeleteDialogComponent } from './address-delete-dialog.component';
import { addressRoute } from './address.route';
import { AddressSectionComponent } from './address-section.component';

@NgModule({
  imports: [ToubibRdvWebAppSharedModule, RouterModule.forChild(addressRoute)],
  declarations: [AddressComponent, AddressDetailComponent, AddressUpdateComponent, AddressDeleteDialogComponent,AddressSectionComponent],
  entryComponents: [AddressDeleteDialogComponent]
})
export class ToubibRdvWebAppAddressModule {}
