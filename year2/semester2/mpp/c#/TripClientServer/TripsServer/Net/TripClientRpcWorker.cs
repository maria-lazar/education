using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using TripCommon.domain;
using TripCommon.Dto;
using TripCommon.Net;
using TripCommon.Validator;

namespace TripsServer.Net
{
    class TripClientRpcWorker: ITripObserver
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("TripClientRpcWorker");

        private ITripServices tripServices;
        private TcpClient connection;

        private NetworkStream stream;
        private IFormatter formatter;
        private volatile bool connected;
        public TripClientRpcWorker(ITripServices tripServices, TcpClient connection)
        {
            LOGGER.Info("initializing worker");
            this.tripServices = tripServices;
            this.connection = connection;
            try
            {
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                connected = true;
            }
            catch (Exception e)
            {
                LOGGER.Warn("initializing worker failed" + e);
                Console.WriteLine(e.StackTrace);
            }
        }

        public void BookingInserted(Booking booking)
        {
            LOGGER.InfoFormat("booking inserted {0}", booking);
            BookingDto bookingDto = new BookingDto(booking.Id, booking.ClientName, booking.PhoneNumber, booking.NumTickets,
                new TripDto(booking.TripId), new AccountDto(booking.AccountId));
            Response response = new Response()
            {
                Type = ResponseType.BOOKING_ADDED,
                Data = bookingDto
            };
            try
            {
                SendResponse(response);
            }
            catch(Exception e)
            {
                LOGGER.Warn("sending response booking inserted failed");
                LOGGER.Warn(e.StackTrace);
            }
        }

        public void Run()
        {
            LOGGER.Info("running worker");
            while (connected)
            {
                try
                {
                    Request request = (Request)formatter.Deserialize(stream);
                    LOGGER.InfoFormat("received request {0}", request);
                    Response response = HandleRequest(request);
                    LOGGER.InfoFormat("response for request {0}", response);
                    if (response != null)
                    {
                        LOGGER.InfoFormat("sending response to client {0}", response);
                        SendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    if (connected)
                    {
                        LOGGER.Warn("running worker stopped with exception {0}", e);
                        LOGGER.Warn(e.StackTrace);
                    }
                    break;
                }
            }
            LOGGER.Info("closing connection");
            try
            {
                stream.Close();
                connection.Close();
            }
            catch (Exception e)
            {
                LOGGER.Warn("Error closing connection" + e);
                LOGGER.Warn(e.StackTrace);
            }
        }
        private Response HandleRequest(Request request)
        {
            LOGGER.Info("handling request " + request);
            if (request.Type == RequestType.LOGIN)
            {
                LOGGER.Info("Login request");
                AccountDto accountDto = (AccountDto)request.Data;
                Account account = null;
                try
                {
                    lock (tripServices)
                    {
                        account = tripServices.FindAccountByNameAndPassword(accountDto.name, accountDto.password);
                    }
                    if (account == null)
                    {
                        return new Response()
                        {
                            Type = ResponseType.OK
                        };
                    }
                    tripServices.AddTripObserver(this);
                    return new Response()
                    {
                        Type = ResponseType.OK,
                        Data = new AccountDto(account.Id, account.Name, account.Password)
                    };
                }
                catch (Exception e)
                {
                    LOGGER.Warn("handling login failed ");
                    return new Response()
                    {
                        Type = ResponseType.ERROR,
                        Data = e.Message
                    };
                }
            }
            if (request.Type == RequestType.LOGOUT)
            {
                LOGGER.Info("Logout request");
                lock (tripServices)
                {
                    tripServices.Logout();
                }
                tripServices.RemoveTripObserver(this);
                connected = false;
                return new Response()
                {
                    Type = ResponseType.OK
                };
            }
            if (request.Type == RequestType.GET_ALL_TRIPS)
            {
                LOGGER.Info("get all trips request");
                List<Trip> trips = null;
                try
                {
                    lock (tripServices)
                    {
                        trips = tripServices.GetAllTrips();
                    }
                    return new Response()
                    {
                        Type = ResponseType.OK,
                        Data = trips
                                    .Select(t => new TripDto(t.Id, t.Landmark, t.CompanyName, t.DepartureTime, t.Price, t.AvailablePlaces))
                                    .ToList()
                    };
                }
                catch (Exception e)
                {
                    LOGGER.Warn("getting all trips failed ");
                    return new Response()
                    {
                        Type = ResponseType.ERROR,
                        Data = e.Message
                    };
                }
            }
            if (request.Type == RequestType.SEARCH_TRIPS)
            {
                LOGGER.Info("search trips request");
                List<Trip> trips = null;
                try
                {
                    TripDto tripDto = (TripDto)request.Data;
                    lock (tripServices)
                    {
                        trips = tripServices.GetTripsByLandmarkDepartureHour(tripDto.landmark, tripDto.start, tripDto.end);
                    }
                    LOGGER.Info("returning response with searched trips");
                    return new Response()
                    {
                        Type = ResponseType.OK,
                        Data = trips
                                    .Select(t => new TripDto(t.Id, t.Landmark, t.CompanyName, t.DepartureTime, t.Price, t.AvailablePlaces))
                                    .ToList()
                    };
                }
                catch (Exception e)
                {
                    LOGGER.Warn("searching trips failed ");
                    return new Response()
                    {
                        Type = ResponseType.ERROR,
                        Data = e.Message
                    };
                }
            }
            if (request.Type == RequestType.ADD_BOOKING)
            {
                LOGGER.Info("add booking request");
                BookingDto bookingDto = (BookingDto)request.Data;
                TripDto tripDto = bookingDto.trip;
                AccountDto accountDto = bookingDto.account;
                Trip t = new Trip(tripDto.id, tripDto.landmark, tripDto.companyName, tripDto.departure, tripDto.price, tripDto.places);
                Account a = new Account(accountDto.id, accountDto.name, accountDto.password);
                try
                {
                    lock (tripServices)
                    {
                        tripServices.AddBooking(bookingDto.client, bookingDto.phone, bookingDto.numTickets, t, a);
                    }
                    LOGGER.Info("saved booking returning ok response");
                    return new Response()
                    {
                        Type = ResponseType.OK
                    };
                }
                catch (Exception e)
                {
                    LOGGER.Warn("add booking failed ");
                    return new Response()
                    {
                        Type = ResponseType.ERROR,
                        Data = e.Message
                    };
                }
            }
            return null;
        }
        private void SendResponse(Response response)
        {
            LOGGER.InfoFormat("sending response {0}", response);
            formatter.Serialize(stream, response);
            stream.Flush();
        }
    }
}
