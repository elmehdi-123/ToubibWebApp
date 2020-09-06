import { Component, OnInit, OnDestroy, LOCALE_ID } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { SpecialtyService } from 'app/entities/specialty/specialty.service';
import { HttpResponse } from '@angular/common/http';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { Validators, FormBuilder } from '@angular/forms';
import { IUser } from 'app/core/user/user.model';
import { IAppointment } from 'app/shared/model/appointment.model';
import { AppointmentDeleteDialogComponent } from 'app/entities/appointment/appointment-delete-dialog.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AppointmentService } from 'app/entities/appointment/appointment.service';
import { LoginModalComponent } from 'app/shared/login/login.component';
import * as moment from 'moment';
import localeFr from '@angular/common/locales/fr';
import { registerLocaleData } from '@angular/common';
import { DatePipe } from '@angular/common';
registerLocaleData(localeFr);

type SelectableEntity = IUser | ISpecialty;

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
  providers: [
    { provide: LOCALE_ID, useValue: 'fr-FR'},
  ]
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  specialties: ISpecialty[] = [];
  appointments: IAppointment[] = [];
  predicate!: string;
  ascending!: boolean;
  datePipe: DatePipe = new DatePipe('fr-FR');


  homeForm = this.fb.group({
    specialties: ['', [Validators.required]]
  
  });


  constructor(private accountService: AccountService, private loginModalService: LoginModalService,
    protected specialtyService: SpecialtyService, private fb: FormBuilder,     protected modalService: NgbModal,
    protected appointmentService: AppointmentService

    ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.specialtyService.query().subscribe((res: HttpResponse<ISpecialty[]>) => (this.specialties = res.body || []));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  getSelected(selectedVal: ISpecialty, option: ISpecialty): ISpecialty {

    if (selectedVal && selectedVal.people) {
      for (let i = 0; i < selectedVal.people.length; i++) {
        console.log(selectedVal.people[i])
      this.appointments?.push.apply(selectedVal.people[i].appointments);
      }
    }
    return option;
  }

  changeSpeciality(){

    for (let i = 0; i < this.homeForm.get('specialties')!.value.people.length; i++) {
      for (let j = 0; j < this.homeForm.get('specialties')!.value.people[i].disponibilties.length; j++) {
        if(this.homeForm.get('specialties')!.value.people[i].disponibilties[j].person == null)
        this.appointments.push(this.homeForm.get('specialties')!.value.people[i].disponibilties[j]);
      }
    }

  
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  trackId(index: number, item: IAppointment): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
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

  reserverRdv(appointment:any){

    if(!this.isAuthenticated()){
    const modalRef: NgbModalRef = this.modalService.open(LoginModalComponent);
    modalRef.result.finally(() =>     console.log(this.account ));
    }else{
     appointment.personId = this.account?.person.id;
     console.log(appointment.dateRdv)
     appointment.dateRdv = moment(appointment.dateRdv).utcOffset('+0000');
     console.log(appointment.dateRdv)

     appointment.docteurId = appointment.docteur.id;
     this.appointmentService.update(appointment).subscribe(res => console.log(res) );
     location.reload();
    }
  }
  
}
