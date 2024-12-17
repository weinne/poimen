import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WorshipEventDetailComponent } from './worship-event-detail.component';

describe('WorshipEvent Management Detail Component', () => {
  let comp: WorshipEventDetailComponent;
  let fixture: ComponentFixture<WorshipEventDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorshipEventDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./worship-event-detail.component').then(m => m.WorshipEventDetailComponent),
              resolve: { worshipEvent: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WorshipEventDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorshipEventDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load worshipEvent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WorshipEventDetailComponent);

      // THEN
      expect(instance.worshipEvent()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
