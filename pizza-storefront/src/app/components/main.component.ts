import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Order } from '../models';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PizzaToppings: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  pizzaSize = SIZES[0]

    orderForm!: FormGroup

  constructor(private fb: FormBuilder, private router: Router, private pizzaSvc: PizzaService) {}

  selectedToppings: string[] = []

  ngOnInit(): void {
    this.orderForm = this.fb.group({
        name: this.fb.control('', [Validators.required, Validators.pattern('[a-z|A-Z| ]+')]),
        email: this.fb.control('', [Validators.required, Validators.email]),
        size: this.fb.control(0, [Validators.required]),
        base: this.fb.control('thin', [Validators.required]),
        sauce: this.fb.control('classic', [Validators.required]),
        toppings: this.fb.control(this.selectedToppings),
        comments: this.fb.control('')
    })
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  placeOrder() {
    const formData = this.orderForm.value as Order
    console.info("Form data", formData)
    this.pizzaSvc.createOrder(formData)
        .then(result => {
            console.info("Result >>", result)
            this.router.navigate(['/orders/' + this.orderForm.value.email])
        })
        .catch(error => {
            console.error("Error >>", error)
        })
  }

  viewOrderDetails() {
    this.router.navigate(['orders/' + this.orderForm.value.email])
  }

  addToToppings(toppingName: string) {
    this.selectedToppings.push(toppingName)
  }
}
