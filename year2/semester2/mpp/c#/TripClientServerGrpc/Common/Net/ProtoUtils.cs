using System;
using System.Collections.Generic;
using System.Text;
using tripsGrpc;
using Account = TripCommon.domain.Account;
using AccountDto = tripsGrpc.Account;
using Booking = TripCommon.domain.Booking;
using Trip = TripCommon.domain.Trip;
using TripDto = tripsGrpc.Trip;

namespace Common.Net
{
    public class ProtoUtils
    {

        public static Response CreateLoginResponse(Account account)
        {
            Response response = null;
            if (account == null)
            {
                response = new Response()
                {
                    Type = Response.Types.ResponseType.Error,
                    Error = "Invalid name or password"
                };
            }
            else
            {
                AccountDto a = new AccountDto()
                {
                    Id = account.Id,
                    Name = account.Name,
                    Password = account.Password
                };
                response = new Response()
                {
                    Type = Response.Types.ResponseType.Ok,
                    Account = a
                };
            }
            return response;
        }
        public static Response CreateOkResponse()
        {
            return new Response()
            {
                Type = Response.Types.ResponseType.Ok
            };
        }

        public static Response CreateErrorResponse(Exception exception)
        {
            return new Response()
            {
                Type = Response.Types.ResponseType.Error,
                Error = exception.Message
            };
        }

        public static Response CreateGetTripsResponse(List<Trip> trips)
        {
            Response response = new Response()
            {
                Type = Response.Types.ResponseType.Ok
            };
            trips.ForEach(el =>
            {
                double departure = el.DepartureTime.Subtract(new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds;
                TripDto trip = new TripDto()
                {
                    Id = el.Id,
                    Landmark = el.Landmark,
                    CompanyName = el.CompanyName,
                    DepartureTime = departure,
                    Price = el.Price,
                    AvailablePlaces = el.AvailablePlaces
                };
                response.Trips.Add(trip);
            });
            return response;
        }

        public static Trip ConvertToTrip(TripDto t)
        {
            var posixTime = DateTime.SpecifyKind(new DateTime(1970, 1, 1, 0, 0, 0), DateTimeKind.Utc);
            DateTime departure = posixTime.AddMilliseconds(t.DepartureTime);
            return new Trip(t.Id, t.Landmark, t.CompanyName, departure, t.Price, t.AvailablePlaces);
        }

        public static Account ConvertToAccount(AccountDto account)
        {
            return new Account(account.Id, account.Name, account.Password);
        }

       
        public static Account GetAccount(Request request)
        {
            AccountDto a = request.Account;
            return new Account(a.Id, a.Name, a.Password);
        }

    }
}
