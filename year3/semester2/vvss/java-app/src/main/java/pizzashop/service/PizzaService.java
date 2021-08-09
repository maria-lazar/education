package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.IMenuRepository;
import pizzashop.repository.IPaymentRepository;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

public class PizzaService {
    private IMenuRepository menuRepo;
    private IPaymentRepository payRepo;

    public PizzaService(IMenuRepository menuRepo, IPaymentRepository payRepo){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
    }

    public List<MenuDataModel> getMenuData(){return menuRepo.getMenu();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public static final String tableError = "Error message: Invalid table\n";
    public static final String amountError = "Error message: Amount must be > 0\n";

    public void addPayment(int table, PaymentType type, double amount) throws Exception {
        if (table < 1 || table > 8)
            throw new Exception(tableError);
        if (amount <= 0 )
            throw new Exception(amountError);
        Payment payment= new Payment(table, type, amount);
        payRepo.add(payment);
    }

    /*
    public double getTotalAmount(PaymentType type){
        double total=0.0f;
        List<Payment> l=getPayments();
        if ((l==null) ||(l.isEmpty()))
            return total;
        for (Payment p:l){
            if (p.getType().equals(type))
                total+=p.getAmount();
        }
        return total;
    }
     */
    /**
     * Get total amount by payment type
     *
     * @param type PaymentType
     * @return double
     */
    public double getTotalAmount(PaymentType type) {
        double total = 0.0f;
        List<Payment> l = getPayments();
        if ((l == null) || (l.isEmpty())) return total;
        int i = 0;
        while (i < l.size()) {
            Payment p = l.get(i);
//        for (Payment p:l){
            if (p.getType().equals(type))
                total += p.getAmount();
            i = i + 1;
        }
        if (total >= 1000)
            System.out.println("Profit");
        return total;
    }

}
