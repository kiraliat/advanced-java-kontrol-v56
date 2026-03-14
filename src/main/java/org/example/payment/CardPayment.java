package org.example.payment;

import org.example.exeptions.PaymentException;

public class CardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        if(amount > 25000) {
            throw new PaymentException("Card limit exceeded");
        }
    }
}
