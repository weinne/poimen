import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlan, NewPlan } from '../plan.model';

export type PartialUpdatePlan = Partial<IPlan> & Pick<IPlan, 'id'>;

export type EntityResponseType = HttpResponse<IPlan>;
export type EntityArrayResponseType = HttpResponse<IPlan[]>;

@Injectable({ providedIn: 'root' })
export class PlanService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plans');

  create(plan: NewPlan): Observable<EntityResponseType> {
    return this.http.post<IPlan>(this.resourceUrl, plan, { observe: 'response' });
  }

  update(plan: IPlan): Observable<EntityResponseType> {
    return this.http.put<IPlan>(`${this.resourceUrl}/${this.getPlanIdentifier(plan)}`, plan, { observe: 'response' });
  }

  partialUpdate(plan: PartialUpdatePlan): Observable<EntityResponseType> {
    return this.http.patch<IPlan>(`${this.resourceUrl}/${this.getPlanIdentifier(plan)}`, plan, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlan[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlanIdentifier(plan: Pick<IPlan, 'id'>): number {
    return plan.id;
  }

  comparePlan(o1: Pick<IPlan, 'id'> | null, o2: Pick<IPlan, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanIdentifier(o1) === this.getPlanIdentifier(o2) : o1 === o2;
  }

  addPlanToCollectionIfMissing<Type extends Pick<IPlan, 'id'>>(
    planCollection: Type[],
    ...plansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const plans: Type[] = plansToCheck.filter(isPresent);
    if (plans.length > 0) {
      const planCollectionIdentifiers = planCollection.map(planItem => this.getPlanIdentifier(planItem));
      const plansToAdd = plans.filter(planItem => {
        const planIdentifier = this.getPlanIdentifier(planItem);
        if (planCollectionIdentifiers.includes(planIdentifier)) {
          return false;
        }
        planCollectionIdentifiers.push(planIdentifier);
        return true;
      });
      return [...plansToAdd, ...planCollection];
    }
    return planCollection;
  }
}
