using System;
using System.Collections.Generic;
using System.Linq;
using TripCommon.domain;
using TripCommon.Dto;
using TripCommon.Net;
using TripCommon.Validator;

namespace TripClient.Service
{
    class TripServicesProxy : BaseServicesProxy, ITripServices
    {
        protected ITripObserver tripObserver;
        public TripServicesProxy(string host, int port) : base(host, port)
        {
        }

        public void AddBooking(string client, string phone, int numTickets, Trip trip, Account account)
        {
            LOGGER.InfoFormat("adding booking for client {0}  fo trip {1} tickets {2}", client, trip, numTickets);
            EnsureConnected();
            TripDto t = new TripDto(trip.Id, trip.Landmark, trip.CompanyName, trip.DepartureTime, trip.Price, trip.AvailablePlaces);
            AccountDto a = new AccountDto(account.Id, account.Name, account.Password);
            Request r = new Request()
            {
                Type = RequestType.ADD_BOOKING,
                Data = new BookingDto(client, phone, numTickets, t, a)
            };
            SendRequest(r);
            Response response = ReadResponse();
            LOGGER.InfoFormat("response for add booking {0}", response);
            if (response.Type == ResponseType.OK)
            {
                LOGGER.Info("booking added");
                return;
            }
            if (response.Type == ResponseType.ERROR)
            {
                String err = response.Data.ToString();
                LOGGER.Info("received ERROR response " + err);
                throw new ServiceException(err);
            }
        }



        public Account FindAccountByNameAndPassword(string name, string password)
        {
            LOGGER.InfoFormat("finding account with name {0} password {1}", name, password);
            EnsureConnected();
            Request r = new Request()
            {
                Type = RequestType.LOGIN,
                Data = new AccountDto(name, password)
            };
            SendRequest(r);
            Response response = ReadResponse();
            LOGGER.InfoFormat("response for find account received is {0}", response);
            if (response.Type == ResponseType.OK)
            {
                AccountDto accountDto = (AccountDto)response.Data;
                if (accountDto == null)
                {
                    LOGGER.Info("No account found");
                    return null;
                }
                Account account = new Account(accountDto.id, accountDto.name, accountDto.password);
                LOGGER.InfoFormat("found account {0}", account);
                return account;
            }
            if (response.Type == ResponseType.ERROR)
            {
                String err = response.Data.ToString();
                LOGGER.Info("finding account failed received ERROR response " + err);
                throw new ServiceException(err);
            }
            return null;
        }


        public List<Trip> GetAllTrips()
        {
            LOGGER.InfoFormat("getting all trips");
            EnsureConnected();
            Request r = new Request()
            {
                Type = RequestType.GET_ALL_TRIPS
            };
            SendRequest(r);
            Response response = ReadResponse();
            LOGGER.InfoFormat("response for get all trips received is {0}", response);
            if (response.Type == ResponseType.OK)
            {
                List<TripDto> tripDtos = (List<TripDto>)response.Data;
                List<Trip> trips = tripDtos
                    .Select(t => new Trip(t.id, t.landmark, t.companyName, t.departure, t.price, t.places))
                    .ToList();
                LOGGER.Info("found all trips");
                return trips;
            }
            if (response.Type == ResponseType.ERROR)
            {
                String err = response.Data.ToString();
                LOGGER.Info("getting all trips failed ERROR response " + err);
                throw new ServiceException(err);
            }
            return null;
        }

        public List<Trip> GetTripsByLandmarkDepartureHour(string landmark, int start, int end)
        {
            LOGGER.InfoFormat("getting trips by landmark {0} departure hours {1} {2}", landmark, start, end);
            EnsureConnected();
            Request r = new Request()
            {
                Type = RequestType.SEARCH_TRIPS,
                Data = new TripDto(landmark, start, end)
            };
            SendRequest(r);
            Response response = ReadResponse();
            LOGGER.InfoFormat("response for search trips received is {0}", response);
            if (response.Type == ResponseType.OK)
            {
                List<TripDto> tripDtos = (List<TripDto>)response.Data;
                List<Trip> trips = tripDtos
                    .Select(t => new Trip(t.id, t.landmark, t.companyName, t.departure, t.price, t.places))
                    .ToList();
                LOGGER.Info("found all trips");
                return trips;
            }
            if (response.Type == ResponseType.ERROR)
            {
                String err = response.Data.ToString();
                LOGGER.Info("searching trips failed ERROR response " + err);
                throw new ServiceException(err);
            }
            return null;
        }

        public void Logout()
        {
            LOGGER.Info("logging out");
            if (connection == null)
            {
                LOGGER.Info("connection already closed");
                return;
            }
            Request r = new Request()
            {
                Type = RequestType.LOGOUT
            };
            SendRequest(r);
            Response response = ReadResponse();
            CloseConnection();
            if (response.Type == ResponseType.ERROR)
            {
                String err = response.Data.ToString();
                LOGGER.Warn("logging out failed " + err);
                throw new ServiceException(err);
            }
        }
        public void AddTripObserver(ITripObserver observer)
        {
            tripObserver = observer;
        }
        public void RemoveTripObserver(ITripObserver observer)
        {
            tripObserver = null;
        }

        public override void HandleBookingAdded(Response response)
        {
            LOGGER.InfoFormat("handling booking added response {0}", response);
            BookingDto bookingDto = (BookingDto)response.Data;
            Booking booking = new Booking(bookingDto.id, bookingDto.trip.id, bookingDto.account.id, bookingDto.client
                , bookingDto.phone, bookingDto.numTickets);
            tripObserver.BookingInserted(booking);
        }
    }
}
