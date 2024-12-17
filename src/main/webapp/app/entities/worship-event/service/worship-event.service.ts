import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorshipEvent, NewWorshipEvent } from '../worship-event.model';

export type PartialUpdateWorshipEvent = Partial<IWorshipEvent> & Pick<IWorshipEvent, 'id'>;

type RestOf<T extends IWorshipEvent | NewWorshipEvent> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestWorshipEvent = RestOf<IWorshipEvent>;

export type NewRestWorshipEvent = RestOf<NewWorshipEvent>;

export type PartialUpdateRestWorshipEvent = RestOf<PartialUpdateWorshipEvent>;

export type EntityResponseType = HttpResponse<IWorshipEvent>;
export type EntityArrayResponseType = HttpResponse<IWorshipEvent[]>;

@Injectable({ providedIn: 'root' })
export class WorshipEventService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/worship-events');

  create(worshipEvent: NewWorshipEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worshipEvent);
    return this.http
      .post<RestWorshipEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(worshipEvent: IWorshipEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worshipEvent);
    return this.http
      .put<RestWorshipEvent>(`${this.resourceUrl}/${this.getWorshipEventIdentifier(worshipEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(worshipEvent: PartialUpdateWorshipEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worshipEvent);
    return this.http
      .patch<RestWorshipEvent>(`${this.resourceUrl}/${this.getWorshipEventIdentifier(worshipEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorshipEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorshipEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorshipEventIdentifier(worshipEvent: Pick<IWorshipEvent, 'id'>): number {
    return worshipEvent.id;
  }

  compareWorshipEvent(o1: Pick<IWorshipEvent, 'id'> | null, o2: Pick<IWorshipEvent, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorshipEventIdentifier(o1) === this.getWorshipEventIdentifier(o2) : o1 === o2;
  }

  addWorshipEventToCollectionIfMissing<Type extends Pick<IWorshipEvent, 'id'>>(
    worshipEventCollection: Type[],
    ...worshipEventsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const worshipEvents: Type[] = worshipEventsToCheck.filter(isPresent);
    if (worshipEvents.length > 0) {
      const worshipEventCollectionIdentifiers = worshipEventCollection.map(worshipEventItem =>
        this.getWorshipEventIdentifier(worshipEventItem),
      );
      const worshipEventsToAdd = worshipEvents.filter(worshipEventItem => {
        const worshipEventIdentifier = this.getWorshipEventIdentifier(worshipEventItem);
        if (worshipEventCollectionIdentifiers.includes(worshipEventIdentifier)) {
          return false;
        }
        worshipEventCollectionIdentifiers.push(worshipEventIdentifier);
        return true;
      });
      return [...worshipEventsToAdd, ...worshipEventCollection];
    }
    return worshipEventCollection;
  }

  protected convertDateFromClient<T extends IWorshipEvent | NewWorshipEvent | PartialUpdateWorshipEvent>(worshipEvent: T): RestOf<T> {
    return {
      ...worshipEvent,
      date: worshipEvent.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorshipEvent: RestWorshipEvent): IWorshipEvent {
    return {
      ...restWorshipEvent,
      date: restWorshipEvent.date ? dayjs(restWorshipEvent.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorshipEvent>): HttpResponse<IWorshipEvent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorshipEvent[]>): HttpResponse<IWorshipEvent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
