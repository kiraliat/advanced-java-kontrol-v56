package org.example.payment;

import org.example.exeptions.PaymentException;

public interface PaymentMethod {

    void pay(double amount) throws PaymentException;

}
