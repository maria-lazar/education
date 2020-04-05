using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.domain
{
    class ActivePlayerId
    {
        private int idPlayer;
        private int idGame;

        public ActivePlayerId(int idPlayer, int idGame)
        {
            IdPlayer = idPlayer;
            IdGame = idGame;
        }

        public int IdPlayer { get => idPlayer; set => idPlayer = value; }
        public int IdGame { get => idGame; set => idGame = value; }
    }
}
