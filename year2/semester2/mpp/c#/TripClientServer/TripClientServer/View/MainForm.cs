using log4net;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using TripClient.Controller;
using TripClientServer.Controller;
using TripCommon.domain;
using TripCommon.Validator;

namespace View.View
{
    public partial class MainForm : Form
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("MainForm");

        internal MainController MainController { get; set; }
        public Form Login { get; internal set; }

        List<Trip> modelTrips;
        List<Trip> modelSearchTrips;
        public MainForm()
        {
            InitializeComponent();
        }

        private void MainForm2_Load(object sender, EventArgs e)
        {
            MainController.UpdateEvent += TripUpdate;
            this.FormClosed += MainForm2_FormClosed;
            modelTrips = new List<Trip>(MainController.GetTrips());
            modelSearchTrips = new List<Trip>();
            PopulateDataGridView(tripDataGridView, modelTrips);
            PopulateDataGridView(searchTripDataGridView, modelSearchTrips);
        }



        void MainForm2_FormClosed(object sender, FormClosedEventArgs e)
        {
            try
            {
                MainController.Logout();
                MainController.UpdateEvent -= TripUpdate;
                Login.Show();
            }
            catch(Exception ex)
            {
                LOGGER.Warn("logging out failed " + ex);
            }
        }
        void LogoutBtn_Click(object sender, EventArgs e)
        {
            this.Close();
        }


        private void SearchTripBtn_Click(object sender, EventArgs e)
        {
            LOGGER.Info("handling search trips");
            try
            {
                string landmark = landmarkTextBox.Text;
                string start = startTextBox.Text;
                string end = endTextBox.Text;
                if (landmark.Equals("") || start.Equals("") || end.Equals(""))
                {
                    MessageBox.Show("All fields must be entered");
                    return;
                }
                int startHour = Int32.Parse(start);
                int endHour = Int32.Parse(end);
                List<Trip> trips = MainController.SearchTrips(landmark, startHour, endHour);
                modelSearchTrips = trips;
                PopulateDataGridView(searchTripDataGridView, modelSearchTrips);
            }
            catch (FormatException)
            {
                LOGGER.Warn("handling search trips failed invalid hours");
                MessageBox.Show("Invalid hours");
                return;
            }
            catch (Exception ex)
            {
                LOGGER.Warn("handling search trips failed " + ex.Message);
                MessageBox.Show(ex.Message);
                return;
            }

        }
        private void TripUpdate(object sender, TripEventArgs e)
        {
            if (e.TripEvent == TripEvent.BookingAdded)
            {
                Booking b = (Booking)e.Data;
                LOGGER.InfoFormat("trip update booking added {0}", b);
                tripDataGridView.BeginInvoke(new UpdateModelTripsCallback(this.UpdateModelTrips), new object[] { tripDataGridView, modelTrips, b });
                searchTripDataGridView.BeginInvoke(new UpdateModelTripsCallback(this.UpdateModelTrips), new Object[] { searchTripDataGridView, modelSearchTrips, b });
            }
        }
        private void UpdateModelTrips(DataGridView tripDataGridView, List<Trip> modelTrips, Booking booking)
        {
            int id = booking.TripId;
            int i = modelTrips.FindIndex(t => t.Id == id);
            if (i >= 0)
            {
                modelTrips[i].AvailablePlaces -= booking.NumTickets;
                PopulateDataGridView(tripDataGridView, modelTrips);
            }
        }

        public delegate void UpdateModelTripsCallback(DataGridView dataGridView, List<Trip> modelTrips, Booking booking);

        private void PopulateDataGridView(DataGridView dataGridView, List<Trip> trips)
        {
            dataGridView.DataSource = null;
            dataGridView.DataSource = trips;
            MarkNoPlacesTrips(dataGridView);
            dataGridView.Columns[5].Visible = false;
            if (dataGridView == searchTripDataGridView)
            {
                dataGridView.Columns[0].Visible = false;
            }
        }
        private void MarkNoPlacesTrips(DataGridView dataGridView)
        {
            foreach (DataGridViewRow row in dataGridView.Rows)
            {
                if (Convert.ToInt32(row.Cells[4].Value) == 0)
                {
                    row.DefaultCellStyle.ForeColor = Color.Red;
                }
            }
        }

        private void BookBtn_Click(object sender, EventArgs e)
        {
            LOGGER.Info("handle add booking");
            try
            {
                string client = clientTextBox.Text;
                string phone = phoneTextBox.Text;
                string numTickets = numTicketsTextBox.Text;
                if (tripDataGridView.SelectedRows.Count == 0)
                {
                    MessageBox.Show("A trip must be selected");
                    return;
                }
                if (tripDataGridView.SelectedRows[0].Cells[0].Value == null)
                {
                    MessageBox.Show("A trip must be selected");
                    return;
                }
                Trip trip = (Trip)tripDataGridView.SelectedRows[0].DataBoundItem;
                if (client.Equals(""))
                {
                    MessageBox.Show("Client name must be entered");
                    return;
                }
                if (phone.Equals(""))
                {
                    MessageBox.Show("Phone number must be entered");
                    return;
                }

                if (numTickets.Equals(""))
                {
                    MessageBox.Show("Number of tickets must be entered");
                    return;
                }
                int noTickets = Int32.Parse(numTickets);
                MainController.AddBooking(client, phone, noTickets, trip);
                ClearTextBoxes();
                MessageBox.Show("Booking saved");
            }
            catch (FormatException)
            {
                LOGGER.Info("handle add booking failed invalid data");
                MessageBox.Show("Invalid data");
            }
            catch (ServiceException se)
            {
                MessageBox.Show(se.Message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
            }
        }

        private void ClearTextBoxes()
        {
            clientTextBox.Clear();
            phoneTextBox.Clear();
            numTicketsTextBox.Clear();
        }
    }
}
