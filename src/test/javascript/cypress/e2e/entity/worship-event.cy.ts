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

describe('WorshipEvent e2e test', () => {
  const worshipEventPageUrl = '/worship-event';
  const worshipEventPageUrlPattern = new RegExp('/worship-event(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const worshipEventSample = { date: '2024-12-18T20:56:52.847Z', worshipType: 'SPECIAL_EVENT' };

  let worshipEvent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/worship-events+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/worship-events').as('postEntityRequest');
    cy.intercept('DELETE', '/api/worship-events/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (worshipEvent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/worship-events/${worshipEvent.id}`,
      }).then(() => {
        worshipEvent = undefined;
      });
    }
  });

  it('WorshipEvents menu should load WorshipEvents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('worship-event');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorshipEvent').should('exist');
    cy.url().should('match', worshipEventPageUrlPattern);
  });

  describe('WorshipEvent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(worshipEventPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WorshipEvent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/worship-event/new$'));
        cy.getEntityCreateUpdateHeading('WorshipEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', worshipEventPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/worship-events',
          body: worshipEventSample,
        }).then(({ body }) => {
          worshipEvent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/worship-events+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/worship-events?page=0&size=20>; rel="last",<http://localhost/api/worship-events?page=0&size=20>; rel="first"',
              },
              body: [worshipEvent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(worshipEventPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WorshipEvent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('worshipEvent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', worshipEventPageUrlPattern);
      });

      it('edit button click should load edit WorshipEvent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorshipEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', worshipEventPageUrlPattern);
      });

      it('edit button click should load edit WorshipEvent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorshipEvent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', worshipEventPageUrlPattern);
      });

      it('last delete button click should delete instance of WorshipEvent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('worshipEvent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', worshipEventPageUrlPattern);

        worshipEvent = undefined;
      });
    });
  });

  describe('new WorshipEvent page', () => {
    beforeEach(() => {
      cy.visit(`${worshipEventPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WorshipEvent');
    });

    it('should create an instance of WorshipEvent', () => {
      cy.get(`[data-cy="date"]`).type('2024-12-18T20:23');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-12-18T20:23');

      cy.get(`[data-cy="title"]`).type('a');
      cy.get(`[data-cy="title"]`).should('have.value', 'a');

      cy.get(`[data-cy="guestPreacher"]`).type('until fathom out');
      cy.get(`[data-cy="guestPreacher"]`).should('have.value', 'until fathom out');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="callToWorshipText"]`).type('apologise towards');
      cy.get(`[data-cy="callToWorshipText"]`).should('have.value', 'apologise towards');

      cy.get(`[data-cy="confessionOfSinText"]`).type('although nor aboard');
      cy.get(`[data-cy="confessionOfSinText"]`).should('have.value', 'although nor aboard');

      cy.get(`[data-cy="assuranceOfPardonText"]`).type('gratefully jittery');
      cy.get(`[data-cy="assuranceOfPardonText"]`).should('have.value', 'gratefully jittery');

      cy.get(`[data-cy="lordSupperText"]`).type('buck');
      cy.get(`[data-cy="lordSupperText"]`).should('have.value', 'buck');

      cy.get(`[data-cy="benedictionText"]`).type('questionably unsung while');
      cy.get(`[data-cy="benedictionText"]`).should('have.value', 'questionably unsung while');

      cy.get(`[data-cy="confessionalText"]`).type('motivate dish miserable');
      cy.get(`[data-cy="confessionalText"]`).should('have.value', 'motivate dish miserable');

      cy.get(`[data-cy="sermonText"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="sermonText"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('sermonFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="sermonLink"]`).type('other generously');
      cy.get(`[data-cy="sermonLink"]`).should('have.value', 'other generously');

      cy.get(`[data-cy="youtubeLink"]`).type('political down geez');
      cy.get(`[data-cy="youtubeLink"]`).should('have.value', 'political down geez');

      cy.setFieldImageAsBytesOfEntity('bulletinFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="worshipType"]`).select('SPECIAL_EVENT');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        worshipEvent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', worshipEventPageUrlPattern);
    });
  });
});
