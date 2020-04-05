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
    class ActivePlayerRepository : SqlRepository<ActivePlayerId, ActivePlayer>
    {
        public ActivePlayerRepository()
        {
            TableName = "ActivePlayer";
            ConvertToEntity = ToEntity;
        }

        public ActivePlayer ToEntity(DbDataReader reader)
        {
            return new ActivePlayer()
            {
                Id = new ActivePlayerId(int.Parse(reader["id_s"].ToString()), int.Parse(reader["id_g"].ToString())),
                Points = int.Parse(reader["points"].ToString()),
                Type = (PlayerType)Enum.Parse(typeof(PlayerType), reader["type"].ToString(), true)
            };
        }

        public override DbCommand SaveCommand(ActivePlayer entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "INSERT INTO ActivePlayer(id_s, id_g, points, type) VALUES (@id_s, @id_g, @points, @type)";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@id_s", entity.Id.IdPlayer),
                new SqlParameter("@id_g", entity.Id.IdGame),
                new SqlParameter("@points", entity.Points),
                new SqlParameter("@type", entity.Type.ToString())
            };
            command.Parameters.AddRange(param.ToArray());
            return command;
        }

        public override DbCommand UpdateCommand(ActivePlayer entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "UPDATE ActivePlayer SET points = @points, type = @type WHERE id_s = @id_s AND id_g = @id_g";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@id_s", entity.Id.IdPlayer),
                new SqlParameter("@id_g", entity.Id.IdGame),
                new SqlParameter("@points", entity.Points),
                new SqlParameter("@type", entity.Type.ToString())
            };
            command.Parameters.AddRange(param.ToArray());
            return command;    
        }

        public override DbCommand FindOneCommand(ActivePlayerId id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM " + TableName + " WHERE id_s = @id_s AND id_g = @id_g";
            List<SqlParameter> p = new List<SqlParameter>()
            {
                new SqlParameter("@id_s", id.IdPlayer),
                new SqlParameter("@id_g", id.IdGame)
            };
            command.Parameters.AddRange(p.ToArray());
            return command;
        }

        public override DbCommand DeleteCommand(ActivePlayerId id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "DELETE FROM " + TableName + " WHERE id_s = @id_s AND id_g= @id_g";
            List<SqlParameter> p = new List<SqlParameter>()
                {
                    new SqlParameter("@id_s", id.IdPlayer),
                    new SqlParameter("@id_g", id.IdGame)
                };
            command.Parameters.AddRange(p.ToArray());
            return command;
        }
    }
}
