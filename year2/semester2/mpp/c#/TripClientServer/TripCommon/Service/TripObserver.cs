using TripCommon.domain;

namespace TripCommon.Validator
{
    public interface ITripObserver
    {
        void BookingInserted(Booking booking);
    }
}
