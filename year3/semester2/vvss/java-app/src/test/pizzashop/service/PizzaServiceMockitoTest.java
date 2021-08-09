package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.IMenuRepository;
import pizzashop.repository.IPaymentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class PizzaServiceMockitoTest {
    private IMenuRepository menuRepo;
    private IPaymentRepository payRepo;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() {
        menuRepo = mock(IMenuRepository.class);
        payRepo = mock(IPaymentRepository.class);
        pizzaService = new PizzaService(menuRepo, payRepo);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPayments_empty() {
        Mockito.when(payRepo.getAll()).thenReturn(new ArrayList<>());
        assertEquals(0, pizzaService.getPayments().size());
        Mockito.verify(payRepo, times(1)).getAll();
    }

    @Test
    void addPayment_succeed() throws Exception {
        Payment payment = new Payment(1, PaymentType.Cash, 100);
        Mockito.when(payRepo.getAll()).thenReturn(Collections.singletonList(payment));
        pizzaService.addPayment(1, PaymentType.Cash, 100);

        assertEquals(1, pizzaService.getPayments().size());
    }

    @Test
    void getMenuData_oneElement() {
        MenuDataModel menuDataModel = new MenuDataModel("item", 1, (double) 20);
        Mockito.when(menuRepo.getMenu()).thenReturn(Collections.singletonList(menuDataModel));

        assertEquals(1, pizzaService.getMenuData().size());

        Mockito.verify(menuRepo, times(1)).getMenu();
    }

    @Test
    void getTotalAmount_succeed() {
        Payment payment = new Payment(1, PaymentType.Cash, 100);
        Payment payment2 = new Payment(1, PaymentType.Card, 200);
        Mockito.when(payRepo.getAll()).thenReturn(Arrays.asList(payment, payment2));

        assertEquals(100, pizzaService.getTotalAmount(PaymentType.Cash));

        Mockito.verify(payRepo, times(1)).getAll();
    }
}