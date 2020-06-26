using System;

namespace TripCommon.Dto
{
    [Serializable]
    public class TripDto
    {
        public int id;
        public string landmark;
        public string companyName;
        public DateTime departure;
        public float price;
        public int places;
        public int start;
        public int end;

        public TripDto(int tripId)
        {
            this.id = tripId;
        }

        public TripDto(string landmark, int start, int end)
        {
            this.landmark = landmark;
            this.start = start;
            this.end = end;
        }

        public TripDto(int id, string landmark, string companyName, DateTime departure, float price, int places)
        {
            this.id = id;
            this.landmark = landmark;
            this.companyName = companyName;
            this.departure = departure;
            this.price = price;
            this.places = places;
        }
    }
}
