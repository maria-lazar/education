using System;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;

namespace TripRestClient
{
    [Serializable]
    public class Trip : Entity<int>
    {
        private String landmark;
        private String companyName;
        private DateTime departureTime;
        private float price;
        private int availablePlaces;

        public Trip()
        {
        }

        public Trip(int id, string landmark, string companyName, DateTime departureTime, float price,
            int availablePlaces)
        {
            this.Id = id;
            this.landmark = landmark;
            this.companyName = companyName;
            this.departureTime = departureTime;
            this.price = price;
            this.availablePlaces = availablePlaces;
        }

        public Trip(string landmark, string companyName, DateTime departureTime, float price, int availablePlaces)
        {
            this.landmark = landmark;
            this.companyName = companyName;
            this.departureTime = departureTime;
            this.price = price;
            this.availablePlaces = availablePlaces;
        }

        [JsonProperty("landmark")]
        public string Landmark
        {
            get => landmark;
            set => landmark = value;
        }

        [JsonProperty("companyName")]
        public string CompanyName
        {
            get => companyName;
            set => companyName = value;
        }

        [JsonProperty("departureTime")]
        public DateTime DepartureTime
        {
            get => departureTime;
            set => departureTime = value;
        }

        [JsonProperty("price")]
        public float Price
        {
            get => price;
            set => price = value;
        }

        [JsonProperty("availablePlaces")]
        public int AvailablePlaces
        {
            get => availablePlaces;
            set => availablePlaces = value;
        }

        public override string ToString()
        {
            return Id + " " + landmark + " " + companyName + " " + departureTime + " " + price + " " + availablePlaces;
        }
    }
}