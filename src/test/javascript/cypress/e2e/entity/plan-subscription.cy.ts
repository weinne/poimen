import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PlanSubscription e2e test', () => {
  const planSubscriptionPageUrl = '/plan-subscription';
  const planSubscriptionPageUrlPattern = new RegExp('/plan-subscription(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const planSubscriptionSample = {
    description: 'unit',
    startDate: '2024-12-18',
    status: 'CANCELED',
    paymentProvider: 'PAYPAL',
    paymentStatus: 'REFUNDED',
  };

  let planSubscription;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/plan-subscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/plan-subscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/plan-subscriptions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (planSubscription) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/plan-subscriptions/${planSubscription.id}`,
      }).then(() => {
        planSubscription = undefined;
      });
    }
  });

  it('PlanSubscriptions menu should load PlanSubscriptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('plan-subscription');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PlanSubscription').should('exist');
    cy.url().should('match', planSubscriptionPageUrlPattern);
  });

  describe('PlanSubscription page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(planSubscriptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PlanSubscription page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/plan-subscription/new$'));
        cy.getEntityCreateUpdateHeading('PlanSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planSubscriptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/plan-subscriptions',
          body: planSubscriptionSample,
        }).then(({ body }) => {
          planSubscription = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/plan-subscriptions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [planSubscription],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(planSubscriptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PlanSubscription page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('planSubscription');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PlanSubscription page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlanSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PlanSubscription page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlanSubscription');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planSubscriptionPageUrlPattern);
      });

      it('last delete button click should delete instance of PlanSubscription', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('planSubscription').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planSubscriptionPageUrlPattern);

        planSubscription = undefined;
      });
    });
  });

  describe('new PlanSubscription page', () => {
    beforeEach(() => {
      cy.visit(`${planSubscriptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PlanSubscription');
    });

    it('should create an instance of PlanSubscription', () => {
      cy.get(`[data-cy="description"]`).type('er tut and');
      cy.get(`[data-cy="description"]`).should('have.value', 'er tut and');

      cy.get(`[data-cy="startDate"]`).type('2024-12-18');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="endDate"]`).type('2024-12-18');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="paymentProvider"]`).select('STRIPE');

      cy.get(`[data-cy="paymentStatus"]`).select('PENDING');

      cy.get(`[data-cy="paymentReference"]`).type('yippee mismatch');
      cy.get(`[data-cy="paymentReference"]`).should('have.value', 'yippee mismatch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        planSubscription = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', planSubscriptionPageUrlPattern);
    });
  });
});
