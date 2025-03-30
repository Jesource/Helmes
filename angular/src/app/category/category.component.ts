import { Component, Input } from '@angular/core';
import { CategoryDTO } from '../api.service';
import {JsonPipe, NgForOf, NgIf, SlicePipe} from "@angular/common";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    JsonPipe,
    SlicePipe
  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent {
  // @Input() category!: CategoryDTO;
  @Input() categories: CategoryDTO[] = [];
  @Input() selectedCategories: number[] = [];
  @Input() onCheckboxChange: (event: any, categoryId: number) => void =
    (event, categoryId) => {
      console.warn(`onCheckboxChange was called with categoryId=${categoryId}, but no implementation was provided.`);
    };
}
