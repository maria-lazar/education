using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Threading.Tasks.Dataflow;
using System.Xml.Linq;
using Common.Net;
using Grpc.Core;
using log4net;
using TripCommon.Validator;
using tripsGrpc;
using Account = TripCommon.domain.Account;
using Trip = TripCommon.domain.Trip;
using Booking = TripCommon.domain.Booking;

namespace GrpcTripsServer.Net
{
    class TripServicesGrpcImpl : TripServices.TripServicesBase
    {
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private ITripServices tripServices;
        private ConcurrentDictionary<int, IServerStreamWriter<Response>> observers = new ConcurrentDictionary<int, IServerStreamWriter<Response>>();
        private readonly BufferBlock<Response> buffer = new BufferBlock<Response>();

        public TripServicesGrpcImpl(ITripServices tripServices)
        {
            this.tripServices = tripServices;
        }

        public override async Task Register(Request request, IServerStreamWriter<Response> responseStream, ServerCallContext context)
        {
            Logger.InfoFormat("adding observer {0}", responseStream);
            observers[request.Account.Id] = responseStream;

            while (observers.ContainsKey(request.Account.Id))
            {
                var e = await buffer.ReceiveAsync();
                foreach (var serverStreamWriter in observers.Values)
                {
                    Logger.InfoFormat("notifying {0}", serverStreamWriter);
                    await serverStreamWriter.WriteAsync(e);
                }
            }
        }

        public override Task<Response> Logout(Request request, ServerCallContext context)
        {
            tripsGrpc.Account account = request.Account;
            IServerStreamWriter<Response> o = null;
            observers.Remove(account.Id, out o);
            Logger.InfoFormat("removing observer {0}", account.Id);
            return Task.FromResult(ProtoUtils.CreateOkResponse());
        }

        public override Task<Response> GetAccountByNamePassword(Request request, ServerCallContext context)
        {
            Account a = ProtoUtils.GetAccount(request);
            Logger.InfoFormat("getting account by name {0} and password {1}", a.Name, a.Password);
            Response response = null;
            try
            {
                Account account = tripServices.GetAccountByNamePassword(a.Name, a.Password);
                response = ProtoUtils.CreateLoginResponse(account);
            }
            catch (Exception e)
            {
                response = ProtoUtils.CreateErrorResponse(e);
            }
            Logger.InfoFormat("sending response {0}", response);
            return Task.FromResult(response);
        }

        public override Task<Response> GetAllTrips(Request request, ServerCallContext context)
        {
            Logger.Info("getting all trips");
            Response response = null;
            try
            {
                List<Trip> t = tripServices.GetAllTrips();
                response = ProtoUtils.CreateGetTripsResponse(t);
            }
            catch (Exception e)
            {
                response = ProtoUtils.CreateErrorResponse(e);
            }
            Logger.InfoFormat("sending get all trips response {0}", response.Type);
            return Task.FromResult(response);
        }

        public override Task<Response> GetTripsByLandmarkDepartureHour(Request request, ServerCallContext context)
        {
            Logger.Info("searching trips");
            Response response = null;
            try
            {
                tripsGrpc.Trip t = request.Trip;
                List<Trip> tripList = tripServices.GetTripsByLandmarkDepartureHour(t.Landmark, t.Start, t.End);
                response = ProtoUtils.CreateGetTripsResponse(tripList);
            }
            catch (Exception e)
            {
                response = ProtoUtils.CreateErrorResponse(e);
            }
            Logger.InfoFormat("sending search trips response {0}", response.Type);
            return Task.FromResult(response);
        }

        public override Task<Response> AddBooking(Request request, ServerCallContext context)
        {
            Logger.Info("add booking");
            Response response = null;
            try
            {
                tripsGrpc.Booking booking = request.Booking;
                Trip trip = ProtoUtils.ConvertToTrip(booking.Trip);
                Account account = ProtoUtils.ConvertToAccount(booking.Account);
                Booking savedBooking = tripServices.AddBooking(booking.Client, booking.Phone, booking.NumTickets, trip, account);
                response = ProtoUtils.CreateOkResponse();

                Logger.Info("notifying observers");
                Response responseBookingAdded = new Response()
                {
                    Booking = new tripsGrpc.Booking()
                    {
                        Id = savedBooking.Id,
                        Account = booking.Account,
                        Trip = booking.Trip,
                        Client = savedBooking.ClientName,
                        Phone = savedBooking.PhoneNumber,
                        NumTickets = savedBooking.NumTickets
                    }
                };
                buffer.Post(responseBookingAdded);
            }
            catch (Exception e)
            {
                response = ProtoUtils.CreateErrorResponse(e);
            }
            Logger.InfoFormat("add booking response {0}", response);
            return Task.FromResult(response);
        }
    }
}
