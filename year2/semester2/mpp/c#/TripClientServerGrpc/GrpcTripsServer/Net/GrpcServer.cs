using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System.Threading;
using Grpc.Core;
using log4net;
using tripsGrpc;

namespace GrpcTripsServer.Net
{
    class GrpcServer
    {

        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private int port;
        private string host;
        private Server server;
        private TripServices.TripServicesBase tripServices;

        public GrpcServer(int port, string host, TripServices.TripServicesBase tripServices)
        {
            this.port = port;
            this.tripServices = tripServices;
            this.host = host;
        }

        public void Start()
        {
            this.server = new Server
            {
                Services = {TripServices.BindService(tripServices)},
                Ports = {{this.host, this.port, ServerCredentials.Insecure}}
            };
            server.Start();
            Logger.Info("Server started, listening on " + this.port);
            Logger.Info("Press any key to stop the server...");
            Console.ReadKey();
            server.ShutdownAsync().Wait();
        }
    }
}
