using JrNBALeague.domain;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static JrNBALeague.domain.Team;
using System.Data.Common;
using System.Configuration;
using JrNBALeague.Repository;
using JrNBALeague.Validator;
using JrNBALeague.Service;

namespace JrNBALeague
{
    class Program
    {
        static void Main(string[] args)
        {
            TeamRepository teamRepo = new TeamRepository();
            PlayerRepository playerRepo = new PlayerRepository();            
            GameRepository gameRepository = new GameRepository();
            ActivePlayerRepository r = new ActivePlayerRepository();            
            TeamService teamService = new TeamService(new TeamValidator(), teamRepo);
            PlayerService playerService = new PlayerService(playerRepo, teamRepo, new PlayerValidator());
            GameService gameService = new GameService(gameRepository, teamRepo, new GameValidator());
            ActivePlayerService activePlayerService = new ActivePlayerService(r, playerRepo, gameRepository);
            UI ui = new UI(teamService, playerService, gameService, activePlayerService);
            ui.Run();
        }       
    }
}
