using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Repository
{
    class InMemoryRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
    {
        private Dictionary<ID, E> entities;

        public InMemoryRepository()
        {
            entities = new Dictionary<ID, E>();
        }

        public E Delete(ID id)
        {
            if (id == null)
            {
                throw new NullReferenceException("Id must be not null");
            }
            if (entities.ContainsKey(id))
            {
                E entity = entities[id];
                entities.Remove(id);
                return entity;
            }
            return null;
        }

        public IEnumerable<E> FindAll()
        {
            return entities.Values;
        }

        public E FindOne(ID id)
        {
            if (id == null)
            {
                throw new NullReferenceException("Id must be not null");
            }
            if (entities.ContainsKey(id))
            {
                return entities[id];
            }
            return null;
        }

        public E Save(E entity)
        {
            if (entity == null)
            {
                throw new NullReferenceException("Entity must be not null");
            }
            if (entities.ContainsKey(entity.Id))
            {
                return entities[entity.Id];
            }
            entities.Add(entity.Id, entity);
            return null;
        }

        public E Update(E entity)
        {
            if (entity == null)
            {
                throw new NullReferenceException("Entity must be not null");
            }
            if (!entities.ContainsKey(entity.Id))
            {
                return entity;
            }
            entities[entity.Id] = entity;
            return null;
        }
    }
}
