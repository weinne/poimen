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

describe('Appointment e2e test', () => {
  const appointmentPageUrl = '/appointment';
  const appointmentPageUrlPattern = new RegExp('/appointment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const appointmentSample = { subject: 'ad afore', startTime: '2024-12-18T22:15:00.703Z', appointmentType: 'MEETING' };

  let appointment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/appointments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/appointments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/appointments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (appointment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/appointments/${appointment.id}`,
      }).then(() => {
        appointment = undefined;
      });
    }
  });

  it('Appointments menu should load Appointments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Appointment').should('exist');
    cy.url().should('match', appointmentPageUrlPattern);
  });

  describe('Appointment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(appointmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Appointment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/appointment/new$'));
        cy.getEntityCreateUpdateHeading('Appointment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appointmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/appointments',
          body: appointmentSample,
        }).then(({ body }) => {
          appointment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/appointments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/appointments?page=0&size=20>; rel="last",<http://localhost/api/appointments?page=0&size=20>; rel="first"',
              },
              body: [appointment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(appointmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Appointment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('appointment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appointmentPageUrlPattern);
      });

      it('edit button click should load edit Appointment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Appointment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appointmentPageUrlPattern);
      });

      it('edit button click should load edit Appointment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Appointment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appointmentPageUrlPattern);
      });

      it('last delete button click should delete instance of Appointment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('appointment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appointmentPageUrlPattern);

        appointment = undefined;
      });
    });
  });

  describe('new Appointment page', () => {
    beforeEach(() => {
      cy.visit(`${appointmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Appointment');
    });

    it('should create an instance of Appointment', () => {
      cy.get(`[data-cy="subject"]`).type('repossess notwithstanding');
      cy.get(`[data-cy="subject"]`).should('have.value', 'repossess notwithstanding');

      cy.get(`[data-cy="startTime"]`).type('2024-12-18T20:02');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-12-18T20:02');

      cy.get(`[data-cy="endTime"]`).type('2024-12-18T16:08');
      cy.get(`[data-cy="endTime"]`).blur();
      cy.get(`[data-cy="endTime"]`).should('have.value', '2024-12-18T16:08');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="local"]`).type('rewrite');
      cy.get(`[data-cy="local"]`).should('have.value', 'rewrite');

      cy.get(`[data-cy="appointmentType"]`).select('EVENT');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        appointment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', appointmentPageUrlPattern);
    });
  });
});
