import { Component, OnInit } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
import { ApiService, CategoryDTO } from './api.service';
import {JsonPipe, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormControl, ReactiveFormsModule} from '@angular/forms';
import {CategoryComponent} from "./category/category.component";


@Component({
  selector: 'app-root',
  standalone: true,
  // imports: [RouterOutlet, JsonPipe, NgForOf, NgIf, ReactiveFormsModule],
  imports: [NgForOf, NgIf, ReactiveFormsModule, JsonPipe, CategoryComponent],
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
    // Initialize form
    // console.log("Initializing categoryForm...");
    //
    // setTimeout(() => {
    //   this.categoryForm = this.formBuilder.group({
    //     name: [''],
    //     idsOfInvolvedSectors: [[]],
    //     agreeToTerms: [false],
    //   });
    // })
    //
    // console.log("categoryForm initialized:", this.categoryForm);

    // Fetch categories
    this.apiService.getCategories().subscribe((data) => {
      console.log('Requesting CategoryDTOs.');
      this.categories = data;
    });
  }

  // Handle checkbox selection
  // onCheckboxChange(event: any, categoryId: number): void {
  //   console.log('Checkbox changed', categoryId);
  //
  //   // Debug: Check if categoryForm is defined
  //   if (!this.categoryForm) {
  //     console.error('categoryForm is NOT initialized at the time of checkbox change!');
  //     return;
  //   }
  //
  //   if (event.target.checked) {
  //     // Add categoryId to list of checked IDs
  //     this.selectedCategories.push(categoryId);
  //   } else {
  //     // Remove categoryId from list of checked IDs
  //     this.selectedCategories = this.selectedCategories.filter(id => id !== categoryId);
  //   }
  //
  //   if (!this.categoryForm) {
  //     console.error('categoryForm is not initialized yet.');
  //     return;
  //   }
  //   this.categoryForm.patchValue({ idsOfInvolvedSectors: this.selectedCategories });
  // }
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


  onAgreeToTermsCheckboxChange = (event: any): void => {
    //
  }

  // Handle form submission
  onSubmit(): void {
    console.log('Form Submitted:', this.categoryForm.value);

    this.apiService.sendFormData(this.categoryForm);
  }
}

