using log4net;
using TripCommon.Db;
using TripCommon.domain;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TripCommon.Repository
{
    class TripRepository : AbstractCrudRepository<int, Trip>
    {
        private static readonly ILog LOGGER = LogManager.GetLogger("TripRepository");
        protected override void AddInsertParams(IDbCommand command, Trip e)
        {
            IDbDataParameter paramLand = command.CreateParameter();
            paramLand.ParameterName = "@l";
            paramLand.Value = e.Landmark;
            command.Parameters.Add(paramLand);
            IDbDataParameter paramCompany = command.CreateParameter();
            paramCompany.ParameterName = "@c";
            paramCompany.Value = e.CompanyName;
            command.Parameters.Add(paramCompany);
            IDbDataParameter paramDepart = command.CreateParameter();
            paramDepart.ParameterName = "@d";
            paramDepart.Value = e.DepartureTime;
            command.Parameters.Add(paramDepart);
            IDbDataParameter paramPrice = command.CreateParameter();
            paramPrice.ParameterName = "@p";
            paramPrice.Value = e.Price;
            command.Parameters.Add(paramPrice);
            IDbDataParameter paramAvailable = command.CreateParameter();
            paramAvailable.ParameterName = "@a";
            paramAvailable.Value = e.AvailablePlaces;
            command.Parameters.Add(paramAvailable);
        }

        protected override string DeleteSql()
        {
            return "DELETE FROM Trip WHERE trip_id=@id";
        }

        protected override string FindAllSql()
        {
            return "SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip";
        }

        protected override string FindOneSql()
        {
            return "SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip WHERE trip_id=@id";
        }

        protected override string InsertSql()
        {
            return "INSERT INTO Trip(landmark, companyName, departureTime, price, availablePlaces) VALUES (@l, @c, @d, @p, @a);SELECT LAST_INSERT_ID()";
        }

        protected override bool IsValidId(Trip e)
        {
            return e.Id > 0;
        }

        protected override void SetId(Trip e, ulong result)
        {
            e.Id = (int)result;
        }

        protected override string SizeSql()
        {
            return "SELECT COUNT(*) FROM Trip";
        }

        protected override Trip ToEntity(IDataReader dataReader)
        {
            int id = dataReader.GetInt32(0);
            String landmark = dataReader.GetString(1);
            String company = dataReader.GetString(2);
            DateTime departure = dataReader.GetDateTime(3);
            float price = dataReader.GetFloat(4);
            int available = dataReader.GetInt32(5);
            return new Trip()
            {
                Id = id,
                Landmark = landmark,
                CompanyName = company,
                DepartureTime = departure,
                Price = price,
                AvailablePlaces = available
            };
        }

        protected override string UpdateSql()
        {
            return "UPDATE Trip SET landmark=@l, companyName=@c, departureTime=@d, price=@p, availablePlaces=@a WHERE trip_id=@id";
        }

        public IList<Trip> FindByLandmarkDepartureHour(string landmark, int start, int end)
        {
            LOGGER.InfoFormat("finding trips with landmark {0} with departure hour between {1} and {2}", landmark, start, end);
            IDbConnection connection = DbUtils.GetConnection();
            IList<Trip> elems = new List<Trip>();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip WHERE landmark=@l" +
                        " AND (HOUR(departureTime) >= @s AND HOUR(departureTime) < @e)";
                IDbDataParameter paramLand = command.CreateParameter();
                paramLand.ParameterName = "@l";
                paramLand.Value = landmark;
                command.Parameters.Add(paramLand);
                IDbDataParameter paramStart = command.CreateParameter();
                paramStart.ParameterName = "@s";
                paramStart.Value = start;
                command.Parameters.Add(paramStart);
                IDbDataParameter paramEnd = command.CreateParameter();
                paramEnd.ParameterName = "@e";
                paramEnd.Value = end;
                command.Parameters.Add(paramEnd);
                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        elems.Add(ToEntity(dataReader));
                    }
                }
            }
            LOGGER.InfoFormat("found trips with landmark {0} with departure hour between {1} and {2}", landmark, start, end);
            return elems;
        }
    }
}
