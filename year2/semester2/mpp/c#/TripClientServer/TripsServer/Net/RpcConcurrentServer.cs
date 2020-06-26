using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using TripCommon.Validator;

namespace TripsServer.Net
{
    class RpcConcurrentServer : AbstractConcurrentServer
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("RpcConcurrentServer");

        private ITripServices tripServices;

        public RpcConcurrentServer(string host, int port, ITripServices tripServices) : base(host, port)
        {
            this.tripServices = tripServices;
        }
        protected override Thread CreateWorker(TcpClient client)
        {
            LOGGER.InfoFormat("creating worker for client {0}", client);
            TripClientRpcWorker worker = new TripClientRpcWorker(tripServices, client);
            return new Thread(new ThreadStart(worker.Run));
        }
    }
}
