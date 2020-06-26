using log4net;
using System.Net.Sockets;
using System.Threading;

namespace TripsServer.Net
{
    public abstract class AbstractConcurrentServer : AbstractServer
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("AbstractConcurrentServer");

        public AbstractConcurrentServer(string host, int port) : base(host, port)
        { }

        public override void ProcessRequest(TcpClient client)
        {
            LOGGER.Info("processing request");
            Thread t = CreateWorker(client);
            t.Start();
        }

        protected abstract Thread CreateWorker(TcpClient client);

    }
}
