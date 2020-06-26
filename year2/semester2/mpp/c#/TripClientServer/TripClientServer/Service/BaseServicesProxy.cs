using log4net;
using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using TripCommon.Net;
using TripCommon.Validator;

namespace TripClient.Service
{
    abstract class BaseServicesProxy
    {
        protected static readonly ILog LOGGER = LogManager.GetLogger("BaseServicesProxy");
        private string host;
        private int port;
        private NetworkStream stream;

        private IFormatter formatter;
        protected TcpClient connection = null;

        private Queue<Response> responses = new Queue<Response>();
        private volatile bool finished;
        private EventWaitHandle _waitHandle;

        public BaseServicesProxy(string host, int port)
        {
            LOGGER.Info("initializing server port and host");
            this.host = host;
            this.port = port;
        }
        protected void EnsureConnected()
        {
            LOGGER.Info("ensuring connection to server");
            if (connection != null)
            {
                return;
            }
            try
            {
                LOGGER.Info("creating connection");
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                finished = false;
                _waitHandle = new AutoResetEvent(false);
                StartReader();
            }
            catch (Exception e)
            {
                LOGGER.Warn(e.StackTrace);
                throw new ServiceException("creating connection failed", e);
            }
        }
        protected void CloseConnection()
        {
            finished = true;
            if (connection == null)
            {
                return;
            }
            TcpClient tempConnection = connection;
            connection = null;
            LOGGER.Info("closing connection");
            try
            {
                stream.Close();
                tempConnection.Close();
                _waitHandle.Close();
                LOGGER.Info("connection closed");
            }
            catch (Exception e)
            {
                LOGGER.Warn("closing connection failed");
            }

        }
        protected void SendRequest(Request request)
        {
            LOGGER.InfoFormat("sending request to server {0}", request);
            try
            {
                formatter.Serialize(stream, request);
                stream.Flush();
            }
            catch (Exception e)
            {
                LOGGER.Warn("sending request to server failed");
                throw new ServiceException("Error sending object " + e);
            }
        }
        protected Response ReadResponse()
        {
            Response response = null;
            try
            {
                _waitHandle.WaitOne();
                lock (responses)
                {
                    response = responses.Dequeue();
                    LOGGER.InfoFormat("read response from server {0}", response);
                }
            }
            catch (Exception e)
            {
                LOGGER.InfoFormat("reading response from server failed {0}", e);
                LOGGER.Warn(e.StackTrace);
            }
            return response;
        }

        private void StartReader()
        {
            LOGGER.Info("starting reader thread");
            Thread tw = new Thread(Run);
            tw.Start();
        }

        private void Run()
        {
            LOGGER.Info("running reader thread");
            while (!finished)
            {
                try
                {
                    Response response = (Response)formatter.Deserialize(stream);
                    LOGGER.InfoFormat("response received from server {0}", response);
                    if (response.Type == ResponseType.BOOKING_ADDED)
                    {
                        HandleBookingAdded(response);
                    }
                    else
                    {
                        lock (responses)
                        {
                            responses.Enqueue(response);
                        }
                        _waitHandle.Set();
                    }
                }
                catch (SerializationException)
                {
                    CloseConnection();
                    break;
                }
                catch (Exception e)
                {
                    LOGGER.Warn("Reading error " + e);
                    LOGGER.Warn(e.StackTrace);
                    CloseConnection();
                    break;
                }

            }
        }

        public abstract void HandleBookingAdded(Response response);
    }
}
