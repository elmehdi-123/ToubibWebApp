import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ToubibRdvWebAppSharedModule } from 'app/shared/shared.module';
import { AppointmentComponent } from './appointmentPatient.component';
import { AppointmentDetailComponent } from './appointmentPatient-detail.component';
import { AppointmentUpdateComponent } from './appointmentPatient-update.component';
import { AppointmentDeleteDialogComponent } from './appointmentPatient-delete-dialog.component';
import { appointmentRoute } from './appointmentPatient.route';
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
