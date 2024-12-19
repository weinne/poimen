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

describe('Church e2e test', () => {
  const churchPageUrl = '/church';
  const churchPageUrlPattern = new RegExp('/church(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const churchSample = {
    name: 'despite whoever',
    cnpj: '46128367775868',
    address: 'an platypus',
    city: 'New Demarioburgh',
    dateFoundation: '2024-12-18',
  };

  let church;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/churches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/churches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/churches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (church) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/churches/${church.id}`,
      }).then(() => {
        church = undefined;
      });
    }
  });

  it('Churches menu should load Churches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('church');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Church').should('exist');
    cy.url().should('match', churchPageUrlPattern);
  });

  describe('Church page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(churchPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Church page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/church/new$'));
        cy.getEntityCreateUpdateHeading('Church');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/churches',
          body: churchSample,
        }).then(({ body }) => {
          church = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/churches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [church],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(churchPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Church page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('church');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchPageUrlPattern);
      });

      it('edit button click should load edit Church page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Church');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchPageUrlPattern);
      });

      it('edit button click should load edit Church page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Church');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchPageUrlPattern);
      });

      it('last delete button click should delete instance of Church', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('church').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', churchPageUrlPattern);

        church = undefined;
      });
    });
  });

  describe('new Church page', () => {
    beforeEach(() => {
      cy.visit(`${churchPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Church');
    });

    it('should create an instance of Church', () => {
      cy.get(`[data-cy="name"]`).type('wrong hm hmph');
      cy.get(`[data-cy="name"]`).should('have.value', 'wrong hm hmph');

      cy.get(`[data-cy="cnpj"]`).type('91012108208636');
      cy.get(`[data-cy="cnpj"]`).should('have.value', '91012108208636');

      cy.get(`[data-cy="address"]`).type('unripe unless');
      cy.get(`[data-cy="address"]`).should('have.value', 'unripe unless');

      cy.get(`[data-cy="city"]`).type('Jailynstead');
      cy.get(`[data-cy="city"]`).should('have.value', 'Jailynstead');

      cy.get(`[data-cy="dateFoundation"]`).type('2024-12-18');
      cy.get(`[data-cy="dateFoundation"]`).blur();
      cy.get(`[data-cy="dateFoundation"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="phone"]`).type('496.631.7027');
      cy.get(`[data-cy="phone"]`).should('have.value', '496.631.7027');

      cy.get(`[data-cy="email"]`).type('Perry_Mayert98@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Perry_Mayert98@yahoo.com');

      cy.get(`[data-cy="website"]`).type('ouch lobster');
      cy.get(`[data-cy="website"]`).should('have.value', 'ouch lobster');

      cy.get(`[data-cy="facebook"]`).type('inscribe');
      cy.get(`[data-cy="facebook"]`).should('have.value', 'inscribe');

      cy.get(`[data-cy="instagram"]`).type('svelte mmm');
      cy.get(`[data-cy="instagram"]`).should('have.value', 'svelte mmm');

      cy.get(`[data-cy="twitter"]`).type('or');
      cy.get(`[data-cy="twitter"]`).should('have.value', 'or');

      cy.get(`[data-cy="youtube"]`).type('formula');
      cy.get(`[data-cy="youtube"]`).should('have.value', 'formula');

      cy.get(`[data-cy="about"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="about"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        church = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', churchPageUrlPattern);
    });
  });
});
