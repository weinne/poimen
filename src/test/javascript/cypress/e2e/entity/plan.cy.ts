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

describe('Plan e2e test', () => {
  const planPageUrl = '/plan';
  const planPageUrlPattern = new RegExp('/plan(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const planSample = { name: 'ew filthy yuck', price: 'queasily' };

  let plan;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/plans+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/plans').as('postEntityRequest');
    cy.intercept('DELETE', '/api/plans/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (plan) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/plans/${plan.id}`,
      }).then(() => {
        plan = undefined;
      });
    }
  });

  it('Plans menu should load Plans page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('plan');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Plan').should('exist');
    cy.url().should('match', planPageUrlPattern);
  });

  describe('Plan page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(planPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Plan page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/plan/new$'));
        cy.getEntityCreateUpdateHeading('Plan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/plans',
          body: planSample,
        }).then(({ body }) => {
          plan = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/plans+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [plan],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(planPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Plan page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('plan');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planPageUrlPattern);
      });

      it('edit button click should load edit Plan page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Plan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planPageUrlPattern);
      });

      it('edit button click should load edit Plan page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Plan');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planPageUrlPattern);
      });

      it('last delete button click should delete instance of Plan', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('plan').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', planPageUrlPattern);

        plan = undefined;
      });
    });
  });

  describe('new Plan page', () => {
    beforeEach(() => {
      cy.visit(`${planPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Plan');
    });

    it('should create an instance of Plan', () => {
      cy.get(`[data-cy="name"]`).type('underneath ah');
      cy.get(`[data-cy="name"]`).should('have.value', 'underneath ah');

      cy.get(`[data-cy="price"]`).type('after hollow onto');
      cy.get(`[data-cy="price"]`).should('have.value', 'after hollow onto');

      cy.get(`[data-cy="description"]`).type('with');
      cy.get(`[data-cy="description"]`).should('have.value', 'with');

      cy.get(`[data-cy="features"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="features"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="renewalPeriod"]`).type('boohoo apud supposing');
      cy.get(`[data-cy="renewalPeriod"]`).should('have.value', 'boohoo apud supposing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        plan = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', planPageUrlPattern);
    });
  });
});
