using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TripCommon.Db
{
    class DbUtils
    {
        private static IDbConnection connection = null;

        public static IDbConnection GetConnection()
        {
            if (connection == null || connection.State == System.Data.ConnectionState.Closed)
            {
                connection = GetNewConnection();
                connection.Open();
            }
            return connection;
        }

        private static IDbConnection GetNewConnection()
        {
            return ConnectionFactory.GetInstance().CreateConnection();
        }
    }
}
