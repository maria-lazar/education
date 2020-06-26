using log4net;
using System;
using System.Configuration;
using TripCommon.Repository;
using TripCommon.Validator;
using TripCommon.Validator;
using TripsServer.Net;
using TripsServer.Service;

namespace TripsServer
{
    class Program
    {
        static readonly ILog LOGGER = LogManager.GetLogger("Program");

        static void Main(string[] args)
        {
            LOGGER.Info("loading application context");
            try
            {
                AccountRepository accountRepository = new AccountRepository();
                TripRepository tripRepository = new TripRepository();
                BookingRepository bookingRepository = new BookingRepository();
                BookingValidator bookingValidator = new BookingValidator();
                AccountValidator accountValidator = new AccountValidator();
                TripValidator tripValidator = new TripValidator();
                ITripServices tripServices = new TripServicesImpl(accountRepository, tripRepository, bookingRepository, accountValidator, tripValidator, bookingValidator);
                AbstractServer server = new RpcConcurrentServer(ConfigurationManager.AppSettings["host"],
                    Int32.Parse(ConfigurationManager.AppSettings["port"]), tripServices);
                server.Start();
            }
            catch (Exception ex)
            {
                LOGGER.Warn(ex.StackTrace);
            }
        }
    }
}
