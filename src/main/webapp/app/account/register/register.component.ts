import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { RegisterService } from './register.service';
import { DocteurOrPatientEnum } from 'app/shared/model/enumerations/docteur-or-patient-enum.model';
import { Person } from 'app/shared/model/person.model';
import { Address } from 'app/shared/model/address.model';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    nom:  ['', [Validators.required]],
    prenom:  ['', [Validators.required]],
    numTele:  ['', [Validators.required, Validators.minLength(4), Validators.maxLength(15)]],
    dateDeNaissance: ['', [Validators.required]],
    civilite: ['', [Validators.required]],
    nomRue: ['', [Validators.required]],
    ville: ['', [Validators.required]],
    commun: ['', [Validators.required]],
    codePostal: ['', [Validators.required]],
    willaya: ['', [Validators.required]],
  });

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: RegisterService,
    private fb: FormBuilder
  ) {}

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
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
      const address = {...new Address(),
        nomRue: this.registerForm.get(['nomRue'])!.value,
        ville: this.registerForm.get(['ville'])!.value,
        commun: this.registerForm.get(['commun'])!.value,
        codePostal: this.registerForm.get(['codePostal'])!.value,
        willaya: this.registerForm.get(['willaya'])!.value,
      }
      const person = {...new Person(),
        nom: this.registerForm.get(['nom'])!.value,
        prenom: this.registerForm.get(['prenom'])!.value,
        numTele: this.registerForm.get(['numTele'])!.value,
        eMail: this.registerForm.get(['email'])!.value,
        dateDeNaissance: this.registerForm.get(['dateDeNaissance'])!.value,
        civilite: this.registerForm.get(['civilite'])!.value,
        docteurOrPatient: DocteurOrPatientEnum.PATIENT,
        addresses: [address]
      };
      this.registerService.save({ login, email, password, langKey: this.languageService.getCurrentLanguage(), person}).subscribe(
        () => (this.success = true),
        response => this.processError(response)
      );
    }
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
