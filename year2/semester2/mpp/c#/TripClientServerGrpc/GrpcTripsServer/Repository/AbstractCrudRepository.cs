//using log4net;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using TripCommon.Domain;
using TripServer.Db;

namespace TripServer.Repository
{
    abstract class AbstractCrudRepository<ID, E> : CrudRepository<ID, E> where E : Entity<ID>
    {

        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
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
            Logger.Info("delete entity with id " + id);
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
                    Logger.Info("delete entity with id " + id + "failed");
                    throw new RepositoryException("Deleting entity failed");
                }
                Logger.Info("entity deleted " + elem);
                return elem;
            }
        }

        

        public IList<E> FindAll()
        {
            Logger.Info("finding all entities");
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
            Logger.Info("found all entities");
            return elems;

        }

        public E FindOne(ID id)
        {
            Logger.InfoFormat("finding entity with id {0}", id);
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
                        Logger.InfoFormat("found entity {0}", elem);
                        return elem;
                    }
                }
            }
            Logger.InfoFormat("no entity found with id {0}", id);
            return null;
        }

        public E Save(E e)
        {

            Logger.Info("saving entity " + e);
            if (e == null)
            {
                Logger.Info("saving entity failed null entity");
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
                        Logger.Info("saving entity failed");
                        throw new RepositoryException("Saving entity failed");
                    }
                    SetId(e, result);
                    Logger.Info("inserted entity " + e);
                    return e;
                }
                Logger.Info("updating entity " + e);
                command.CommandText = UpdateSql();
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = e.Id;
                command.Parameters.Add(paramId);
                int lines = command.ExecuteNonQuery();
                if (lines == 0)
                {
                    Logger.Info("updating entity failed");
                    throw new RepositoryException("Updating entity failed");
                }
                Logger.Info("updated entity " + e);
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
