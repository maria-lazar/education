using System;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Reflection;
using Grpc.Core;
using GrpcTripsServer.Net;
using log4net;
using log4net.Config;
using TripCommon.Validator;
using TripServer.Repository;
using TripsServer.Service;

namespace GrpcTripsServer
{
    class Program
    {
        static void Main(string[] args)
        {
            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

            TripRepository tripRepository = new TripRepository();
            TripValidator tripValidator = new TripValidator();
            AccountRepository accountRepository = new AccountRepository();
            AccountValidator accountValidator = new AccountValidator();
            BookingRepository bookingRepository = new BookingRepository();
            BookingValidator bookingValidator = new BookingValidator();
            ITripServices tripServices = new TripServicesImpl(accountRepository, tripRepository, bookingRepository,
                accountValidator, tripValidator, bookingValidator);

            TripServicesGrpcImpl tripServicesGrpc = new TripServicesGrpcImpl(tripServices);
            GrpcServer server = new GrpcServer(int.Parse(ConfigurationManager.AppSettings["port"]),
                ConfigurationManager.AppSettings["host"], tripServicesGrpc);
            server.Start();
        }
    }
}
