import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { SpecialtyService } from 'app/entities/specialty/specialty.service';

type SelectableEntity = IUser | ISpecialty;

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  specialties: ISpecialty[] = [];
  dateDeNaissanceDp: any;

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    numTele: [],
    eMail: [],
    dateDeNaissance: [],
    civilite: [],
    docteurOrPatient: [],
    userId: [],
    specialties: []
  });

  constructor(
    protected personService: PersonService,
    protected userService: UserService,
    protected specialtyService: SpecialtyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.specialtyService.query().subscribe((res: HttpResponse<ISpecialty[]>) => (this.specialties = res.body || []));
    });
  }

  updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      nom: person.nom,
      prenom: person.prenom,
      numTele: person.numTele,
      eMail: person.eMail,
      dateDeNaissance: person.dateDeNaissance,
      civilite: person.civilite,
      docteurOrPatient: person.docteurOrPatient,
      userId: person.userId,
      specialties: person.specialties
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      numTele: this.editForm.get(['numTele'])!.value,
      eMail: this.editForm.get(['eMail'])!.value,
      dateDeNaissance: this.editForm.get(['dateDeNaissance'])!.value,
      civilite: this.editForm.get(['civilite'])!.value,
      docteurOrPatient: this.editForm.get(['docteurOrPatient'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      specialties: this.editForm.get(['specialties'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: ISpecialty[], option: ISpecialty): ISpecialty {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
