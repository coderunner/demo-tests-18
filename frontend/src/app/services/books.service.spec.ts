import { TestBed } from '@angular/core/testing';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { BookService } from './books.service';
import { provideHttpClient } from '@angular/common/http';

describe('BooksService', () => {
  let service: BookService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    // On utilise provideHttpClientTesting() pour avoir accès au HttpTestingController
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(BookService);
  });

  it('should call get on the backend api', async () => {
    const book = {
      id: '12233',
      title: 'title',
      author: 'author',
      description: 'desc',
      nbPages: 1,
    };

    // On exécute la requête au service
    // Le code du service fera un appel sur le client HTTP de test.
    const getPromise = service.get(1, 'asc');

    // On valide que l'appel HTTP est fait correctement
    const req = httpTestingController.expectOne(
      'http://127.0.0.1:8080/books?limit=1&order=asc'
    );
    expect(req.request.method).toEqual('GET');
    expect(req.request.params.get('limit')).toEqual('1');

    // On envoie la réponse au service.
    req.flush([book]);

    const response = await getPromise;
    expect(response).toEqual([book]);
  });

  it('should call post on the backend api when adding a book', (done) => {
    const book = {
      id: null,
      title: 'title',
      author: 'author',
      description: 'desc',
      nbPages: 1,
    };

    service.add(book).then((r) => {
      expect(r).toEqual({ ...book, id: 'id' });
      done();
    });

    const req = httpTestingController.expectOne('http://127.0.0.1:8080/books');
    expect(req.request.method).toEqual('POST');

    req.flush({ ...book, id: 'id' });
  });
});
