using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TripCommon.Dto
{
    [Serializable]
    public class BookingDto
    {
        public int id;
        public string client;
        public string phone;
        public int numTickets;
        public TripDto trip;
        public AccountDto account;

        public BookingDto(string client, string phone, int numTickets, TripDto trip, AccountDto account)
        {
            this.client = client;
            this.phone = phone;
            this.numTickets = numTickets;
            this.trip = trip;
            this.account = account;
        }

        public BookingDto(int id, string clientName, string phoneNumber, int numTickets, TripDto tripDto, AccountDto accountDto)
        {
            this.id = id;
            this.client = clientName;
            this.phone = phoneNumber;
            this.numTickets = numTickets;
            this.trip = tripDto;
            this.account = accountDto;
        }

        public override string ToString()
        {
            return "Booking: { id=" + id + ", tripId=" + trip.id + ", accountId=" + account.id + ", client=" + client + ",num=" + numTickets;
        }
    }
}
