import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPageComponent } from './view-page.component';
import { TestHelper } from '../../tests/test-helper';
import { BookService } from '../../services/books.service';
import { Book } from '../../model/book';

// Nom de la série de test.
describe('ViewPageComponent', () => {
  let fixture: ComponentFixture<ViewPageComponent>;
  let testHelper: TestHelper<ViewPageComponent>;

  // On se défini un mock pour simuler le BookService.
  // Nous avons seulement besoin de définir les méthodes qui seront utiliser dans le tests.
  // Le comportement sera définit dans le test.
  const bookServiceMock = {
    get: async (limit: number, order: 'asc' | 'desc'): Promise<Book[]> => {
      return [];
    },
  };

  beforeEach(async () => {
    // Toujours valider les imports et les providers.
    await TestBed.configureTestingModule({
      imports: [],
      // On défini un provider pour le test pour qu'Angular nous donne notre stub et non le vrai service.
      providers: [{ provide: BookService, useValue: bookServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewPageComponent);
    testHelper = new TestHelper(fixture);
    fixture.detectChanges();
  });

  it('should call the BookService with the limit', () => {
    // On crée un spy sur la méthode get de notre stub.
    const getSpy = spyOn(bookServiceMock, 'get').and.returnValue(
      Promise.resolve([
        {
          id: '123',
          title: 'title',
          author: 'author',
          description: 'desc',
          nbPages: 12,
        },
      ])
    );

    // On simule le clique sur le bouton.
    const button = testHelper.getButton('more-button');
    button.click();

    // On peut valider les appels au stub.
    expect(getSpy).toHaveBeenCalledWith(2, 'asc');

    button.click();
    expect(getSpy).toHaveBeenCalledWith(3, 'asc');
  });

  it('should call the BookService with the order', () => {
    const getSpy = spyOn(bookServiceMock, 'get');

    const button = testHelper.getButton('order-button');
    button.click();

    expect(getSpy).toHaveBeenCalledWith(1, 'desc');

    button.click();
    expect(getSpy).toHaveBeenCalledWith(1, 'asc');
  });
});
