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
import { ICounselingSession, NewCounselingSession } from '../counseling-session.model';

export type PartialUpdateCounselingSession = Partial<ICounselingSession> & Pick<ICounselingSession, 'id'>;

type RestOf<T extends ICounselingSession | NewCounselingSession> = Omit<T, 'date' | 'startTime' | 'endTime'> & {
  date?: string | null;
  startTime?: string | null;
  endTime?: string | null;
};

export type RestCounselingSession = RestOf<ICounselingSession>;

export type NewRestCounselingSession = RestOf<NewCounselingSession>;

export type PartialUpdateRestCounselingSession = RestOf<PartialUpdateCounselingSession>;

export type EntityResponseType = HttpResponse<ICounselingSession>;
export type EntityArrayResponseType = HttpResponse<ICounselingSession[]>;

@Injectable({ providedIn: 'root' })
export class CounselingSessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counseling-sessions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/counseling-sessions/_search');

  create(counselingSession: NewCounselingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(counselingSession);
    return this.http
      .post<RestCounselingSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(counselingSession: ICounselingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(counselingSession);
    return this.http
      .put<RestCounselingSession>(`${this.resourceUrl}/${this.getCounselingSessionIdentifier(counselingSession)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(counselingSession: PartialUpdateCounselingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(counselingSession);
    return this.http
      .patch<RestCounselingSession>(`${this.resourceUrl}/${this.getCounselingSessionIdentifier(counselingSession)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCounselingSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCounselingSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestCounselingSession[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ICounselingSession[]>()], asapScheduler)),
    );
  }

  getCounselingSessionIdentifier(counselingSession: Pick<ICounselingSession, 'id'>): number {
    return counselingSession.id;
  }

  compareCounselingSession(o1: Pick<ICounselingSession, 'id'> | null, o2: Pick<ICounselingSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getCounselingSessionIdentifier(o1) === this.getCounselingSessionIdentifier(o2) : o1 === o2;
  }

  addCounselingSessionToCollectionIfMissing<Type extends Pick<ICounselingSession, 'id'>>(
    counselingSessionCollection: Type[],
    ...counselingSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const counselingSessions: Type[] = counselingSessionsToCheck.filter(isPresent);
    if (counselingSessions.length > 0) {
      const counselingSessionCollectionIdentifiers = counselingSessionCollection.map(counselingSessionItem =>
        this.getCounselingSessionIdentifier(counselingSessionItem),
      );
      const counselingSessionsToAdd = counselingSessions.filter(counselingSessionItem => {
        const counselingSessionIdentifier = this.getCounselingSessionIdentifier(counselingSessionItem);
        if (counselingSessionCollectionIdentifiers.includes(counselingSessionIdentifier)) {
          return false;
        }
        counselingSessionCollectionIdentifiers.push(counselingSessionIdentifier);
        return true;
      });
      return [...counselingSessionsToAdd, ...counselingSessionCollection];
    }
    return counselingSessionCollection;
  }

  protected convertDateFromClient<T extends ICounselingSession | NewCounselingSession | PartialUpdateCounselingSession>(
    counselingSession: T,
  ): RestOf<T> {
    return {
      ...counselingSession,
      date: counselingSession.date?.format(DATE_FORMAT) ?? null,
      startTime: counselingSession.startTime?.toJSON() ?? null,
      endTime: counselingSession.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCounselingSession: RestCounselingSession): ICounselingSession {
    return {
      ...restCounselingSession,
      date: restCounselingSession.date ? dayjs(restCounselingSession.date) : undefined,
      startTime: restCounselingSession.startTime ? dayjs(restCounselingSession.startTime) : undefined,
      endTime: restCounselingSession.endTime ? dayjs(restCounselingSession.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCounselingSession>): HttpResponse<ICounselingSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCounselingSession[]>): HttpResponse<ICounselingSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
