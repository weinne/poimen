import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMinistryMembership, NewMinistryMembership } from '../ministry-membership.model';

export type PartialUpdateMinistryMembership = Partial<IMinistryMembership> & Pick<IMinistryMembership, 'id'>;

type RestOf<T extends IMinistryMembership | NewMinistryMembership> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestMinistryMembership = RestOf<IMinistryMembership>;

export type NewRestMinistryMembership = RestOf<NewMinistryMembership>;

export type PartialUpdateRestMinistryMembership = RestOf<PartialUpdateMinistryMembership>;

export type EntityResponseType = HttpResponse<IMinistryMembership>;
export type EntityArrayResponseType = HttpResponse<IMinistryMembership[]>;

@Injectable({ providedIn: 'root' })
export class MinistryMembershipService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ministry-memberships');

  create(ministryMembership: NewMinistryMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryMembership);
    return this.http
      .post<RestMinistryMembership>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ministryMembership: IMinistryMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryMembership);
    return this.http
      .put<RestMinistryMembership>(`${this.resourceUrl}/${this.getMinistryMembershipIdentifier(ministryMembership)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ministryMembership: PartialUpdateMinistryMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryMembership);
    return this.http
      .patch<RestMinistryMembership>(`${this.resourceUrl}/${this.getMinistryMembershipIdentifier(ministryMembership)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMinistryMembership>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMinistryMembership[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMinistryMembershipIdentifier(ministryMembership: Pick<IMinistryMembership, 'id'>): number {
    return ministryMembership.id;
  }

  compareMinistryMembership(o1: Pick<IMinistryMembership, 'id'> | null, o2: Pick<IMinistryMembership, 'id'> | null): boolean {
    return o1 && o2 ? this.getMinistryMembershipIdentifier(o1) === this.getMinistryMembershipIdentifier(o2) : o1 === o2;
  }

  addMinistryMembershipToCollectionIfMissing<Type extends Pick<IMinistryMembership, 'id'>>(
    ministryMembershipCollection: Type[],
    ...ministryMembershipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ministryMemberships: Type[] = ministryMembershipsToCheck.filter(isPresent);
    if (ministryMemberships.length > 0) {
      const ministryMembershipCollectionIdentifiers = ministryMembershipCollection.map(ministryMembershipItem =>
        this.getMinistryMembershipIdentifier(ministryMembershipItem),
      );
      const ministryMembershipsToAdd = ministryMemberships.filter(ministryMembershipItem => {
        const ministryMembershipIdentifier = this.getMinistryMembershipIdentifier(ministryMembershipItem);
        if (ministryMembershipCollectionIdentifiers.includes(ministryMembershipIdentifier)) {
          return false;
        }
        ministryMembershipCollectionIdentifiers.push(ministryMembershipIdentifier);
        return true;
      });
      return [...ministryMembershipsToAdd, ...ministryMembershipCollection];
    }
    return ministryMembershipCollection;
  }

  protected convertDateFromClient<T extends IMinistryMembership | NewMinistryMembership | PartialUpdateMinistryMembership>(
    ministryMembership: T,
  ): RestOf<T> {
    return {
      ...ministryMembership,
      startDate: ministryMembership.startDate?.toJSON() ?? null,
      endDate: ministryMembership.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMinistryMembership: RestMinistryMembership): IMinistryMembership {
    return {
      ...restMinistryMembership,
      startDate: restMinistryMembership.startDate ? dayjs(restMinistryMembership.startDate) : undefined,
      endDate: restMinistryMembership.endDate ? dayjs(restMinistryMembership.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMinistryMembership>): HttpResponse<IMinistryMembership> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMinistryMembership[]>): HttpResponse<IMinistryMembership[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
