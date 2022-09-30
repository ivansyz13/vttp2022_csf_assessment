// Implement the methods in PizzaService for Task 3
// Add appropriate parameter and return type 
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Order } from './models';

@Injectable()
export class PizzaService {

  constructor(private http: HttpClient) { }

  // POST /api/order
  // Add any required parameters or return type
  createOrder(formData: Order) {
    const headers = new HttpHeaders()
        .set("Content-type", "application/json")
        .set("Accept", "application/json")
    
    return firstValueFrom(
        this.http.post('/api/order', formData, { headers })
    )
  }

  // GET /api/order/<email>/all
  // Add any required parameters or return type
  getOrders(email: string) { 
    return firstValueFrom(
        this.http.get('/api/order/' + email + '/all')
    )
  }
}
