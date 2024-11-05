import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../environments/environment';
import { Book } from '../model/book';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  constructor(private httpClient: HttpClient) {}

  get(limit: number = 2, order: 'asc' | 'desc' = 'asc'): Promise<Book[]> {
    const params = new HttpParams().set('limit', limit).set('order', order);
    return firstValueFrom(
      this.httpClient.get<Book[]>(`${environment.backendUrl}/books`, {
        params: params,
      })
    );
  }

  add(book: Book): Promise<Book> {
    return firstValueFrom(
      this.httpClient.post<Book>(`${environment.backendUrl}/books`, book)
    );
  }

  delete(id: string): Promise<Book> {
    return firstValueFrom(
      this.httpClient.delete<Book>(`${environment.backendUrl}/books/${id}`)
    );
  }
}
