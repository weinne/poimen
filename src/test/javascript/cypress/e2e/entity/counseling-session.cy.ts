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

describe('CounselingSession e2e test', () => {
  const counselingSessionPageUrl = '/counseling-session';
  const counselingSessionPageUrlPattern = new RegExp('/counseling-session(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const counselingSessionSample = {
    subject: 'meanwhile too into',
    date: '2024-12-18',
    startTime: '2024-12-18T05:32:30.534Z',
    status: 'CANCELED',
  };

  let counselingSession;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/counseling-sessions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/counseling-sessions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/counseling-sessions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (counselingSession) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/counseling-sessions/${counselingSession.id}`,
      }).then(() => {
        counselingSession = undefined;
      });
    }
  });

  it('CounselingSessions menu should load CounselingSessions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('counseling-session');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CounselingSession').should('exist');
    cy.url().should('match', counselingSessionPageUrlPattern);
  });

  describe('CounselingSession page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(counselingSessionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CounselingSession page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/counseling-session/new$'));
        cy.getEntityCreateUpdateHeading('CounselingSession');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', counselingSessionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/counseling-sessions',
          body: counselingSessionSample,
        }).then(({ body }) => {
          counselingSession = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/counseling-sessions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/counseling-sessions?page=0&size=20>; rel="last",<http://localhost/api/counseling-sessions?page=0&size=20>; rel="first"',
              },
              body: [counselingSession],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(counselingSessionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CounselingSession page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('counselingSession');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', counselingSessionPageUrlPattern);
      });

      it('edit button click should load edit CounselingSession page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CounselingSession');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', counselingSessionPageUrlPattern);
      });

      it('edit button click should load edit CounselingSession page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CounselingSession');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', counselingSessionPageUrlPattern);
      });

      it('last delete button click should delete instance of CounselingSession', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('counselingSession').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', counselingSessionPageUrlPattern);

        counselingSession = undefined;
      });
    });
  });

  describe('new CounselingSession page', () => {
    beforeEach(() => {
      cy.visit(`${counselingSessionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CounselingSession');
    });

    it('should create an instance of CounselingSession', () => {
      cy.get(`[data-cy="subject"]`).type('precious hospitalization');
      cy.get(`[data-cy="subject"]`).should('have.value', 'precious hospitalization');

      cy.get(`[data-cy="date"]`).type('2024-12-18');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="startTime"]`).type('2024-12-18T23:13');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-12-18T23:13');

      cy.get(`[data-cy="endTime"]`).type('2024-12-18T10:31');
      cy.get(`[data-cy="endTime"]`).blur();
      cy.get(`[data-cy="endTime"]`).should('have.value', '2024-12-18T10:31');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="counselingTasks"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="counselingTasks"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="status"]`).select('IN_PROGRESS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        counselingSession = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', counselingSessionPageUrlPattern);
    });
  });
});
