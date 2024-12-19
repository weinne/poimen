import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMinistryGroup, NewMinistryGroup } from '../ministry-group.model';

export type PartialUpdateMinistryGroup = Partial<IMinistryGroup> & Pick<IMinistryGroup, 'id'>;

type RestOf<T extends IMinistryGroup | NewMinistryGroup> = Omit<T, 'establishedDate'> & {
  establishedDate?: string | null;
};

export type RestMinistryGroup = RestOf<IMinistryGroup>;

export type NewRestMinistryGroup = RestOf<NewMinistryGroup>;

export type PartialUpdateRestMinistryGroup = RestOf<PartialUpdateMinistryGroup>;

export type EntityResponseType = HttpResponse<IMinistryGroup>;
export type EntityArrayResponseType = HttpResponse<IMinistryGroup[]>;

@Injectable({ providedIn: 'root' })
export class MinistryGroupService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ministry-groups');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ministry-groups/_search');

  create(ministryGroup: NewMinistryGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryGroup);
    return this.http
      .post<RestMinistryGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ministryGroup: IMinistryGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryGroup);
    return this.http
      .put<RestMinistryGroup>(`${this.resourceUrl}/${this.getMinistryGroupIdentifier(ministryGroup)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ministryGroup: PartialUpdateMinistryGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministryGroup);
    return this.http
      .patch<RestMinistryGroup>(`${this.resourceUrl}/${this.getMinistryGroupIdentifier(ministryGroup)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMinistryGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMinistryGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMinistryGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMinistryGroup[]>()], asapScheduler)),
    );
  }

  getMinistryGroupIdentifier(ministryGroup: Pick<IMinistryGroup, 'id'>): number {
    return ministryGroup.id;
  }

  compareMinistryGroup(o1: Pick<IMinistryGroup, 'id'> | null, o2: Pick<IMinistryGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getMinistryGroupIdentifier(o1) === this.getMinistryGroupIdentifier(o2) : o1 === o2;
  }

  addMinistryGroupToCollectionIfMissing<Type extends Pick<IMinistryGroup, 'id'>>(
    ministryGroupCollection: Type[],
    ...ministryGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ministryGroups: Type[] = ministryGroupsToCheck.filter(isPresent);
    if (ministryGroups.length > 0) {
      const ministryGroupCollectionIdentifiers = ministryGroupCollection.map(ministryGroupItem =>
        this.getMinistryGroupIdentifier(ministryGroupItem),
      );
      const ministryGroupsToAdd = ministryGroups.filter(ministryGroupItem => {
        const ministryGroupIdentifier = this.getMinistryGroupIdentifier(ministryGroupItem);
        if (ministryGroupCollectionIdentifiers.includes(ministryGroupIdentifier)) {
          return false;
        }
        ministryGroupCollectionIdentifiers.push(ministryGroupIdentifier);
        return true;
      });
      return [...ministryGroupsToAdd, ...ministryGroupCollection];
    }
    return ministryGroupCollection;
  }

  protected convertDateFromClient<T extends IMinistryGroup | NewMinistryGroup | PartialUpdateMinistryGroup>(ministryGroup: T): RestOf<T> {
    return {
      ...ministryGroup,
      establishedDate: ministryGroup.establishedDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMinistryGroup: RestMinistryGroup): IMinistryGroup {
    return {
      ...restMinistryGroup,
      establishedDate: restMinistryGroup.establishedDate ? dayjs(restMinistryGroup.establishedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMinistryGroup>): HttpResponse<IMinistryGroup> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMinistryGroup[]>): HttpResponse<IMinistryGroup[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
