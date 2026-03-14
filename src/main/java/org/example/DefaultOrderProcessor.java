package org.example;

import org.example.domain.Order;
import org.example.domain.OrderItem;
import org.example.exeptions.CategoryMixException;
import org.example.exeptions.ValidationException;

import java.util.HashSet;
import java.util.Set;

public class DefaultOrderProcessor extends OrderProcessorTemplate {

    @Override
    protected void validate(Order order) {

        if(order.getItems().length < 2) {
            throw new ValidationException("Order must contain at least 2 items");
        }

    }

    @Override
    protected void validateCategoryMix(Order order) {

        Set<String> categories = new HashSet<>();

        for(OrderItem item : order.getItems()) {
            categories.add(item.getCategory());
        }

        if(categories.size() < 3) {
            throw new CategoryMixException("Need >=3 different categories");
        }
    }
}