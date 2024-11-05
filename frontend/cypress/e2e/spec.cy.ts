describe('Add books', () => {
  it('Visits the initial project page', () => {
    cy.visit('/');
    cy.get('[data-testid="add-link"]').click();
    cy.get('[data-testid="title-input"]').type('title');
    cy.get('[data-testid="author-input"]').type('author');
    cy.get('[data-testid="description-input"]').type('desc');
    cy.get('[data-testid="nb-pages-input"]').type('111');
    cy.get('[data-testid="add-button"]').click();

    cy.get('[data-testid="add-link"]').click();
    cy.get('[data-testid="title-input"]').type('title2');
    cy.get('[data-testid="author-input"]').type('author2');
    cy.get('[data-testid="description-input"]').type('desc2');
    cy.get('[data-testid="nb-pages-input"]').type('222');
    cy.get('[data-testid="add-button"]').click();

    cy.get('[data-testid="view-link"]').click();
    cy.get('[data-testid="more-button"]').click();
    cy.get('[data-testid="order-button"]').click();

    // cy.contains("Supprimer").click();

    // Ajouter des validations
  });
});
