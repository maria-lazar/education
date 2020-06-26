using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TripCommon.Domain
{
    public class Entity<ID>
    {
        private ID id;

        public ID Id { get => id; set => id = value; }
    }
}
