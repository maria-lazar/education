using System;
using System.Collections.Generic;
using System.Text;

namespace TripCommon.Net
{
    public enum ResponseType
    {
        OK, ERROR,
        BOOKING_ADDED
    }
    [Serializable]   
    public class Response
    {
        private ResponseType type;
        private object data;

       
        public Response()
        {

        }

        public ResponseType Type { get => type; set => type = value; }
        public object Data { get => data; set => data = value; }

        public override string ToString()
        {
            return "Response{ type= " + type + ", data=" + data + "}";
        }
        
    }
}
