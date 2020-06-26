package validator;


import domain.Booking;

public class BookingValidator implements Validator<Booking> {
    @Override
    public void validate(Booking entity) {
        if (entity.getNumTickets() <= 0){
            throw new ValidationException("Invalid number of tickets");
        }
    }
}
