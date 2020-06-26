using System;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;

namespace TripRestClient
{
    public class Entity<ID>
    {
        private ID id;
        [JsonProperty("id")]
        public ID Id
        {
            get => id;
            set => id = value;
        }
    }
}