using log4net;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TripCommon.domain;
using TripCommon.Repository;
using TripCommon.Validator;
using TripCommon.Validator;

namespace TripsServer.Service
{
    class TripServicesImpl : ITripServices
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("TripServicesImpl");

        private AccountRepository accountRepository;
        private TripRepository tripRepository;
        private BookingRepository bookingRepository;
        private AccountValidator accountValidator;
        private TripValidator tripValidator;
        private BookingValidator bookingValidator;
        List<ITripObserver> tripObservers = new List<ITripObserver>();

        public TripServicesImpl(AccountRepository accountRepository, TripRepository tripRepository, BookingRepository bookingRepository, AccountValidator accountValidator, TripValidator tripValidator, BookingValidator bookingValidator)
        {
            this.accountRepository = accountRepository;
            this.tripRepository = tripRepository;
            this.bookingRepository = bookingRepository;
            this.accountValidator = accountValidator;
            this.tripValidator = tripValidator;
            this.bookingValidator = bookingValidator;
        }

        public void AddBooking(string client, string phone, int numTickets, Trip trip, Account account)
        {
            LOGGER.Info("adding booking");
            if (trip.AvailablePlaces < numTickets)
            {
                throw new ValidationException("Not enough places available");
            }
            Booking toSave = new Booking(-1, trip.Id, account.Id, client, phone, numTickets);
            bookingValidator.Validate(toSave);
            Booking b = bookingRepository.Save(toSave);
            trip.AvailablePlaces = trip.AvailablePlaces - numTickets;
            tripRepository.Save(trip);
            LOGGER.Info("Notifying observers");
            Task.Run(() =>
            {
                tripObservers.ForEach(o =>
                {
                    LOGGER.InfoFormat("notifying {0}", o);
                    o.BookingInserted(b);
                });
            });
        }



        public Account FindAccountByNameAndPassword(string name, string password)
        {
            LOGGER.InfoFormat("finding account by name {0} and password {1}", name, password);
            return accountRepository.FindByNamePassword(name, password);
        }

        public List<Trip> GetAllTrips()
        {
            return tripRepository.FindAll().ToList();
        }

        public List<Trip> GetTripsByLandmarkDepartureHour(string landmark, int start, int end)
        {
            return tripRepository.FindByLandmarkDepartureHour(landmark, start, end).ToList();
        }

        public void Logout()
        {

        }
        public void AddTripObserver(ITripObserver observer)
        {
            tripObservers.Add(observer);
        }
        public void RemoveTripObserver(ITripObserver observer)
        {
            tripObservers.Remove(observer);
        }
    }
}
