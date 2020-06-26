using System;

namespace TripClientServer.Controller
{
    enum TripEvent
    {
        BookingAdded
    }
    class TripEventArgs: EventArgs
    {
        private readonly TripEvent tripEvent;
        private readonly object data;

        public TripEventArgs(TripEvent tripEvent, object data)
        {
            this.tripEvent = tripEvent;
            this.data = data;
        }

        public object Data => data;

        internal TripEvent TripEvent => tripEvent;
    }
}
