
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAppointment, Appointment } from 'app/shared/model/appointment.model';
import { AppointmentService } from './appointment.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person/person.service';
import * as moment from 'moment';
import { AccountService } from 'app/core/auth/account.service';
import localeFr from '@angular/common/locales/fr';
import { registerLocaleData } from '@angular/common';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-appointment-update',
  templateUrl: './appointment-update.component.html'
})
export class AppointmentUpdateComponent implements OnInit {
  isSaving = false;
  people: IPerson[] = [];
  dateRdvDp: any;
  beginTime: any = { hour: 0, minute: 15};
  endTime : any = {hour: 0, minute: 15};
  account: any;
  datePipe: DatePipe = new DatePipe('en-US');


  editForm = this.fb.group({
    id: [],
    motif: [],
    dateRdv: [],
    personId: [],
    docteurId: [],
    patient:[]
  });

  constructor(
    protected appointmentService: AppointmentService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.activatedRoute.data.subscribe(({ appointment }) => {
      this.updateForm(appointment);

      this.personService.query().subscribe((res: HttpResponse<IPerson[]>) => (this.people = res.body || []));
    });
  }

  updateForm(appointment: IAppointment): void {
    console.log(appointment.dateRdv ? this.datePipe.transform(appointment.dateRdv, 'short') : '')
    this.editForm.patchValue({
      id: appointment.id,
      motif: appointment.motif,
      dateRdv: appointment.dateRdv ? this.datePipe.transform(appointment.dateRdv, 'short') : '',
      personId: appointment.personId,
      docteurId: appointment.docteurId ? appointment.docteurId : this.account.person.id,
      patient: appointment.patient != null ? appointment.patient?.nom +" "+  appointment.patient?.prenom : null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointment = this.createFromForm();
    if (appointment.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentService.update(appointment));
    } else {
      this.subscribeToSaveResponse(this.appointmentService.create(appointment));
    }
  }

  private createFromForm(): IAppointment {
    return {
      ...new Appointment(),
      id: this.editForm.get(['id'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      dateRdv: moment(this.editForm.get(['dateRdv'])!.value),
      personId: this.editForm.get(['personId'])!.value,
      docteurId: this.editForm.get(['docteurId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPerson): any {
    return item.id;
  }
}
