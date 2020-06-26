
using TripCommon.domain;

namespace TripCommon.Validator
{
    public class BookingValidator : IValidator<Booking>
    {
        public void Validate(Booking entity)
        {
            if (entity.NumTickets <= 0)
            {
                throw new ValidationException("Invalid number of tickets");
            }
        }
    }
}
