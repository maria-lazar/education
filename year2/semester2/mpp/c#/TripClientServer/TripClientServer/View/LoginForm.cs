using log4net;
using System;
using System.Windows.Forms;
using TripClient.Controller;
using TripCommon.domain;
using View.View;

namespace View
{
    public partial class LoginForm : Form
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("LoginForm");
        public LoginForm()
        {
            InitializeComponent();
        }

        internal LoginController LoginController { get; set; }

        private void LoginForm_Load(object sender, EventArgs e)
        {
            this.FormClosed += LoginForm_FormClosed;
        }

        private void LoginButton_Click(object sender, EventArgs e)
        {
            try
            {
                LOGGER.Info("handle login");
                string name = textName.Text;
                string password = textPassword.Text;
                if (name.Equals(""))
                {
                    MessageBox.Show("Name must be entered");
                    return;
                }
                if (password.Equals(""))
                {
                    MessageBox.Show("Password must be entered");
                    return;
                }
                Account account = LoginController.LogIn(name, password);
                if (account == null)
                {
                    MessageBox.Show("Invalid name or password");
                    return;
                }
                LOGGER.Info("account found" + account);
                ClearFields();
                this.Hide();
                MainForm mainForm = new MainForm();
                MainController mainController = new MainController(LoginController.TripServices)
                {
                    Account = account 
                };
                mainForm.MainController = mainController;
                mainForm.Login = this;
                mainForm.Text = account.Name;
                LOGGER.Info("displaying main form");
                mainForm.Show();
            }
            catch (Exception ex)
            {
                LOGGER.Warn("handle login failed");
                MessageBox.Show(ex.Message);
            }
        }
        void ClearFields()
        {
            textName.Clear();
            textPassword.Clear();
        }
        void LoginForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            LoginController.Logout();
            Application.Exit();
        }
    }
}
