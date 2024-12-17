import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ChurchService } from '../service/church.service';
import { IChurch } from '../church.model';
import { ChurchFormService } from './church-form.service';

import { ChurchUpdateComponent } from './church-update.component';

describe('Church Management Update Component', () => {
  let comp: ChurchUpdateComponent;
  let fixture: ComponentFixture<ChurchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let churchFormService: ChurchFormService;
  let churchService: ChurchService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChurchUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ChurchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChurchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    churchFormService = TestBed.inject(ChurchFormService);
    churchService = TestBed.inject(ChurchService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const church: IChurch = { id: 456 };
      const users: IUser[] = [{ id: 30816 }];
      church.users = users;

      const userCollection: IUser[] = [{ id: 1461 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...users];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ church });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const church: IChurch = { id: 456 };
      const user: IUser = { id: 9182 };
      church.users = [user];

      activatedRoute.data = of({ church });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.church).toEqual(church);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchFormService, 'getChurch').mockReturnValue(church);
      jest.spyOn(churchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: church }));
      saveSubject.complete();

      // THEN
      expect(churchFormService.getChurch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(churchService.update).toHaveBeenCalledWith(expect.objectContaining(church));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchFormService, 'getChurch').mockReturnValue({ id: null });
      jest.spyOn(churchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: church }));
      saveSubject.complete();

      // THEN
      expect(churchFormService.getChurch).toHaveBeenCalled();
      expect(churchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(churchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
