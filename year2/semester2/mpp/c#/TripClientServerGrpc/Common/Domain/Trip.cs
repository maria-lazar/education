using System;
using TripCommon.Domain;

namespace TripCommon.domain
{
    public class Trip: Entity<int>
    {
        private String landmark;
        private String companyName;
        private DateTime departureTime;
        private float price;
        private int availablePlaces;

        public Trip()
        {
        }
        public Trip(int id, string landmark, string companyName, DateTime departureTime, float price, int availablePlaces)
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

        public string Landmark { get => landmark; set => landmark = value; }
        public string CompanyName { get => companyName; set => companyName = value; }
        public DateTime DepartureTime { get => departureTime; set => departureTime = value; }
        public float Price { get => price; set => price = value; }
        public int AvailablePlaces { get => availablePlaces; set => availablePlaces = value; }

        public override string ToString()
        {
            return Id + " " + landmark + " " + companyName + " " + departureTime + " " + price + " " + availablePlaces;
        }
    }

}
