package org.example;
import org.example.domain.Order;
import org.example.domain.OrderItem;
import org.example.payment.PaymentMethod;

import java.util.logging.Logger;

public abstract class OrderProcessorTemplate {

    private static final Logger log = Logger.getLogger(OrderProcessorTemplate.class.getName());

    public final void process(Order order, PaymentMethod paymentMethod) {

        log.info("Start processing order");

        validate(order);

        validateCategoryMix(order);

        double total = calculate(order);

        paymentMethod.pay(total);

        finish(order);

        log.info("Order processed successfully");
    }

    protected abstract void validate(Order order);

    protected abstract void validateCategoryMix(Order order);

    protected double calculate(Order order) {

        double sum = 0;

        for(OrderItem item : order.getItems()) {
            sum += item.getPrice().getAmount();
        }

        return sum;
    }

    protected void finish(Order order) {
        order.markPaid();
    }
}