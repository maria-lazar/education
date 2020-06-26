using log4net;
using TripCommon.domain;
using TripCommon.Validator;

namespace TripClient.Controller
{
    class LoginController
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("LoginController");

        ITripServices tripServices;

        public LoginController(ITripServices tripServices)
        {
            this.tripServices = tripServices;
        }

        public ITripServices TripServices { get => tripServices; }

        public Account LogIn(string name, string password)
        {
            LOGGER.InfoFormat("handle login with name {0} and password {1}", name, password);
            return tripServices.FindAccountByNameAndPassword(name, password);
        }

        internal void Logout()
        {
            LOGGER.Info("assuring connection to server is closed");
            tripServices.Logout();
        }
    }
}
