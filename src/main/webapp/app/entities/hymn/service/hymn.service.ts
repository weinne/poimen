import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IHymn, NewHymn } from '../hymn.model';

export type PartialUpdateHymn = Partial<IHymn> & Pick<IHymn, 'id'>;

export type EntityResponseType = HttpResponse<IHymn>;
export type EntityArrayResponseType = HttpResponse<IHymn[]>;

@Injectable({ providedIn: 'root' })
export class HymnService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hymns');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/hymns/_search');

  create(hymn: NewHymn): Observable<EntityResponseType> {
    return this.http.post<IHymn>(this.resourceUrl, hymn, { observe: 'response' });
  }

  update(hymn: IHymn): Observable<EntityResponseType> {
    return this.http.put<IHymn>(`${this.resourceUrl}/${this.getHymnIdentifier(hymn)}`, hymn, { observe: 'response' });
  }

  partialUpdate(hymn: PartialUpdateHymn): Observable<EntityResponseType> {
    return this.http.patch<IHymn>(`${this.resourceUrl}/${this.getHymnIdentifier(hymn)}`, hymn, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHymn>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHymn[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHymn[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IHymn[]>()], asapScheduler)));
  }

  getHymnIdentifier(hymn: Pick<IHymn, 'id'>): number {
    return hymn.id;
  }

  compareHymn(o1: Pick<IHymn, 'id'> | null, o2: Pick<IHymn, 'id'> | null): boolean {
    return o1 && o2 ? this.getHymnIdentifier(o1) === this.getHymnIdentifier(o2) : o1 === o2;
  }

  addHymnToCollectionIfMissing<Type extends Pick<IHymn, 'id'>>(
    hymnCollection: Type[],
    ...hymnsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hymns: Type[] = hymnsToCheck.filter(isPresent);
    if (hymns.length > 0) {
      const hymnCollectionIdentifiers = hymnCollection.map(hymnItem => this.getHymnIdentifier(hymnItem));
      const hymnsToAdd = hymns.filter(hymnItem => {
        const hymnIdentifier = this.getHymnIdentifier(hymnItem);
        if (hymnCollectionIdentifiers.includes(hymnIdentifier)) {
          return false;
        }
        hymnCollectionIdentifiers.push(hymnIdentifier);
        return true;
      });
      return [...hymnsToAdd, ...hymnCollection];
    }
    return hymnCollection;
  }
}
