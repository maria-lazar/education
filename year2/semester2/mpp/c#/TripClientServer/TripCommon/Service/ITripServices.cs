using System.Collections.Generic;
using TripCommon.domain;

namespace TripCommon.Validator
{
    public interface ITripServices
    {
        Account FindAccountByNameAndPassword(string name, string password);

        void AddBooking(string client, string phone, int numTickets, Trip trip, Account account);

        List<Trip> GetAllTrips();

        List<Trip> GetTripsByLandmarkDepartureHour(string landmark, int start, int end);

        void Logout();
        void AddTripObserver(ITripObserver observer);

        void RemoveTripObserver(ITripObserver observer);
    }
}
