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

describe('MinistryGroup e2e test', () => {
  const ministryGroupPageUrl = '/ministry-group';
  const ministryGroupPageUrlPattern = new RegExp('/ministry-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ministryGroupSample = { name: 'immaculate', type: 'INTERNAL_SOCIETY' };

  let ministryGroup;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ministry-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ministry-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ministry-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ministryGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ministry-groups/${ministryGroup.id}`,
      }).then(() => {
        ministryGroup = undefined;
      });
    }
  });

  it('MinistryGroups menu should load MinistryGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ministry-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MinistryGroup').should('exist');
    cy.url().should('match', ministryGroupPageUrlPattern);
  });

  describe('MinistryGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ministryGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MinistryGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ministry-group/new$'));
        cy.getEntityCreateUpdateHeading('MinistryGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ministry-groups',
          body: ministryGroupSample,
        }).then(({ body }) => {
          ministryGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ministry-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ministry-groups?page=0&size=20>; rel="last",<http://localhost/api/ministry-groups?page=0&size=20>; rel="first"',
              },
              body: [ministryGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ministryGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MinistryGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ministryGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryGroupPageUrlPattern);
      });

      it('edit button click should load edit MinistryGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MinistryGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryGroupPageUrlPattern);
      });

      it('edit button click should load edit MinistryGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MinistryGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryGroupPageUrlPattern);
      });

      it('last delete button click should delete instance of MinistryGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ministryGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryGroupPageUrlPattern);

        ministryGroup = undefined;
      });
    });
  });

  describe('new MinistryGroup page', () => {
    beforeEach(() => {
      cy.visit(`${ministryGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MinistryGroup');
    });

    it('should create an instance of MinistryGroup', () => {
      cy.get(`[data-cy="name"]`).type('at sauerkraut oval');
      cy.get(`[data-cy="name"]`).should('have.value', 'at sauerkraut oval');

      cy.get(`[data-cy="description"]`).type('ascribe');
      cy.get(`[data-cy="description"]`).should('have.value', 'ascribe');

      cy.get(`[data-cy="establishedDate"]`).type('2024-12-18');
      cy.get(`[data-cy="establishedDate"]`).blur();
      cy.get(`[data-cy="establishedDate"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="type"]`).select('DEACON_BOARD');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ministryGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', ministryGroupPageUrlPattern);
    });
  });
});
