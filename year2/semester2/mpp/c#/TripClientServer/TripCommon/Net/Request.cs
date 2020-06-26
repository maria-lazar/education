using System;

namespace TripCommon.Net
{
    public enum RequestType
    {
        LOGIN, GET_ALL_TRIPS,
        LOGOUT,
        SEARCH_TRIPS,
        ADD_BOOKING
    }
    [Serializable]
    public class Request
    {
        private RequestType type;
        private object data;


        public Request()
        {

        }

        public RequestType Type { get => type; set => type = value; }
        public object Data { get => data; set => data = value; }

        public override string ToString()
        {
            return "Request{ type= " + type + ", data=" + data + "}";
        }

    }
}
