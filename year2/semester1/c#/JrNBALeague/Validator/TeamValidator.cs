using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static JrNBALeague.domain.Team;

namespace JrNBALeague.Validator
{
    class TeamValidator : IValidator<Team>
    {
        public void Validate(Team entity)
        {
            TeamName name;
            String n = entity.Name.Replace(" ", "");

            if (!Enum.TryParse<TeamName>(n, out name))
            {
                throw new ValidationException("Not a team name");
            }
        }
    }
}
