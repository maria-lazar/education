
using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using TripCommon.domain;
using TripServer.Db;

namespace TripServer.Repository
{
    class AccountRepository : AbstractCrudRepository<int, Account>
    {
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        public AccountRepository()
        {

        }

        protected override void AddInsertParams(IDbCommand command, Account account)
        {
            IDbDataParameter paramName = command.CreateParameter();
            paramName.ParameterName = "@name";
            paramName.Value = account.Name;
            command.Parameters.Add(paramName);
            IDbDataParameter paramPassw = command.CreateParameter();
            paramPassw.ParameterName = "@passw";
            paramPassw.Value = account.Password;
            command.Parameters.Add(paramPassw);
        }

        protected override string DeleteSql()
        {
            return "DELETE FROM Account WHERE account_id=@id";
        }

        protected override string FindAllSql()
        {
            return "SELECT account_id, name, password FROM Account";
        }

        protected override string FindOneSql()
        {
            return "SELECT account_id, name, password FROM Account WHERE account_id = @id";
        }

        protected override string InsertSql()
        {
            return "INSERT INTO Account(name, password) VALUES(@name, @passw);SELECT LAST_INSERT_ID()";
        }

        protected override bool IsValidId(Account e)
        {
            return e.Id > 0;
        }

        protected override void SetId(Account account, ulong result)
        {
            account.Id = (int)result;
        }

        protected override string SizeSql()
        {
            return "SELECT COUNT(*) FROM Account";
        }

        protected override Account ToEntity(IDataReader dataReader)
        {
            int id = dataReader.GetInt32(0);
            String name = dataReader.GetString(1);
            String password = dataReader.GetString(2);
            return new Account()
            {
                Id = id,
                Name = name,
                Password = password
            };
        }

        protected override string UpdateSql()
        {
            return "UPDATE Account SET name=@name, password=@passw WHERE account_id=@id";
        }

        public Account FindByNamePassword(string name, string password)
        {
            Logger.InfoFormat("Finding account by name {0} password {1}", name, password);
            IDbConnection connection = DbUtils.GetConnection();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "SELECT account_id, name, password FROM Account WHERE name=@n AND password=@p";
                IDbDataParameter paramName = command.CreateParameter();
                paramName.ParameterName = "@n";
                paramName.Value = name;
                command.Parameters.Add(paramName);
                IDbDataParameter paramPassw = command.CreateParameter();
                paramPassw.ParameterName = "@p";
                paramPassw.Value = password;
                command.Parameters.Add(paramPassw);
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        Account elem = ToEntity(dataReader);
                        Logger.InfoFormat("found account {0}", elem);
                        return elem;
                    }
                }
            }
            Logger.InfoFormat("no account found with name {0} and password {1}", name, password);
            return null;

        }
    }
}
