using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Repository
{
    class GameRepository : SqlRepository<int, Game>
    {
        public GameRepository()
        {
            TableName = "Game";
            ConvertToEntity = ToEntity;
        }

        public override DbCommand DeleteCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "DELETE FROM " + TableName + " WHERE id_g = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand FindOneCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM " + TableName + " WHERE id_g = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand SaveCommand(Game entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "INSERT INTO Game(id_t1, id_t2, date) VALUES (@id_t1, @id_t2, @date)";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@id_t1", entity.FirstTeamId),
                new SqlParameter("@id_t2", entity.SecondTeamId),
                new SqlParameter("@date", entity.Date)
            };
            command.Parameters.AddRange(param.ToArray());
            return command;
        }

        public Game ToEntity(DbDataReader reader)
        {
            return new Game()
            {
                Id = int.Parse(reader["id_g"].ToString()),
                FirstTeamId = int.Parse(reader["id_t1"].ToString()),
                SecondTeamId = int.Parse(reader["id_t2"].ToString()),
                Date = DateTime.Parse(reader["date"].ToString())
            };
        }
        
        public override DbCommand UpdateCommand(Game entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "UPDATE Game SET id_t1 = @id_t1, id_t2 = @id_t2, date = @date WHERE id_g = @id";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@id", entity.Id),
                new SqlParameter("@id_t1", entity.FirstTeamId),
                new SqlParameter("@id_t2", entity.SecondTeamId),
                new SqlParameter("@date", entity.Date)
            };
            command.Parameters.AddRange(param.ToArray());
            return command;
        }
    }
}
