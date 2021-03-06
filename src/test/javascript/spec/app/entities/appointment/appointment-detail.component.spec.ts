import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ToubibRdvWebAppTestModule } from '../../../test.module';
import { AppointmentDetailComponent } from 'app/entities/appointment/appointment-detail.component';
import { Appointment } from 'app/shared/model/appointment.model';

describe('Component Tests', () => {
  describe('Appointment Management Detail Component', () => {
    let comp: AppointmentDetailComponent;
    let fixture: ComponentFixture<AppointmentDetailComponent>;
    const route = ({ data: of({ appointment: new Appointment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ToubibRdvWebAppTestModule],
        declarations: [AppointmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AppointmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppointmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appointment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appointment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
