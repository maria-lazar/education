using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Validator
{
    class PlayerValidator : IValidator<Player>
    {
        public void Validate(Player entity)
        {
            SchoolName name;
            String s = entity.School;
            s = s.Replace(" ", "");
            if (!Enum.TryParse<SchoolName>(s, out name))
            {
                throw new ValidationException("Not a school name");
            }
        }
    }
}
