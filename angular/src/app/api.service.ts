import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {FormGroup} from "@angular/forms";

interface Category {
  id: number;
  name: string;
  parentId: number;
}

export interface CategoryDTO {
  category: Category;
  subcategories?: CategoryDTO[];
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private BASE_ENDPOINT_URL = 'http://localhost:8080/rest/';

  constructor(private http: HttpClient) {
  }


  getCategories(): Observable<CategoryDTO[]> {
    console.log('Configuring subscription to CategoryDTOs');

    const getCategoriesEndpoint = this.BASE_ENDPOINT_URL + 'categories?isDetailed=true';
    const observable = this.http.get<CategoryDTO[]>(getCategoriesEndpoint);

    console.log('Returning observable: ', observable);
    return observable;
  }

  sendFormData(categoryForm: FormGroup) {
    if (!categoryForm) {
      console.error('Failed sending data to backend: Form is not initialized!');
      return;
    }

    const formData = categoryForm.value;
    console.log('Submitting form data:', formData);

    const sendFormDataEndpointUrl = this.BASE_ENDPOINT_URL + 'userData';
    this.http.post(sendFormDataEndpointUrl, formData).subscribe({
      next: response => console.log('Success:', response),
      error: error => console.error('Error:', error)
    });
  }
}
