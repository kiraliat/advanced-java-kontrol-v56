import org.example.*;
import org.example.domain.Money;
import org.example.domain.Order;
import org.example.domain.OrderItem;
import org.example.domain.OrderStatus;
import org.example.exeptions.CategoryMixException;
import org.example.exeptions.PaymentException;
import org.example.exeptions.ValidationException;
import org.example.payment.BankTransferPayment;
import org.example.payment.CardPayment;
import org.example.payment.PayPalPayment;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.*;

public class OrderProcessorTest {

    private DefaultOrderProcessor processor = new DefaultOrderProcessor();

    private Order createValidOrder() {

        OrderItem[] items = {
                new OrderItem("item1","food", new Money(200)),
                new OrderItem("item2","tech", new Money(300)),
                new OrderItem("item3","books", new Money(400))
        };

        return new Order("1", items);
    }

    @Test
    @DisplayName("Successful card payment")
    public void shouldProcessCardPayment() {

        Order order = createValidOrder();

        processor.process(order, new CardPayment());

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    @DisplayName("Successful PayPal payment")
    public void shouldProcessPaypalPayment() {

        Order order = createValidOrder();

        processor.process(order, new PayPalPayment());

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    @DisplayName("Successful bank transfer")
    public void shouldProcessBankTransfer() {

        Order order = createValidOrder();

        processor.process(order, new BankTransferPayment());

        assertEquals(OrderStatus.PAID, order.getStatus());
    }


    @Test
    @DisplayName("Should fail when order has less than 2 items")
    public void shouldThrowValidationException() {

        OrderItem[] items = {
                new OrderItem("item1","food", new Money(200))
        };

        Order order = new Order("1", items);

        assertThrows(ValidationException.class, () ->
                processor.process(order, new CardPayment())
        );
    }

    @Test
    @DisplayName("Should throw CategoryMixException when categories < 3")
    public void shouldThrowCategoryMixException() {

        OrderItem[] items = {
                new OrderItem("item1","food", new Money(200)),
                new OrderItem("item2","food", new Money(300))
        };

        Order order = new Order("1", items);

        assertThrows(CategoryMixException.class, () ->
                processor.process(order, new CardPayment())
        );
    }

    @Test
    @DisplayName("Card payment should fail when amount > 25000")
    public void shouldFailCardLimit() {

        OrderItem[] items = {
                new OrderItem("item1","a", new Money(20000)),
                new OrderItem("item2","b", new Money(20000)),
                new OrderItem("item3","c", new Money(20000))
        };

        Order order = new Order("1", items);

        assertThrows(PaymentException.class, () ->
                processor.process(order, new CardPayment())
        );
    }

    @Test
    @DisplayName("PayPal should fail when amount < 200")
    public void shouldFailPaypalMinimum() {

        OrderItem[] items = {
                new OrderItem("item1","a", new Money(50)),
                new OrderItem("item2","b", new Money(50)),
                new OrderItem("item3","c", new Money(50))
        };

        Order order = new Order("1", items);

        assertThrows(PaymentException.class, () ->
                processor.process(order, new PayPalPayment())
        );
    }

    @Test
    @DisplayName("Order cannot be paid twice")
    public void shouldNotAllowDoublePayment() {

        Order order = createValidOrder();

        processor.process(order, new CardPayment());

        assertThrows(IllegalStateException.class, () ->
                processor.process(order, new CardPayment())
        );
    }


    @ParameterizedTest
    @ValueSource(doubles = {500, 1000, 5000})
    @DisplayName("Card payment should work for valid amounts")
    void cardPaymentParameterized(double price) {

        OrderItem[] items = {
                new OrderItem("item1","a", new Money(price)),
                new OrderItem("item2","b", new Money(price)),
                new OrderItem("item3","c", new Money(price))
        };

        Order order = new Order("1", items);

        processor.process(order, new CardPayment());

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    @DisplayName("Money equals should work")
    public void moneyEqualsTest() {

        Money m1 = new Money(100);
        Money m2 = new Money(100);

        assertEquals(m1, m2);
    }

    @Test
    @DisplayName("Order items defensive copy")
    public void defensiveCopyTest() {

        OrderItem[] items = {
                new OrderItem("item1","a", new Money(100)),
                new OrderItem("item2","b", new Money(200)),
                new OrderItem("item3","c", new Money(300))
        };

        Order order = new Order("1", items);

        OrderItem[] returned = order.getItems();

        returned[0] = null;

        assertNotNull(order.getItems()[0]);
    }
}