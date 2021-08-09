package pizzashop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    @Test
    void getTableNumber_succeed() throws Exception {
        Payment payment = new Payment(1, PaymentType.Card, 100d);
        assertEquals(1, payment.getTableNumber());
    }

    @Test
    void getPaymentType_succeed() throws Exception {
        Payment payment = new Payment(1, PaymentType.Card, 100d);
        assertEquals(PaymentType.Card, payment.getType());
    }

    @Test
    void setPaymentType_succeed() throws Exception {
        Payment payment = new Payment(1, PaymentType.Card, 100d);
        payment.setType(PaymentType.Cash);
        assertEquals(PaymentType.Cash, payment.getType());
    }
    @Test
    void setAmount_succeed() throws Exception {
        Payment payment = new Payment(1, PaymentType.Card, 100d);
        payment.setAmount(300);
        assertEquals(300, payment.getAmount());
    }
}