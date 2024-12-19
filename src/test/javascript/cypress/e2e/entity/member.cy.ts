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

describe('Member e2e test', () => {
  const memberPageUrl = '/member';
  const memberPageUrlPattern = new RegExp('/member(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const memberSample = {
    name: 'yak',
    dateOfBirth: '2024-12-18',
    maritalStatus: 'MARRIED',
    status: 'PASTOR',
    cpf: '05185333642',
    rg: 'mechanically',
  };

  let member;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (member) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/members/${member.id}`,
      }).then(() => {
        member = undefined;
      });
    }
  });

  it('Members menu should load Members page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Member').should('exist');
    cy.url().should('match', memberPageUrlPattern);
  });

  describe('Member page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(memberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Member page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/member/new$'));
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/members',
          body: memberSample,
        }).then(({ body }) => {
          member = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/members?page=0&size=20>; rel="last",<http://localhost/api/members?page=0&size=20>; rel="first"',
              },
              body: [member],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(memberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Member page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('member');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberPageUrlPattern);
      });

      it('edit button click should load edit Member page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberPageUrlPattern);
      });

      it('edit button click should load edit Member page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberPageUrlPattern);
      });

      it('last delete button click should delete instance of Member', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('member').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberPageUrlPattern);

        member = undefined;
      });
    });
  });

  describe('new Member page', () => {
    beforeEach(() => {
      cy.visit(`${memberPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Member');
    });

    it('should create an instance of Member', () => {
      cy.get(`[data-cy="name"]`).type('ew');
      cy.get(`[data-cy="name"]`).should('have.value', 'ew');

      cy.setFieldImageAsBytesOfEntity('photo', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="email"]`).type('Michaela29@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Michaela29@gmail.com');

      cy.get(`[data-cy="phoneNumber"]`).type('mockingly');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'mockingly');

      cy.get(`[data-cy="dateOfBirth"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfBirth"]`).blur();
      cy.get(`[data-cy="dateOfBirth"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="address"]`).type('impressionable eek pointed');
      cy.get(`[data-cy="address"]`).should('have.value', 'impressionable eek pointed');

      cy.get(`[data-cy="city"]`).type('Mayrastad');
      cy.get(`[data-cy="city"]`).should('have.value', 'Mayrastad');

      cy.get(`[data-cy="state"]`).type('hmph from stupendous');
      cy.get(`[data-cy="state"]`).should('have.value', 'hmph from stupendous');

      cy.get(`[data-cy="zipCode"]`).type('93327-8107');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '93327-8107');

      cy.get(`[data-cy="cityOfBirth"]`).type('inasmuch beyond');
      cy.get(`[data-cy="cityOfBirth"]`).should('have.value', 'inasmuch beyond');

      cy.get(`[data-cy="previousReligion"]`).type('ouch owlishly');
      cy.get(`[data-cy="previousReligion"]`).should('have.value', 'ouch owlishly');

      cy.get(`[data-cy="maritalStatus"]`).select('WIDOWED');

      cy.get(`[data-cy="spouseName"]`).type('trim loose scram');
      cy.get(`[data-cy="spouseName"]`).should('have.value', 'trim loose scram');

      cy.get(`[data-cy="dateOfMarriage"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfMarriage"]`).blur();
      cy.get(`[data-cy="dateOfMarriage"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="status"]`).select('VISITOR');

      cy.get(`[data-cy="cpf"]`).type('01019804509');
      cy.get(`[data-cy="cpf"]`).should('have.value', '01019804509');

      cy.get(`[data-cy="rg"]`).type('windy aside');
      cy.get(`[data-cy="rg"]`).should('have.value', 'windy aside');

      cy.get(`[data-cy="dateOfBaptism"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfBaptism"]`).blur();
      cy.get(`[data-cy="dateOfBaptism"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="churchOfBaptism"]`).type('careless gleefully playfully');
      cy.get(`[data-cy="churchOfBaptism"]`).should('have.value', 'careless gleefully playfully');

      cy.get(`[data-cy="dateOfMembership"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfMembership"]`).blur();
      cy.get(`[data-cy="dateOfMembership"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="typeOfMembership"]`).select('TRANSFER');

      cy.get(`[data-cy="associationMeetingMinutes"]`).type('aggressive');
      cy.get(`[data-cy="associationMeetingMinutes"]`).should('have.value', 'aggressive');

      cy.get(`[data-cy="dateOfDeath"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfDeath"]`).blur();
      cy.get(`[data-cy="dateOfDeath"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="dateOfExit"]`).type('2024-12-18');
      cy.get(`[data-cy="dateOfExit"]`).blur();
      cy.get(`[data-cy="dateOfExit"]`).should('have.value', '2024-12-18');

      cy.get(`[data-cy="exitDestination"]`).type('as mostly on');
      cy.get(`[data-cy="exitDestination"]`).should('have.value', 'as mostly on');

      cy.get(`[data-cy="exitReason"]`).select('TRANSFER');

      cy.get(`[data-cy="exitMeetingMinutes"]`).type('bemuse');
      cy.get(`[data-cy="exitMeetingMinutes"]`).should('have.value', 'bemuse');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        member = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', memberPageUrlPattern);
    });
  });
});
