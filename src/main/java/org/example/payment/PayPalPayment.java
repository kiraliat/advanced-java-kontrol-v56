package org.example.payment;

import org.example.exeptions.PaymentException;

public class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        if(amount < 200) {
            throw new PaymentException("PayPal minimum is 200");
        }
    }
}
