package pizzashop.repository;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class PaymentRepositoryMock  implements IPaymentRepository{
    //private static String filename = "data/payments.txt";
    private List<Payment> paymentList;

    public PaymentRepositoryMock() {
        this.paymentList = null;
    }

    public void add(Payment payment) {
        if (paymentList==null)
            paymentList=new ArrayList<>();
        paymentList.add(payment);
    }

    public List<Payment> getAll() {

        return paymentList;
    }

    public void setUpEmptyList(){
        if (paymentList==null)
            paymentList=new ArrayList<>();
    }


}
