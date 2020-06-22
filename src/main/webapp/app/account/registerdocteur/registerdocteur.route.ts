import { Route } from '@angular/router';

import { RegisterDocteurComponent } from './registerdocteur.component';

export const registerDocteurRoute: Route = {
  path: 'registerdocteur',
  component: RegisterDocteurComponent,
  data: {
    authorities: [],
    pageTitle: 'register.title'
  }
};
