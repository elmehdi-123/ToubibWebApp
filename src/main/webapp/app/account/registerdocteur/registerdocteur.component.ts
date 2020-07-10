import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { RegisterService } from './registerdocteur.service';
import { Person } from 'app/shared/model/person.model';
import { SpecialtyService } from 'app/entities/specialty/specialty.service';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { IUser } from 'app/core/user/user.model';

type SelectableEntity = IUser | ISpecialty;

@Component({
  selector: 'jhi-registerdocteur',
  templateUrl: './registerdocteur.component.html'
})
export class RegisterDocteurComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  specialties: ISpecialty[] = [];


  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    id: [],
    nom: [],
    prenom: [],
    numTele: [],
    eMail: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    dateDeNaissance: [],
    civilite: [],
    docteurOrPatient: [],
    userId: [],
    specialties: []
  });

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    protected specialtyService: SpecialtyService,
    private registerService: RegisterService,
    private fb: FormBuilder
  ) {}

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
    this.specialtyService.query().subscribe((res: HttpResponse<ISpecialty[]>) => (this.specialties = res.body || []));

  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const login = this.registerForm.get(['login'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      const person = {...new Person(),
        nom: this.registerForm.get(['nom'])!.value,
        prenom: this.registerForm.get(['prenom'])!.value,
        numTele: this.registerForm.get(['numTele'])!.value,
        eMail: this.registerForm.get(['eMail'])!.value,
        dateDeNaissance: this.registerForm.get(['dateDeNaissance'])!.value,
        civilite: this.registerForm.get(['civilite'])!.value,
        docteurOrPatient: this.registerForm.get(['docteurOrPatient'])!.value,
        specialties: this.registerForm.get(['specialties'])!.value,
      };
      

      
      this.registerService.save({ login, email, password, langKey: this.languageService.getCurrentLanguage(),person }).subscribe(
        () => (this.success = true),
        response => this.processError(response)
      );
    }
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

  openLogin(): void {
    this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }
}


