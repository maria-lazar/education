package pizzashop.repository;

import pizzashop.model.Payment;

import java.util.List;

public interface IPaymentRepository {
    public void add(Payment payment);

    public List<Payment> getAll();

    public void setUpEmptyList();
}
