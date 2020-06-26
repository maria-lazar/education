using TripCommon.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TripCommon.Domain;

namespace TripCommon.Repository
{
    interface CrudRepository<ID, E> where E: Entity<ID>
    {
        E FindOne(ID id);

        IList<E> FindAll();

        E Save(E e);

        E Delete(ID id);

        int Size();

    }
}
