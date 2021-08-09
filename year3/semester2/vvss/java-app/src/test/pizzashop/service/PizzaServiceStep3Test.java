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
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class PizzaServiceStep3Test {
    private IMenuRepository menuRepo;
    private IPaymentRepository payRepo;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() {
        menuRepo = new MenuRepository();
        payRepo = new PaymentRepository();
        pizzaService = new PizzaService(menuRepo, payRepo);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void addPayment_valid() throws Exception {
        int size = pizzaService.getPayments().size();

        pizzaService.addPayment(1, PaymentType.Cash, 100);

        assertEquals(size + 1, pizzaService.getPayments().size());
    }

    @Test
    void addPayment_invalidTable() {
        Exception exception = assertThrows(Exception.class, () -> pizzaService.addPayment(10, PaymentType.Cash, 100));

        assertEquals(PizzaService.tableError, exception.getMessage());
    }

    @Test
    void addPayment_invalidAmount() {
        Exception exception = assertThrows(Exception.class, () -> pizzaService.addPayment(1, PaymentType.Cash, -100));

        assertEquals(PizzaService.amountError, exception.getMessage());
    }

    @Test
    void getPayments_succeed() throws Exception {
        int size = pizzaService.getPayments().size();

        pizzaService.addPayment(1, PaymentType.Cash, 100);
        pizzaService.addPayment(2, PaymentType.Cash, 1001);

        assertEquals(size + 2, pizzaService.getPayments().size());
    }
}