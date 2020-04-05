using JrNBALeague.domain;
using JrNBALeague.Repository;
using JrNBALeague.Validator;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.Service
{
    class TeamService
    {
        private TeamRepository repository;
        private TeamValidator validator;

        public TeamService(TeamValidator v, TeamRepository repository)
        {
            this.repository = repository;
            this.validator = v;
        }

        public IEnumerable<Team> GetAll()
        {
            return repository.FindAll();
        }

        public Team GetTeam(int id)
        {
            return repository.FindOne(id);
        }

        public Team Remove(int id)
        {
            return repository.Delete(id);
        }

        public Team Add(Team t)
        {
            validator.Validate(t);
            return repository.Save(t);
        }

        public Team Update(Team t)
        {
            validator.Validate(t);
            return repository.Update(t);
        }
    }
}
