import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlanSubscription, NewPlanSubscription } from '../plan-subscription.model';

export type PartialUpdatePlanSubscription = Partial<IPlanSubscription> & Pick<IPlanSubscription, 'id'>;

type RestOf<T extends IPlanSubscription | NewPlanSubscription> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestPlanSubscription = RestOf<IPlanSubscription>;

export type NewRestPlanSubscription = RestOf<NewPlanSubscription>;

export type PartialUpdateRestPlanSubscription = RestOf<PartialUpdatePlanSubscription>;

export type EntityResponseType = HttpResponse<IPlanSubscription>;
export type EntityArrayResponseType = HttpResponse<IPlanSubscription[]>;

@Injectable({ providedIn: 'root' })
export class PlanSubscriptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plan-subscriptions');

  create(planSubscription: NewPlanSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planSubscription);
    return this.http
      .post<RestPlanSubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(planSubscription: IPlanSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planSubscription);
    return this.http
      .put<RestPlanSubscription>(`${this.resourceUrl}/${this.getPlanSubscriptionIdentifier(planSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(planSubscription: PartialUpdatePlanSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planSubscription);
    return this.http
      .patch<RestPlanSubscription>(`${this.resourceUrl}/${this.getPlanSubscriptionIdentifier(planSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlanSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlanSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlanSubscriptionIdentifier(planSubscription: Pick<IPlanSubscription, 'id'>): number {
    return planSubscription.id;
  }

  comparePlanSubscription(o1: Pick<IPlanSubscription, 'id'> | null, o2: Pick<IPlanSubscription, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanSubscriptionIdentifier(o1) === this.getPlanSubscriptionIdentifier(o2) : o1 === o2;
  }

  addPlanSubscriptionToCollectionIfMissing<Type extends Pick<IPlanSubscription, 'id'>>(
    planSubscriptionCollection: Type[],
    ...planSubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const planSubscriptions: Type[] = planSubscriptionsToCheck.filter(isPresent);
    if (planSubscriptions.length > 0) {
      const planSubscriptionCollectionIdentifiers = planSubscriptionCollection.map(planSubscriptionItem =>
        this.getPlanSubscriptionIdentifier(planSubscriptionItem),
      );
      const planSubscriptionsToAdd = planSubscriptions.filter(planSubscriptionItem => {
        const planSubscriptionIdentifier = this.getPlanSubscriptionIdentifier(planSubscriptionItem);
        if (planSubscriptionCollectionIdentifiers.includes(planSubscriptionIdentifier)) {
          return false;
        }
        planSubscriptionCollectionIdentifiers.push(planSubscriptionIdentifier);
        return true;
      });
      return [...planSubscriptionsToAdd, ...planSubscriptionCollection];
    }
    return planSubscriptionCollection;
  }

  protected convertDateFromClient<T extends IPlanSubscription | NewPlanSubscription | PartialUpdatePlanSubscription>(
    planSubscription: T,
  ): RestOf<T> {
    return {
      ...planSubscription,
      startDate: planSubscription.startDate?.toJSON() ?? null,
      endDate: planSubscription.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlanSubscription: RestPlanSubscription): IPlanSubscription {
    return {
      ...restPlanSubscription,
      startDate: restPlanSubscription.startDate ? dayjs(restPlanSubscription.startDate) : undefined,
      endDate: restPlanSubscription.endDate ? dayjs(restPlanSubscription.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlanSubscription>): HttpResponse<IPlanSubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlanSubscription[]>): HttpResponse<IPlanSubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
