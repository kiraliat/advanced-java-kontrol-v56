package org.example.domain;

import java.util.Arrays;

public class Order {
    private final String id;
    private OrderItem[] items;
    private OrderStatus status = OrderStatus.NEW;

    public Order(String id, OrderItem[] items) {
        this.id = id;
        this.items = Arrays.copyOf(items, items.length); // defensive copy
    }

    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markPaid() {
        if(status != OrderStatus.NEW) {
            throw new IllegalStateException("Order already paid");
        }
        status = OrderStatus.PAID;
    }
}
