using log4net;
using System;
using System.Net;
using System.Net.Sockets;
using TripCommon.Validator;

namespace TripsServer.Net
{
    public abstract class AbstractServer
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("AbstractServer");
        private TcpListener server;
        private String host;
        private int port;
        public AbstractServer(String host, int port)
        {
            LOGGER.InfoFormat("setting server host = {0} and port = {1}", host, port);
            this.host = host;
            this.port = port;
        }
        public void Start()
        {
            try
            {
                LOGGER.Info("starting server");
                IPAddress adr = IPAddress.Parse(host);
                IPEndPoint ep = new IPEndPoint(adr, port);
                server = new TcpListener(ep);
                server.Start();
                while (true)
                {
                    LOGGER.Info("Waiting for clients ...");
                    TcpClient client = server.AcceptTcpClient();
                    LOGGER.Info("Client connected ...");
                    ProcessRequest(client);
                }
            }
            catch (Exception ex)
            {
                LOGGER.Warn("starting server failed");
                throw new ServiceException(ex.Message);
            }
            finally
            {
                Stop();
            }
        }
        public void Stop()
        {
            try
            {
                LOGGER.Info("stoping server");
                server.Stop();
            }
            catch (Exception e)
            {
                LOGGER.Warn("closing server failed " + e);
                throw new ServiceException("Closing server error " + e.Message);
            }
        }

        public abstract void ProcessRequest(TcpClient client);

    }
}
