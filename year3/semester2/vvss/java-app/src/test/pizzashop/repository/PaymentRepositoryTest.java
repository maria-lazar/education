package pizzashop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    @Test
    void add_succeed_Mockito() {
        int size = paymentRepository.getAll().size();
        Payment p = mock(Payment.class);
        Mockito.when(p.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p.getAmount()).thenReturn(100d);
        Mockito.when(p.getTableNumber()).thenReturn(1);

        paymentRepository.add(p);

        assertEquals(PaymentType.Cash, paymentRepository.getAll().get(size).getType());
    }

    @Test
    void add_succeed() {
        int size = paymentRepository.getAll().size();

        paymentRepository.add(new Payment(1, PaymentType.Cash, 90));

        assertEquals(PaymentType.Cash, paymentRepository.getAll().get(size).getType());
    }

    @Test
    void getAll_succeed_Mockito() {
        int size = paymentRepository.getAll().size();
        Payment p = mock(Payment.class);
        Mockito.when(p.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p.getAmount()).thenReturn(90d);
        Mockito.when(p.getTableNumber()).thenReturn(1);
        paymentRepository.add(p);
        Payment p2 = mock(Payment.class);
        Mockito.when(p2.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p2.getAmount()).thenReturn(100d);
        Mockito.when(p2.getTableNumber()).thenReturn(7);
        paymentRepository.add(p2);

        assertEquals(100d, paymentRepository.getAll().get(size + 1).getAmount());
    }
    @Test
    void getAll_succeed() {
        int size = paymentRepository.getAll().size();
        Payment p = new Payment(4, PaymentType.Cash, 50d);
        Payment p2 = new Payment(1, PaymentType.Card, 150d);
        paymentRepository.add(p);
        paymentRepository.add(p2);

        assertEquals(150d, paymentRepository.getAll().get(size + 1).getAmount());
    }
}