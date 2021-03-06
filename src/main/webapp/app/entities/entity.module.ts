import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'address',
        loadChildren: () => import('./address/address.module').then(m => m.ToubibRdvWebAppAddressModule)
      },
      {
        path: 'person',
        loadChildren: () => import('./person/person.module').then(m => m.ToubibRdvWebAppPersonModule)
      },
      {
        path: 'specialty',
        loadChildren: () => import('./specialty/specialty.module').then(m => m.ToubibRdvWebAppSpecialtyModule)
      },
      {
        path: 'appointment',
        loadChildren: () => import('./appointment/appointment.module').then(m => m.ToubibRdvWebAppAppointmentModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ToubibRdvWebAppEntityModule {}
