import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BookService } from '../../services/books.service';

@Component({
  selector: 'app-add-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './add-page.component.html',
  styleUrl: './add-page.component.css',
})
export class AddPageComponent {
  form;

  constructor(private fb: FormBuilder, private booksService: BookService) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      author: ['', Validators.required],
      description: ['', Validators.required],
      nbPages: ['', Validators.required],
    });
  }

  onAdd() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      this.booksService.add({
        id: null,
        title: this.form.value.title ?? '',
        author: this.form.value.author ?? '',
        description: this.form.value.description ?? '',
        nbPages: +(this.form.value.nbPages ?? 0),
      });
      this.form.reset();
    }
  }
}
