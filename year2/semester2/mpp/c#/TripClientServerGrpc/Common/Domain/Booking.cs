using System;
using TripCommon.Domain;

namespace TripCommon.domain
{
    public class Booking: Entity<int>
    {
        private int tripId;
        private int accountId;
        private String clientName;
        private String phoneNumber;
        private int numTickets;

        public Booking()
        {
        }

        public Booking(int id, int tripId, int accountId, string clientName, string phoneNumber, int numTickets)
        {
            this.Id = id;
            this.tripId = tripId;
            this.accountId = accountId;
            this.clientName = clientName;
            this.phoneNumber = phoneNumber;
            this.numTickets = numTickets;
        }

        public override string ToString()
        {
            return Id + " " + tripId + " " + accountId + " " + clientName + " " + phoneNumber + " " + numTickets;
        }

        public int TripId { get => tripId; set => tripId = value; }
        public int AccountId { get => accountId; set => accountId = value; }
        public string ClientName { get => clientName; set => clientName = value; }
        public string PhoneNumber { get => phoneNumber; set => phoneNumber = value; }
        public int NumTickets { get => numTickets; set => numTickets = value; }
    }
}
