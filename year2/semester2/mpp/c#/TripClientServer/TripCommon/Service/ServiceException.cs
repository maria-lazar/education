using System;
using System.Collections.Generic;
using System.Text;

namespace TripCommon.Validator
{
    public class ServiceException: Exception
    {
        public ServiceException(string s): base(s) { }
        public ServiceException(string s, Exception e) : base(s, e) { }

    }
}
