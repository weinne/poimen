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

describe('Hymn e2e test', () => {
  const hymnPageUrl = '/hymn';
  const hymnPageUrlPattern = new RegExp('/hymn(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hymnSample = { title: 'mostly aw beside' };

  let hymn;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/hymns+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/hymns').as('postEntityRequest');
    cy.intercept('DELETE', '/api/hymns/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hymn) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/hymns/${hymn.id}`,
      }).then(() => {
        hymn = undefined;
      });
    }
  });

  it('Hymns menu should load Hymns page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('hymn');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Hymn').should('exist');
    cy.url().should('match', hymnPageUrlPattern);
  });

  describe('Hymn page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hymnPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Hymn page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/hymn/new$'));
        cy.getEntityCreateUpdateHeading('Hymn');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hymnPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/hymns',
          body: hymnSample,
        }).then(({ body }) => {
          hymn = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/hymns+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [hymn],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hymnPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Hymn page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hymn');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hymnPageUrlPattern);
      });

      it('edit button click should load edit Hymn page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Hymn');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hymnPageUrlPattern);
      });

      it('edit button click should load edit Hymn page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Hymn');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hymnPageUrlPattern);
      });

      it('last delete button click should delete instance of Hymn', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('hymn').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hymnPageUrlPattern);

        hymn = undefined;
      });
    });
  });

  describe('new Hymn page', () => {
    beforeEach(() => {
      cy.visit(`${hymnPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Hymn');
    });

    it('should create an instance of Hymn', () => {
      cy.get(`[data-cy="title"]`).type('intently meh');
      cy.get(`[data-cy="title"]`).should('have.value', 'intently meh');

      cy.get(`[data-cy="lyricsAuthor"]`).type('though gah');
      cy.get(`[data-cy="lyricsAuthor"]`).should('have.value', 'though gah');

      cy.get(`[data-cy="musicAuthor"]`).type('tenant oh soliloquy');
      cy.get(`[data-cy="musicAuthor"]`).should('have.value', 'tenant oh soliloquy');

      cy.get(`[data-cy="hymnary"]`).type('fooey bah');
      cy.get(`[data-cy="hymnary"]`).should('have.value', 'fooey bah');

      cy.get(`[data-cy="hymnNumber"]`).type('corny phooey');
      cy.get(`[data-cy="hymnNumber"]`).should('have.value', 'corny phooey');

      cy.get(`[data-cy="link"]`).type('silently indeed alert');
      cy.get(`[data-cy="link"]`).should('have.value', 'silently indeed alert');

      cy.get(`[data-cy="youtubeLink"]`).type('accidentally');
      cy.get(`[data-cy="youtubeLink"]`).should('have.value', 'accidentally');

      cy.setFieldImageAsBytesOfEntity('sheetMusic', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('midi', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="tone"]`).type('provi');
      cy.get(`[data-cy="tone"]`).should('have.value', 'provi');

      cy.get(`[data-cy="lyrics"]`).type('blah eek');
      cy.get(`[data-cy="lyrics"]`).should('have.value', 'blah eek');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        hymn = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', hymnPageUrlPattern);
    });
  });
});
