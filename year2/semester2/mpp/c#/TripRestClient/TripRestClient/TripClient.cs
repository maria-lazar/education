using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace TripRestClient
{
    class TripClient
    {
        HttpClient httpClient = new HttpClient();
        private string url;

        public TripClient(string url)
        {
            this.url = url;
            httpClient.BaseAddress = new Uri(url);
            httpClient.DefaultRequestHeaders.Accept.Clear();
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        }

        public async Task<Trip> GetById(int id)
        {
            HttpResponseMessage response = await httpClient.GetAsync(id.ToString());
            Trip trip = null;
            if (response.IsSuccessStatusCode)
            {
                trip = await response.Content.ReadAsAsync<Trip>();
            }
            else
            {
                throw new ServiceException("Couldn't get resource: response status code " +
                                           response.StatusCode.ToString());
            }

            return trip;
        }

        public async Task<List<Trip>> GetAll()
        {
            HttpResponseMessage response = await httpClient.GetAsync("");
            List<Trip> trips = null;
            if (response.IsSuccessStatusCode)
            {
                trips = await response.Content.ReadAsAsync<List<Trip>>();
            }
            else
            {
                throw new ServiceException("Couldn't get resource: response status code: " +
                                           response.StatusCode.ToString());
            }

            return trips;
        }

        public async Task<Trip> Add(Trip trip)
        {
            string s = trip.DepartureTime.ToString("MM-dd-yyyy h:mm");
            trip.DepartureTime = DateTime.ParseExact(s, "MM-dd-yyyy h:mm", CultureInfo.CurrentCulture);

            string json = JsonConvert.SerializeObject(trip);
            StringContent data = new StringContent(json, Encoding.Unicode, "application/json");
            HttpResponseMessage response = await httpClient.PostAsync("", data);
            Trip addedTrip = null;
            if (response.IsSuccessStatusCode)
            {
                addedTrip = await response.Content.ReadAsAsync<Trip>();
            }
            else
            {
                throw new ServiceException("Couldn't post resource: response status code: " +
                                           response.StatusCode.ToString());
            }

            return addedTrip;
        }

        public async Task<Trip> Update(Trip trip)
        {
            string s = trip.DepartureTime.ToString("MM-dd-yyyy h:mm");
            trip.DepartureTime = DateTime.ParseExact(s, "MM-dd-yyyy h:mm", CultureInfo.CurrentCulture);

            string json = JsonConvert.SerializeObject(trip);
            StringContent data = new StringContent(json, Encoding.Unicode, "application/json");
            HttpResponseMessage response = await httpClient.PutAsync(trip.Id.ToString(), data);
            // HttpResponseMessage response = await httpClient.PutAsync("2", data);
            Trip updatedTrip = null;
            if (response.IsSuccessStatusCode)
            {
                updatedTrip = await response.Content.ReadAsAsync<Trip>();
            }
            else
            {
                throw new ServiceException("Couldn't put resource: response status code: " +
                                           response.StatusCode.ToString());
            }

            return updatedTrip;
        }

        public async Task<Trip> Delete(int tripId)
        {
            HttpResponseMessage response = await httpClient.DeleteAsync(tripId.ToString());
            // HttpResponseMessage response = await httpClient.DeleteAsync("2");
            Trip deletedTrip = null;
            if (response.IsSuccessStatusCode)
            {
                deletedTrip = await response.Content.ReadAsAsync<Trip>();
            }
            else
            {
                throw new ServiceException("Couldn't delete resource: response status code: " +
                                           response.StatusCode.ToString());
            }

            return deletedTrip;
        }
    }

    class ServiceException : Exception
    {
        public ServiceException(string message) : base(message)
        {
        }
    }
}