import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MinistryMembershipDetailComponent } from './ministry-membership-detail.component';

describe('MinistryMembership Management Detail Component', () => {
  let comp: MinistryMembershipDetailComponent;
  let fixture: ComponentFixture<MinistryMembershipDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MinistryMembershipDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ministry-membership-detail.component').then(m => m.MinistryMembershipDetailComponent),
              resolve: { ministryMembership: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MinistryMembershipDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinistryMembershipDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ministryMembership on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MinistryMembershipDetailComponent);

      // THEN
      expect(instance.ministryMembership()).toEqual(expect.objectContaining({ id: 123 }));
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
