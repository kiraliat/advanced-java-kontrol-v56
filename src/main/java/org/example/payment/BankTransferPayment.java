package org.example.payment;

public class BankTransferPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {

        double finalAmount = amount * 1.015;

        System.out.println("Charged with commission: " + finalAmount);
    }
}
