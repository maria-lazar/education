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
    class TeamRepository: SqlRepository<int, Team>
    {

        public TeamRepository()
        {
            TableName = "Team";
            ConvertToEntity = ToEntity;
        }
        public Team ToEntity(DbDataReader reader)
        {
            return new Team()
            {
                Id = int.Parse(reader["id_t"].ToString()),
                Name = reader["name"].ToString()
            };
        }
        

        public override DbCommand SaveCommand(Team entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "INSERT INTO Team(name) VALUES (@name)";
            SqlParameter p = new SqlParameter("@name", entity.Name);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand UpdateCommand(Team entity)
        {
            DbCommand command = NewCommand();
            command.CommandText = "UPDATE Team SET name = @name WHERE id_t = @id";
            SqlParameter p = new SqlParameter("@name", entity.Name);
            SqlParameter p2 = new SqlParameter("@id", entity.Id);
            List<SqlParameter> parameters = new List<SqlParameter>() { p, p2 };
            command.Parameters.AddRange(parameters.ToArray());
            return command;
        }

        public override DbCommand FindOneCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM " + TableName + " WHERE id_t = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }

        public override DbCommand DeleteCommand(int id)
        {
            DbCommand command = NewCommand();
            command.CommandText = "DELETE FROM " + TableName + " WHERE id_t = @id";
            SqlParameter p = new SqlParameter("@id", id);
            command.Parameters.Add(p);
            return command;
        }
        //public List<Team> findByName(string v)
        //{
        //    DbCommand command = NewCommand();
        //    command.CommandText = "SELECT * FROM Team WHERE name=@name";
        //    SqlParameter p = new SqlParameter("@name", v);
        //    command.Parameters.Add(p);
        //    List<Team> teams = new List<Team>();
        //    using (DbDataReader reader = command.ExecuteReader())
        //    {
        //        while (reader.Read())
        //        {
        //            Team t = new Team()
        //            {
        //                Id = int.Parse(reader["id_t"].ToString()),
        //                Name = reader["name"].ToString()
        //            };
        //            teams.Add(t);
        //        }
        //    }
        //    return teams;
        //}
        
    }
}
