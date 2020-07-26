import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppointment, Appointment } from 'app/shared/model/appointment.model';
import { AppointmentService } from './appointment.service';
import { AppointmentComponent } from './appointment.component';
import { AppointmentDetailComponent } from './appointment-detail.component';
import { AppointmentUpdateComponent } from './appointment-update.component';

@Injectable({ providedIn: 'root' })
export class AppointmentResolve implements Resolve<IAppointment> {
  constructor(private service: AppointmentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appointment: HttpResponse<Appointment>) => {
          if (appointment.body) {
            return of(appointment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appointment());
  }
}

export const appointmentRoute: Routes = [
  {
    path: '',
    component: AppointmentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER, Authority.DOCTEUR, Authority.PATIENT],
      defaultSort: 'id,asc',
      pageTitle: 'toubibRdvWebApp.appointment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AppointmentDetailComponent,
    resolve: {
      appointment: AppointmentResolve
    },
    data: {
      authorities: [Authority.USER, Authority.DOCTEUR, Authority.PATIENT],
      pageTitle: 'toubibRdvWebApp.appointment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AppointmentUpdateComponent,
    resolve: {
      appointment: AppointmentResolve
    },
    data: {
      authorities: [Authority.USER, Authority.DOCTEUR, Authority.PATIENT],
      pageTitle: 'toubibRdvWebApp.appointment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AppointmentUpdateComponent,
    resolve: {
      appointment: AppointmentResolve
    },
    data: {
      authorities: [Authority.USER, Authority.DOCTEUR, Authority.PATIENT],
      pageTitle: 'toubibRdvWebApp.appointment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
