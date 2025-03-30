import {Component, OnInit} from '@angular/core';
import {ApiService, CategoryDTO} from './api.service';
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {CategoryComponent} from "./category/category.component";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, CategoryComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  // categoryForm!: FormGroup;
  categoryForm: FormGroup = this.formBuilder.group({  // Ensure it's defined at the start
    name: [''],
    idsOfInvolvedSectors: [[]],
    agreeToTerms: [false],
  });
  categories: CategoryDTO[] = [];
  selectedCategories: number[] = [];

  constructor(private apiService: ApiService, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    // Fetch categories
    this.apiService.getCategories().subscribe((data) => {
      console.log('Requesting CategoryDTOs.');
      this.categories = data;
    });
  }

  // Handle checkbox selection
  onCheckboxChange = (event: any, categoryId: number): void => {
    if (!this.categoryForm) {
      console.error('categoryForm is NOT initialized at the time of checkbox change!');
      return;
    }

    console.log('Before updating form:', this.categoryForm.value);
    if (event.target.checked) {
      this.selectedCategories.push(categoryId);
    } else {
      this.selectedCategories = this.selectedCategories.filter(id => id !== categoryId);
    }

    this.categoryForm.patchValue({ idsOfInvolvedSectors: this.selectedCategories });
    console.log('After updating form:', this.categoryForm.value);
  };



  // Handle form submission
  onSubmit(): void {
    console.log('Form Submitted:', this.categoryForm.value);

    this.apiService.sendFormData(this.categoryForm);
  }
}

