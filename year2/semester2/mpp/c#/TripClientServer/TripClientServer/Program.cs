using log4net;
using System;
using System.Configuration;
using System.Windows.Forms;
using TripClient.Controller;
using TripClient.Service;
using TripCommon.Validator;
using View;

namespace TripClient
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static readonly ILog LOGGER = LogManager.GetLogger("Program");


        [STAThread]
        static void Main()
        {
            LOGGER.Info("creating application context");
            try
            {
                ITripServices tripServices = new TripServicesProxy(ConfigurationManager.AppSettings["server-host"],
                    Int32.Parse(ConfigurationManager.AppSettings["server-port"]));
                LoginController loginController = new LoginController(tripServices);
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                LoginForm loginForm = new LoginForm
                {
                    LoginController = loginController
                };
                Application.Run(loginForm);
            }
            catch (Exception e)
            {
                LOGGER.Warn(e.Message);
            }
        }
    }
}
