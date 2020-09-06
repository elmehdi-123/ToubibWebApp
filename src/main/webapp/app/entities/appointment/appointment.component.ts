import { Component, OnInit, OnDestroy } from '@angular/core';
// import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointment } from 'app/shared/model/appointment.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AppointmentService } from './appointment.service';
import { AppointmentDeleteDialogComponent } from './appointment-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { DocteurOrPatientEnum } from 'app/shared/model/enumerations/docteur-or-patient-enum.model';
import { DatePipe } from '@angular/common';
import * as moment from 'moment';

@Component({
  selector: 'jhi-appointment',
  templateUrl: './appointment.component.html'
})
export class AppointmentComponent implements OnInit, OnDestroy {
  datePipe : DatePipe = new DatePipe('fr');
  appointments?: IAppointment[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  account: any;

  constructor(
    protected appointmentService: AppointmentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private accountService: AccountService,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    // To get ALL appointements 
    /* this.appointmentService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IAppointment[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      ); */

      if(this.account.person.docteurOrPatient === DocteurOrPatientEnum.DOCTEUR)
      this.onSuccess(this.account.person.disponibilties, pageToLoad);
      if(this.account.person.docteurOrPatient === DocteurOrPatientEnum.PATIENT)
      this.onSuccess(this.account.person.appointments, pageToLoad); 
     }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInAppointments();
    console.log(this.account)
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppointment): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAppointments(): void {
    this.eventSubscriber = this.eventManager.subscribe('appointmentListModification', () => this.loadPage());
  }

  delete(appointment: IAppointment): void {
    const modalRef = this.modalService.open(AppointmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appointment = appointment;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAppointment[] | null,page: number): void {
    this.totalItems = Number(data?.length);
    this.page = page;
    this.router.navigate(['/appointment'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.appointments = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }

  annulerRdv(appointment: any){
    appointment.personId = undefined;
    appointment.dateRdv = moment(appointment.dateRdv).utcOffset('+0000');
    appointment.docteurId = appointment.docteur.id;
    this.appointmentService.update(appointment).subscribe(res => console.log(res) );
    location.reload();
  }
}
