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
    class ActivePlayerService
    {
        private ActivePlayerRepository repository;
        private PlayerRepository playerRepository;
        private GameRepository gameRepository;

        public ActivePlayerService(ActivePlayerRepository repository, PlayerRepository playerRepository, GameRepository gameRepository)
        {
            this.repository = repository;
            this.playerRepository = playerRepository;
            this.gameRepository = gameRepository;
        }

        public List<Player> FindPlayersGameTeam(int teamId, int gameId)
        {
            Game game = gameRepository.FindOne(gameId);
            if (game == null)
            {
                throw new ValidationException("Inexistent game");
            }
            if (game.FirstTeamId != teamId && game.SecondTeamId != teamId)
            {
                throw new ValidationException("Team wasn't involved in the game");
            }
            return repository.FindAll()
                    .Where(a => (a.Id.IdGame == gameId && playerRepository.FindOne(a.Id.IdPlayer).TeamId == teamId))
                    .Select(a => playerRepository.FindOne(a.Id.IdPlayer))
                    .ToList();
        }
        public IEnumerable<KeyValuePair<int, int>> GetScore(int gameId)
        {
            if (gameRepository.FindOne(gameId).Date.CompareTo(DateTime.Now) > 0)
            {
                throw new ValidationException("Game hasn't started");
            }
            return from activeplayer in repository.FindAll()
                    where activeplayer.Id.IdGame == gameId && activeplayer.Type == PlayerType.active
                    group activeplayer by playerRepository.FindOne(activeplayer.Id.IdPlayer).TeamId into team
                    select new KeyValuePair<int, int>(team.Key, team.Sum(p => p.Points));
        }
    }
}
