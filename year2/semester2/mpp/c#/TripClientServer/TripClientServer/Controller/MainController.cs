using log4net;
using System;
using System.Collections.Generic;
using TripClientServer.Controller;
using TripCommon.domain;
using TripCommon.Validator;

namespace TripClient.Controller
{
    class MainController: ITripObserver
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("MainController");
        public event EventHandler<TripEventArgs> UpdateEvent;

        private ITripServices tripServices;

        public Account Account { get; internal set; }

        public MainController(ITripServices tripService)
        {
            this.tripServices = tripService;
            this.tripServices.AddTripObserver(this);
        }

        public List<Trip> GetTrips()
        {
            LOGGER.Info("getting all trips");
            return tripServices.GetAllTrips();
        }

        internal List<Trip> SearchTrips(string landmark, int start, int end)
        {
            LOGGER.Info("searching trips");
            if ((start >= end) || (start < 0) || (start > 23) || (end < 0) || (end > 24))
            {
                throw new ValidationException("Invalid hours");
            }
            return tripServices.GetTripsByLandmarkDepartureHour(landmark, start, end);
        }

        internal void AddBooking(string client, string phone, int numTickets, Trip trip)
        {
            LOGGER.Info("adding booking");
            tripServices.AddBooking(client, phone, numTickets, trip, Account);
        }

        internal void Logout()
        {
            LOGGER.Info("logging out");
            tripServices.Logout();
            tripServices.RemoveTripObserver(this);
        }

        public void BookingInserted(Booking booking)
        {
            LOGGER.InfoFormat("booking inserted {0}", booking);
            TripEventArgs tripEventArgs = new TripEventArgs(TripEvent.BookingAdded, booking);
            OnTripEvent(tripEventArgs);
        }

        private void OnTripEvent(TripEventArgs tripEventArgs)
        {
            if (UpdateEvent == null)
            {
                return;
            }
            UpdateEvent(this, tripEventArgs);
            LOGGER.Info("Update Event called");
        }
    }
}
