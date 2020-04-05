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
    public delegate E ToEntity<E>(DbDataReader reader);

    abstract class SqlRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
    {
        public ToEntity<E> ConvertToEntity;
        //public abstract E ToEntity(DbDataReader reader);

        public abstract DbCommand SaveCommand(E entity);
        public abstract DbCommand UpdateCommand(E entity);
        public abstract DbCommand FindOneCommand(ID id);
        public abstract DbCommand DeleteCommand(ID id);

        private string tableName;

        public string TableName { get => tableName; set => tableName = value; }

        public DbCommand NewCommand()
        {
            SqlConnectionFactory connectionFactory = SqlConnectionFactory.GetInstance();
            DbCommand command = connectionFactory.Factory.CreateCommand();
            command.Connection = connectionFactory.Connection;
            return command;
        }

        public E Save(E entity)
        {
            DbCommand command = SaveCommand(entity);
            if (FindOne(entity.Id) == null)
            {
                command.ExecuteNonQuery();
                return null;
            }
            return FindOne(entity.Id);
        }

        public E Update(E entity)
        {
            DbCommand command = UpdateCommand(entity);
            if (FindOne(entity.Id) == null)
            {
                return entity;
            }
            command.ExecuteNonQuery();
            return null;
        }

        public E Delete(ID id)
        {
            if (id == null)
            {
                throw new ArgumentNullException("Id must be not null");
            }
            DbCommand command = DeleteCommand(id);
            E e = FindOne(id);
            if (command.ExecuteNonQuery() == 0)
            {
                return null;
            }
            return e;
        }

        
        public IEnumerable<E> FindAll()
        {
            DbCommand command = NewCommand();
            command.CommandText = "SELECT * FROM " + TableName;
            List<E> entities = new List<E>();
            using (DbDataReader reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    E e = ConvertToEntity(reader);
                    entities.Add(e);
                }
            }
            return entities;
        }

        

        public E FindOne(ID id)
        {
            if (id == null)
            {
                throw new ArgumentNullException("Id must be not null");
            }
            DbCommand command = FindOneCommand(id);
            E e = null;
            using (DbDataReader reader = command.ExecuteReader())
            {
                if (reader.Read())
                {
                    e = ConvertToEntity(reader);
                }
            }
            return e;
        }
    }
}
