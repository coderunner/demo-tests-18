import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayBooksComponent } from './display-books.component';
import { TestHelper } from '../../tests/test-helper';

// Nom de la suite, normallement le nom du composant ou service à tester.
// Il est possible d'imbriquer des describe pour organiser les tests de façon hiérarchique.
describe('DisplayBooksComponent', () => {
  let component: DisplayBooksComponent;
  let fixture: ComponentFixture<DisplayBooksComponent>;
  let testHelper: TestHelper<DisplayBooksComponent>;

  /**
   * beforeEach est exécuté avant chaque test (it).
   */
  beforeEach(async () => {
    await TestBed.configureTestingModule({}).compileComponents();

    // La fixture est un objet utilitaire pour nous aider à tester le composant.
    fixture = TestBed.createComponent(DisplayBooksComponent);
    // Elle contient une référence au composant.
    component = fixture.componentInstance;
    // On peut initialiser le TestHelper.
    testHelper = new TestHelper(fixture);
    // On attend que tout soit stable.
    fixture.detectChanges();
  });

  // 'it' est le nom de la spécification, c'est ce qui sera affiché dans le rapport de test.
  it('should not display books if empty', () => {
    const title = testHelper.getElement('title');
    expect(title).toBeUndefined();
  });

  it('should display books', () => {
    // On assigne une valeur au Input du composant.
    fixture.componentRef.setInput('books', [
      {
        id: '123',
        title: 'title',
        author: 'author',
        description: 'desc',
        nbPages: 12,
      },
      {
        id: '1232',
        title: 'title2',
        author: 'author2',
        description: 'desc2',
        nbPages: 122,
      },
    ]);

    // On force la détection de changement.
    fixture.detectChanges();

    // On valide l'affichage
    const titles = testHelper.getElements('title');
    expect(titles).toBeDefined();
    expect(titles.length).toBe(2);
    expect(titles[0].innerText).toEqual('Titre: title');
    expect(titles[1].innerText).toEqual('Titre: title2');
  });
});
