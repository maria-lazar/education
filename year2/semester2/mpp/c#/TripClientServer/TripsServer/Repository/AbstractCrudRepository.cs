//using log4net;
using log4net;
using TripCommon.Db;
using TripCommon.domain;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TripCommon.Domain;

namespace TripCommon.Repository
{
    abstract class AbstractCrudRepository<ID, E> : CrudRepository<ID, E> where E : Entity<ID>
    {

        private static readonly ILog LOGGER = LogManager.GetLogger("AbstractCrudRepository");
        protected abstract String FindAllSql();
        protected abstract E ToEntity(IDataReader dataReader);
        protected abstract string DeleteSql();
        protected abstract string FindOneSql();
        protected abstract void AddInsertParams(IDbCommand command, E e);
        protected abstract bool IsValidId(E e);
        protected abstract String UpdateSql();
        protected abstract void SetId(E e, ulong result);
        protected abstract string InsertSql();
        protected abstract string SizeSql();

        public E Delete(ID id)
        {
            LOGGER.Info("delete entity with id " + id);
            IDbConnection connection = DbUtils.GetConnection();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = DeleteSql();
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);
                E elem = FindOne(id);
                int lines = command.ExecuteNonQuery();
                if (lines == 0)
                {
                    LOGGER.Info("delete entity with id " + id + "failed");
                    throw new RepositoryException("Deleting entity failed");
                }
                LOGGER.Info("entity deleted " + elem);
                return elem;
            }
        }

        

        public IList<E> FindAll()
        {
            LOGGER.Info("finding all entities");
            IDbConnection connection = DbUtils.GetConnection();
            IList<E> elems = new List<E>();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = FindAllSql();
                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {                  
                        elems.Add(ToEntity(dataReader));
                    }
                }
            }
            LOGGER.Info("found all entities");
            return elems;

        }

        public E FindOne(ID id)
        {
            LOGGER.InfoFormat("finding entity with id {0}", id);
            IDbConnection connection = DbUtils.GetConnection();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = FindOneSql();
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        E elem = ToEntity(dataReader);
                        LOGGER.InfoFormat("found entity {0}", elem);
                        return elem;
                    }
                }
            }
            LOGGER.InfoFormat("no entity found with id {0}", id);
            return null;
        }

        public E Save(E e)
        {

            LOGGER.Info("saving entity " + e);
            if (e == null)
            {
                LOGGER.Info("saving entity failed null entity");
                throw new ArgumentNullException("Entity to save cannot be null");
            }
            IDbConnection connection = DbUtils.GetConnection();
            using (var command = connection.CreateCommand())
            {
                AddInsertParams(command, e);
                if (!IsValidId(e))
                {
                    command.CommandText = InsertSql();
                    ulong result = (ulong)command.ExecuteScalar();
                    if (result == 0)
                    {
                        LOGGER.Info("saving entity failed");
                        throw new RepositoryException("Saving entity failed");
                    }
                    SetId(e, result);
                    LOGGER.Info("inserted entity " + e);
                    return e;
                }
                LOGGER.Info("updating entity " + e);
                command.CommandText = UpdateSql();
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = e.Id;
                command.Parameters.Add(paramId);
                int lines = command.ExecuteNonQuery();
                if (lines == 0)
                {
                    LOGGER.Info("updating entity failed");
                    throw new RepositoryException("Updating entity failed");
                }
                LOGGER.Info("updated entity " + e);
                return e;
            }
        }


        public int Size()
        {
            IDbConnection connection = DbUtils.GetConnection();
            using (var command = connection.CreateCommand())
            {
                command.CommandText = SizeSql();
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        return (int)dataReader.GetInt64(0);
                    }
                }
            }
            throw new RepositoryException("Getting size failed");
        }

    }
}
