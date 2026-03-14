package org.example;

import org.example.domain.Money;
import org.example.domain.Order;
import org.example.domain.OrderItem;
import org.example.exeptions.AppException;
import org.example.exeptions.CategoryMixException;
import org.example.exeptions.PaymentException;
import org.example.exeptions.ValidationException;
import org.example.payment.CardPayment;
import org.example.payment.PayPalPayment;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        OrderItem[] items = {
                new OrderItem("Laptop", "Tech", new Money(1500)),
                new OrderItem("Headphones", "Tech", new Money(200)),
                new OrderItem("Book", "Books", new Money(50))
        };

        Order order = new Order("ORD-001", items);

        OrderProcessorTemplate processor = new DefaultOrderProcessor();

        try {
            System.out.println("=== Оплата карткою ===");
            processor.process(order, new CardPayment());
            System.out.println("Статус замовлення: " + order.getStatus());
        } catch (AppException e) {
            System.out.println("Помилка оплати: " + e.getMessage());
        }

        // Створимо нове замовлення для PayPal
        Order paypalOrder = new Order("ORD-002", items);

        try {
            System.out.println("\n=== Оплата PayPal ===");
            processor.process(paypalOrder, new PayPalPayment());
            System.out.println("Статус замовлення: " + paypalOrder.getStatus());
        } catch (AppException e) {
            System.out.println("Помилка оплати: " + e.getMessage());
        }

        // Приклад негативного сценарію — замовлення з 2 товарами однієї категорії
        Order badOrder = new Order("ORD-003", new OrderItem[]{
                new OrderItem("Item1", "Food", new Money(100)),
                new OrderItem("Item2", "Food", new Money(200))
        });

        try {
            System.out.println("\n=== Оплата замовлення з поганою категорією ===");
            processor.process(badOrder, new CardPayment());
        } catch (CategoryMixException e) {
            System.out.println("CategoryMixException: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("ValidationException: " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("PaymentException: " + e.getMessage());
        }

    }
}