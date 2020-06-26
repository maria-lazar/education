using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TripCommon.Db
{
    class MySqlConnectionFactory : ConnectionFactory
    {
        public override IDbConnection CreateConnection()
        {
            String connectionString = ConfigurationManager.AppSettings["connectionString"];
            return new MySqlConnection(connectionString);
        }
    }
}
