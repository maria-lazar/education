using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Repository
{
    class PlayerRepository : SqlRepository<int, Player>
    {
        public PlayerRepository()
        {
            TableName = "Player";
            ConvertToEntity = ToEntity;
        }

        public override DbCommand DeleteCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "DELETE FROM " + TableName + " WHERE id_s = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand FindOneCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM " + TableName + " WHERE id_s = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand SaveCommand(Player entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "INSERT INTO Player(name, school, id_t) VALUES (@name, @school, @id_t)";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@name", entity.Name),
                new SqlParameter("@school", entity.School),
                new SqlParameter("@id_t", entity.TeamId)
            };
            command.Parameters.AddRange(param.ToArray());
            return command;
        }

        public Player ToEntity(DbDataReader reader)
        {
            return new Player()
            {
                Id = int.Parse(reader["id_s"].ToString()),
                Name = reader["name"].ToString(),
                School = reader["school"].ToString(),
                TeamId = int.Parse(reader["id_t"].ToString())
            };
        }
        

        public override DbCommand UpdateCommand(Player entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "UPDATE Player SET name = @name, school = @school, id_t = @id_t WHERE id_s = @id";
            List<SqlParameter> param = new List<SqlParameter>() {
                new SqlParameter("@id", entity.Id),
                new SqlParameter("@name", entity.Name),
                new SqlParameter("@school", entity.School),
                new SqlParameter("@id_t", entity.TeamId)
            };
            command.Parameters.AddRange(param.ToArray());
            return command;
        }

        internal List<Player> FindAllGameActive(int gameId, int teamId)
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM Player P INNER JOIN ActivePlayer AP ON P.id = AP.id_s INNER JOIN Game G ON G.id_g = AP.id_g WHERE G.id_g=@id AND P.id_t =@idT";
            List<SqlParameter> p = new List<SqlParameter>(){
                new SqlParameter("@id", gameId),
                new SqlParameter("@idT", teamId)};
            command.Parameters.AddRange(p.ToArray());
            List<Player> entities = new List<Player>();
            using (DbDataReader reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    Player e = ToEntity(reader);
                    entities.Add(e);
                }
            }
            return entities;
        }
    }
}
