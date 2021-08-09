package pizzashop.service;

import org.junit.jupiter.api.*;
import pizzashop.model.PaymentType;
import pizzashop.repository.IPaymentRepository;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepositoryMock;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) //1.void test_4() elimina _ si il inloc. cu " "
class PizzaServiceTest {

    private MenuRepository menuRepo;
    private IPaymentRepository payRepo;
    private PizzaService pizzaService;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        menuRepo = new MenuRepository();
        payRepo = new PaymentRepositoryMock();
        pizzaService = new PizzaService(menuRepo,payRepo);


    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void addPayment() throws Exception {
        addPayment_addSucced3();
        addPayment_addSucced4();
        addPayment_isTableLessThan1_addFailed();
    }

    @Test
    @DisplayName("Se va rula testul 1: ECP valid") //2
    void addPayment_addSucced() throws Exception {

        int size;
        if (pizzaService.getPayments() == null)
            size=0;
        else
            size = pizzaService.getPayments().size();

        pizzaService.addPayment(2, PaymentType.Cash,10);

        assertEquals(size+1,pizzaService.getPayments().size());
    }
    @Test
    @Order(2)
    void addPayment_addSucced2() throws Exception{

        int size2;
        if (pizzaService.getPayments() == null)
            size2=0;
        else
            size2 = pizzaService.getPayments().size();

        pizzaService.addPayment(6,PaymentType.Cash,10);

        assertEquals(size2+1,pizzaService.getPayments().size());
    }

    @Test
    @Order(1)
    void  addPayment_TableIsOver8_addFailed(){
        try {
            pizzaService.addPayment(9, PaymentType.Card, 10);
            fail();

        }catch (Exception e){
            assertEquals(e.getMessage(),PizzaService.tableError);
        }
    }

    @Test
    void addPayment_AmountIsUnder0_addFailed(){
        try {
            pizzaService.addPayment(2, PaymentType.Card, -10);
            fail();

        }catch (Exception e){
            assertEquals(e.getMessage(),PizzaService.amountError);
        }
    }

    void addPayment_addSucced3() throws Exception{
        int size3;
        if (pizzaService.getPayments() == null)
            size3=0;
        else
            size3 = pizzaService.getPayments().size();

        pizzaService.addPayment(1,PaymentType.Cash,10);

        assertEquals(size3+1,pizzaService.getPayments().size());

    }

    void addPayment_addSucced4() throws Exception{

        //setUp
        int size4 = pizzaService.getPayments().size();

        //act
        pizzaService.addPayment(1,PaymentType.Card,Integer.MAX_VALUE);

        //assert
        assertEquals(size4+1,pizzaService.getPayments().size());

    }

    //todo:denumire functii testare + constante la mesaje de eroare
    //used: MethodName_StateUnderTest_ExpectedBehavior
    void addPayment_isTableLessThan1_addFailed() throws Exception{
        try {
            pizzaService.addPayment(0, PaymentType.Cash, 20);
            fail();

        }catch (Exception e){
            assertEquals(e.getMessage(), PizzaService.tableError);
        }
    }

    @Test
    @Tag("testcuTag")
    void addPayment_amountIs0_addFailed() throws Exception{
        try {
            pizzaService.addPayment(2, PaymentType.Card, 0);
            fail();

        }catch (Exception e){
            assertEquals(e.getMessage(),PizzaService.amountError);
        }
    }

    @Test
    @Disabled("Disabled - identic cu alt test.") //3
    void addPayment_amountIs0_addFailed2() throws Exception{

        try {
            pizzaService.addPayment(2, PaymentType.Card, 0);
            fail();

        }catch (Exception e){
            assertEquals(e.getMessage(),"Error message: Amount must be > 0\n");
        }
    }



    //LAB WHITE BOX
    @Test
    void getTotalAmount_TC01nullList_amountIs0(){
        assertEquals(pizzaService.getTotalAmount(PaymentType.Cash),0);
    }

    @Test
    void getTotalAmount_TC02_amountIs1000() throws Exception {
        pizzaService.addPayment(1,PaymentType.Cash,1000d);
        assertEquals(pizzaService.getTotalAmount(PaymentType.Cash),1000);

    }
    @Test
    void getTotalAmount_TC03_amountIs1000() throws Exception {
        pizzaService.addPayment(1,PaymentType.Cash,1000d);
        assertEquals(pizzaService.getTotalAmount(PaymentType.Card),0d);
    }
    @Test
    void getTotalAmount_TC04EmptyList_amountIs0() throws Exception {
        payRepo.setUpEmptyList();
        assertEquals(pizzaService.getTotalAmount(PaymentType.Card),0d);
    }
    @Test
    void getTotalAmount_TC05_amountIs100() throws Exception {
        pizzaService.addPayment(1,PaymentType.Cash,100d);
        assertEquals(pizzaService.getTotalAmount(PaymentType.Cash),100d);
    }
    @Test
    void getTotalAmount_TC06_amountIs2100() throws Exception {
        pizzaService.addPayment(1,PaymentType.Card,100d);
        pizzaService.addPayment(1,PaymentType.Card,2000d);
        assertEquals(pizzaService.getTotalAmount(PaymentType.Card),2100d);
    }
}