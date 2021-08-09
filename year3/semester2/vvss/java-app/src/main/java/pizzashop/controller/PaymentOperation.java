package pizzashop.controller;

public interface PaymentOperation {
     void cardPayment();
     void cashPayment();
     void cancelPayment();
}
