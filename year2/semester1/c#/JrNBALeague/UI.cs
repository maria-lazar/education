using JrNBALeague.domain;
using JrNBALeague.Service;
using JrNBALeague.Validator;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague
{
    class UI
    {
        private TeamService teamService;
        private PlayerService playerService;
        private GameService gameService;
        private ActivePlayerService activePlayerService;

        public UI(TeamService teamService, PlayerService playerService, GameService gameService, ActivePlayerService activePlayerService)
        {
            this.teamService = teamService;
            this.playerService = playerService;
            this.gameService = gameService;
            this.activePlayerService = activePlayerService;
        }

        private void PrintMenu()
        {
            Console.WriteLine("0 - Exit");
            Console.WriteLine("1 - Team players");
            Console.WriteLine("2 - Active players from a game");
            Console.WriteLine("3 - Games between 2 dates");
            Console.WriteLine("4 - Game score");
        }

        public void Run()
        {
            PrintMenu();
            Console.Write("> ");
            string command = Console.ReadLine();
            while (command != "0")
            {
                switch (command)
                {
                    case "1":
                        TeamPlayers();
                        break;
                    case "2":
                        ActivePlayersFromTeamGame();
                        break;
                    case "3":
                        GamesBetween();
                        break;
                    case "4":
                        GameScore();
                        break;
                    default:
                        Console.WriteLine("Invalid command");
                        break;
                }
                PrintMenu();
                Console.Write("> ");
                command = Console.ReadLine();
            }
        }


        private void GameScore()
        {
            try
            {
                List<Game> games = gameService.GetAll().ToList();
                List<Team> teams = teamService.GetAll().ToList();
                PrintGames(games, teams);
                Console.WriteLine("Game Number: ");
                Game g = games[int.Parse(Console.ReadLine()) - 1];
                int gameId = g.Id;
                List<KeyValuePair<int, int>> score = activePlayerService.GetScore(gameId).ToList();
                if (score.Count() < 2)
                {
                    Console.WriteLine("No score");
                    return;
                }
                Team firstTeam = teams.Find(t => t.Id == score.ElementAt(0).Key);
                Team secondTeam = teams.Find(t => t.Id == score.ElementAt(1).Key);
                Console.WriteLine(firstTeam + " : " + secondTeam);
                Console.WriteLine(score.ElementAt(0).Value + " : " + score.ElementAt(1).Value);
            }
            catch (FormatException)
            {
                Console.WriteLine("Invalid number");
            }
            catch (ArgumentOutOfRangeException)
            {
                Console.WriteLine("Invalid game number");
            }
            catch(ValidationException ve)
            {
                Console.WriteLine(ve.Message);
            }
        }

        private void GamesBetween()
        {
            try
            {
                //gameService.GetAll().ToList().ForEach(Console.WriteLine);
                Console.WriteLine("Date(yyyy-mm-dd): ");
                DateTime date1 = DateTime.Parse(Console.ReadLine());
                Console.WriteLine("Date(yyyy-mm-dd): ");
                DateTime date2 = DateTime.Parse(Console.ReadLine());
                List<Game> games = gameService.GetBetween(date1, date2);
                if (games.Count == 0)
                {
                    Console.WriteLine("No games");
                    return;
                }
                PrintGames(games, teamService.GetAll().ToList());
            }
            catch (FormatException)
            {
                Console.WriteLine("Invalid date");
            }
        }

        private void ActivePlayersFromTeamGame()
        {
            try
            {
                List<Team> teams = teamService.GetAll().ToList();
                PrintTeams(teams);
                Console.WriteLine("Team Number: ");
                int teamId = teams[int.Parse(Console.ReadLine()) - 1].Id;
                List<Game> games = gameService.GetAll().ToList();
                PrintGames(games, teams);
                Console.WriteLine("Game Number: ");
                int gameId = games[int.Parse(Console.ReadLine()) - 1].Id;
                //playerRepo.FindAllGameActive(gameId, teamId).ForEach(Console.WriteLine);
                activePlayerService.FindPlayersGameTeam(teamId, gameId).ForEach(Console.WriteLine);
            }
            catch (ValidationException ve)
            {
                Console.WriteLine(ve.Message);
            }
            catch (ArgumentOutOfRangeException)
            {
                Console.WriteLine("Invalid index number");
            }
            catch (FormatException)
            {
                Console.WriteLine("Invalid number");
            }
        }

        private void PrintGames(List<Game> games, List<Team> teams)
        {
            int k = 1;
            games.ForEach(g =>
            {
                string firstTeam = teams.Find(team => team.Id == g.FirstTeamId).Name.Replace("\r", "");
                string secondTeam = teams.Find(team => team.Id == g.SecondTeamId).Name.Replace("\r", "");
                Console.WriteLine(k + ". " + firstTeam + " vs " + secondTeam + " " + g.Date);
                k++;
            });
        }
        private void PrintPlayers(List<Player> players, List<Team> teams)
        {
            int k = 1;
            players.ForEach(p =>
            {
                string team = teams.Find(t => p.TeamId == t.Id).Name;
                Console.WriteLine(k + ". " + p.Name + " " + team);
                k++;
            });
        }

        private void PrintTeams(List<Team> teams)
        {
            int k = 1;
            teams.ForEach(t =>
            {
                Console.WriteLine(k + ". " + t);
                k++;
            });
        }

        private void TeamPlayers()
        {
            List<Team> teams = teamService.GetAll().ToList();
            PrintTeams(teams);
            Console.WriteLine("Team Number: ");
            try
            {
                int teamId = teams[int.Parse(Console.ReadLine()) - 1].Id;
                List<Player> players = playerService.GetPlayersTeam(teamId);
                if (players.Count == 0)
                {
                    Console.WriteLine("No players");
                    return;
                }
                players.ForEach(Console.WriteLine);
            }catch(ValidationException ve)
            {
                Console.WriteLine(ve.Message);
            }catch(ArgumentOutOfRangeException)
            {
                Console.WriteLine("Invalid team number");
            }
            catch (FormatException)
            {
                Console.WriteLine("Invalid number");
            }
        }
    }
}
