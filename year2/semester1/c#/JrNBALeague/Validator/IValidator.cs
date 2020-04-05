using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Validator
{
    interface IValidator<E>
    {
        void Validate(E entity);
    }
}
