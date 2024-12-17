import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchedule, NewSchedule } from '../schedule.model';

export type PartialUpdateSchedule = Partial<ISchedule> & Pick<ISchedule, 'id'>;

type RestOf<T extends ISchedule | NewSchedule> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestSchedule = RestOf<ISchedule>;

export type NewRestSchedule = RestOf<NewSchedule>;

export type PartialUpdateRestSchedule = RestOf<PartialUpdateSchedule>;

export type EntityResponseType = HttpResponse<ISchedule>;
export type EntityArrayResponseType = HttpResponse<ISchedule[]>;

@Injectable({ providedIn: 'root' })
export class ScheduleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/schedules');

  create(schedule: NewSchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedule);
    return this.http
      .post<RestSchedule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(schedule: ISchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedule);
    return this.http
      .put<RestSchedule>(`${this.resourceUrl}/${this.getScheduleIdentifier(schedule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(schedule: PartialUpdateSchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedule);
    return this.http
      .patch<RestSchedule>(`${this.resourceUrl}/${this.getScheduleIdentifier(schedule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSchedule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getScheduleIdentifier(schedule: Pick<ISchedule, 'id'>): number {
    return schedule.id;
  }

  compareSchedule(o1: Pick<ISchedule, 'id'> | null, o2: Pick<ISchedule, 'id'> | null): boolean {
    return o1 && o2 ? this.getScheduleIdentifier(o1) === this.getScheduleIdentifier(o2) : o1 === o2;
  }

  addScheduleToCollectionIfMissing<Type extends Pick<ISchedule, 'id'>>(
    scheduleCollection: Type[],
    ...schedulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const schedules: Type[] = schedulesToCheck.filter(isPresent);
    if (schedules.length > 0) {
      const scheduleCollectionIdentifiers = scheduleCollection.map(scheduleItem => this.getScheduleIdentifier(scheduleItem));
      const schedulesToAdd = schedules.filter(scheduleItem => {
        const scheduleIdentifier = this.getScheduleIdentifier(scheduleItem);
        if (scheduleCollectionIdentifiers.includes(scheduleIdentifier)) {
          return false;
        }
        scheduleCollectionIdentifiers.push(scheduleIdentifier);
        return true;
      });
      return [...schedulesToAdd, ...scheduleCollection];
    }
    return scheduleCollection;
  }

  protected convertDateFromClient<T extends ISchedule | NewSchedule | PartialUpdateSchedule>(schedule: T): RestOf<T> {
    return {
      ...schedule,
      startTime: schedule.startTime?.toJSON() ?? null,
      endTime: schedule.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSchedule: RestSchedule): ISchedule {
    return {
      ...restSchedule,
      startTime: restSchedule.startTime ? dayjs(restSchedule.startTime) : undefined,
      endTime: restSchedule.endTime ? dayjs(restSchedule.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSchedule>): HttpResponse<ISchedule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSchedule[]>): HttpResponse<ISchedule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
