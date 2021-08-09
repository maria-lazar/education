package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PizzaServiceStep2Test {
    private PaymentRepository paymentRepository;
    private MenuRepository menuRepository;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        menuRepository = new MenuRepository();
        pizzaService = new PizzaService(menuRepository, paymentRepository);
    }

    @Test
    void add_succeed() throws Exception {
        int size = paymentRepository.getAll().size();
        Payment p = mock(Payment.class);
        Mockito.when(p.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p.getAmount()).thenReturn(100d);
        Mockito.when(p.getTableNumber()).thenReturn(1);

        pizzaService.addPayment(p.getTableNumber(), p.getType(), p.getAmount());

        assertEquals(PaymentType.Cash, paymentRepository.getAll().get(size).getType());
    }

    @Test
    void add_invalid() throws Exception {
        Payment p = mock(Payment.class);
        Mockito.when(p.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(p.getAmount()).thenReturn(90d);
        Mockito.when(p.getTableNumber()).thenReturn(10);

        Exception exception = assertThrows(Exception.class, () -> pizzaService.addPayment(p.getTableNumber(), p.getType(), p.getAmount()));

        assertEquals(PizzaService.tableError, exception.getMessage());
    }
}