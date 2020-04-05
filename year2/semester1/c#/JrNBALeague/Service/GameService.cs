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
    class GameService
    {
        private GameRepository repository;
        private TeamRepository teamRepository;
        private GameValidator validator;

        public GameService(GameRepository repository, TeamRepository teamRepository, GameValidator validator)
        {
            this.repository = repository;
            this.teamRepository = teamRepository;
            this.validator = validator;
        }

        public IEnumerable<Game> GetAll()
        {
            return repository.FindAll();
        }

        public List<Game> GetBetween(DateTime date1, DateTime date2)
        {
            DateTime aux;
            if (date1.CompareTo(date2) > 0)
            {
                aux = date1;
                date1 = date2;
                date2 = aux;
            }
            return repository.FindAll().Where(g => (g.Date.CompareTo(date1) > 0 && g.Date.CompareTo(date2) < 0)).ToList();
        }
    }
}
