using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TripCommon.domain;

namespace TripServer.Repository
{
    class BookingRepository : AbstractCrudRepository<int, Booking>
    {
        protected override void AddInsertParams(IDbCommand command, Booking e)
        {
            IDbDataParameter paramAccount = command.CreateParameter();
            paramAccount.ParameterName = "@a";
            paramAccount.Value = e.AccountId;
            command.Parameters.Add(paramAccount);
            IDbDataParameter paramTrip = command.CreateParameter();
            paramTrip.ParameterName = "@t";
            paramTrip.Value = e.TripId;
            command.Parameters.Add(paramTrip);
            IDbDataParameter paramClient = command.CreateParameter();
            paramClient.ParameterName = "@c";
            paramClient.Value = e.ClientName;
            command.Parameters.Add(paramClient);
            IDbDataParameter paramPhone = command.CreateParameter();
            paramPhone.ParameterName = "@p";
            paramPhone.Value = e.PhoneNumber;
            command.Parameters.Add(paramPhone);
            IDbDataParameter paramNumTickets = command.CreateParameter();
            paramNumTickets.ParameterName = "@n";
            paramNumTickets.Value = e.NumTickets;
            command.Parameters.Add(paramNumTickets);
        }

        protected override string DeleteSql()
        {
            return "DELETE FROM Booking WHERE booking_id=@id";
        }

        protected override string FindAllSql()
        {
            return "SELECT booking_id, account_id, trip_id, clientName, phoneNumber, numTickets FROM Booking";
        }

        protected override string FindOneSql()
        {
            return "SELECT booking_id, account_id, trip_id, clientName, phoneNumber, numTickets FROM Booking WHERE booking_id=@id";
        }

        protected override string InsertSql()
        {
            return "INSERT INTO Booking(account_id, trip_id, clientName, phoneNumber, numTickets) VALUES(@a, @t, @c, @p, @n);SELECT LAST_INSERT_ID()";
        }

        protected override bool IsValidId(Booking e)
        {
            return e.Id > 0;
        }

        protected override void SetId(Booking e, ulong result)
        {
            e.Id = (int)result;
        }

        protected override string SizeSql()
        {
            return "SELECT COUNT(*) FROM Booking";
        }

        protected override Booking ToEntity(IDataReader dataReader)
        {
            int id = dataReader.GetInt32(0);
            int accountId = dataReader.GetInt32(1);
            int tripId = dataReader.GetInt32(2);
            String client = dataReader.GetString(3);
            String phone = dataReader.GetString(4);
            int numTickets = dataReader.GetInt32(5);
            return new Booking()
            {
                Id = id,
                AccountId = accountId,
                TripId = tripId,
                ClientName = client,
                PhoneNumber = phone,
                NumTickets = numTickets
            };
        }

        protected override string UpdateSql()
        {
            return "UPDATE Booking SET account_id=@a, trip_id=@t, clientName=@c, phoneNumber=@p, numTickets=@n WHERE booking_id=@id";
        }
    }
}
