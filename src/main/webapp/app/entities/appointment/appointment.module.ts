import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ToubibRdvWebAppSharedModule } from 'app/shared/shared.module';
import { AppointmentComponent } from './appointment.component';
import { AppointmentDetailComponent } from './appointment-detail.component';
import { AppointmentUpdateComponent } from './appointment-update.component';
import { AppointmentDeleteDialogComponent } from './appointment-delete-dialog.component';
import { appointmentRoute } from './appointment.route';
import { OwlDateTimeModule, OwlNativeDateTimeModule, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';


@NgModule({
  imports: [ToubibRdvWebAppSharedModule, RouterModule.forChild(appointmentRoute),  OwlDateTimeModule, 
    OwlNativeDateTimeModule],
  declarations: [AppointmentComponent, AppointmentDetailComponent, AppointmentUpdateComponent, AppointmentDeleteDialogComponent],
  entryComponents: [AppointmentDeleteDialogComponent],
  providers: [
    // use french locale
    {provide: OWL_DATE_TIME_LOCALE, useValue: 'fr'},
  ]
})
export class ToubibRdvWebAppAppointmentModule {}
