import { Component, input, OnInit, output } from '@angular/core';
import { Book } from '../../model/book';

@Component({
    selector: 'app-display-books',
    imports: [],
    templateUrl: './display-books.component.html',
    styleUrl: './display-books.component.css'
})
export class DisplayBooksComponent implements OnInit {
  books = input<Book[]>();

  deleteBook = output<string>();

  constructor() {}

  ngOnInit(): void {}

  delete(id: string | null) {
    if (id) {
      this.deleteBook.emit(id);
    }
  }
}
