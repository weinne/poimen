import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChurch, NewChurch } from '../church.model';

export type PartialUpdateChurch = Partial<IChurch> & Pick<IChurch, 'id'>;

type RestOf<T extends IChurch | NewChurch> = Omit<T, 'dateFoundation'> & {
  dateFoundation?: string | null;
};

export type RestChurch = RestOf<IChurch>;

export type NewRestChurch = RestOf<NewChurch>;

export type PartialUpdateRestChurch = RestOf<PartialUpdateChurch>;

export type EntityResponseType = HttpResponse<IChurch>;
export type EntityArrayResponseType = HttpResponse<IChurch[]>;

@Injectable({ providedIn: 'root' })
export class ChurchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/churches');

  create(church: NewChurch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(church);
    return this.http
      .post<RestChurch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(church: IChurch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(church);
    return this.http
      .put<RestChurch>(`${this.resourceUrl}/${this.getChurchIdentifier(church)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(church: PartialUpdateChurch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(church);
    return this.http
      .patch<RestChurch>(`${this.resourceUrl}/${this.getChurchIdentifier(church)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestChurch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestChurch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChurchIdentifier(church: Pick<IChurch, 'id'>): number {
    return church.id;
  }

  compareChurch(o1: Pick<IChurch, 'id'> | null, o2: Pick<IChurch, 'id'> | null): boolean {
    return o1 && o2 ? this.getChurchIdentifier(o1) === this.getChurchIdentifier(o2) : o1 === o2;
  }

  addChurchToCollectionIfMissing<Type extends Pick<IChurch, 'id'>>(
    churchCollection: Type[],
    ...churchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const churches: Type[] = churchesToCheck.filter(isPresent);
    if (churches.length > 0) {
      const churchCollectionIdentifiers = churchCollection.map(churchItem => this.getChurchIdentifier(churchItem));
      const churchesToAdd = churches.filter(churchItem => {
        const churchIdentifier = this.getChurchIdentifier(churchItem);
        if (churchCollectionIdentifiers.includes(churchIdentifier)) {
          return false;
        }
        churchCollectionIdentifiers.push(churchIdentifier);
        return true;
      });
      return [...churchesToAdd, ...churchCollection];
    }
    return churchCollection;
  }

  protected convertDateFromClient<T extends IChurch | NewChurch | PartialUpdateChurch>(church: T): RestOf<T> {
    return {
      ...church,
      dateFoundation: church.dateFoundation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restChurch: RestChurch): IChurch {
    return {
      ...restChurch,
      dateFoundation: restChurch.dateFoundation ? dayjs(restChurch.dateFoundation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestChurch>): HttpResponse<IChurch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestChurch[]>): HttpResponse<IChurch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
