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
    class PlayerService
    {
        private PlayerRepository repository;
        private TeamRepository teamRepository;
        private PlayerValidator validator;

        public PlayerService(PlayerRepository repository, TeamRepository r, PlayerValidator validator)
        {
            this.repository = repository;
            this.validator = validator;
            this.teamRepository = r;
        }

        public IEnumerable<Player> GetAll()
        {
            return repository.FindAll();
        }

        public Player GetPlayer(int id)
        {
            return repository.FindOne(id);
        }

        public Player Remove(int id)
        {
            return repository.Delete(id);
        }

        public Player Add(Player player)
        {
            validator.Validate(player);
            return repository.Save(player);
        }

        public List<Player> GetPlayersTeam(int teamId)
        {
            if (teamRepository.FindOne(teamId) == null)
            {
                throw new ValidationException("Inexistent team");
            }
            return repository.FindAll().Where(p => p.TeamId == teamId).ToList();
        }

        public Player Update(Player player)
        {
            validator.Validate(player);
            return repository.Update(player);
        }
    }
}
