using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Repository
{
    public class SqlConnectionFactory
    {
        private string provider;
        private string connectionString;
        private DbProviderFactory factory;
        private DbConnection connection;
        private static SqlConnectionFactory instance = null;

        public static SqlConnectionFactory GetInstance()
        {
            if (instance == null)
            {
                return new SqlConnectionFactory();
            }
            return instance;
        }

        private SqlConnectionFactory()
        {
            provider = ConfigurationManager.AppSettings["provider"];
            connectionString = ConfigurationManager.AppSettings["connectionString"];
            factory = DbProviderFactories.GetFactory(provider);
            connection = factory.CreateConnection();
            connection.ConnectionString = connectionString;
            connection.Open();
        }

        public DbConnection Connection { get => connection; set => connection = value; }
        public DbProviderFactory Factory { get => factory; set => factory = value; }
    }
}
