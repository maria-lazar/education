using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.domain
{
    class Game: Entity<int>
    {
        private int firstTeamId;
        private int secondTeamId;
        private DateTime date;

        public Game(int firstTeam, int secondTeam, DateTime date)
        {
            FirstTeamId = firstTeam;
            SecondTeamId = secondTeam;
            Date = date;
        }

        public Game()
        {
        }
        public DateTime Date { get => date; set => date = value; }
        public int FirstTeamId { get => firstTeamId; set => firstTeamId = value; }
        public int SecondTeamId { get => secondTeamId; set => secondTeamId = value; }

        public override string ToString()
        {
            return firstTeamId + " " + secondTeamId + " " + date;
        }
    }
}
