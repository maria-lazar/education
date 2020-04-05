using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.domain
{
    public enum PlayerType
    {
        active,
        substitute
    }
    class ActivePlayer: Entity<ActivePlayerId>
    {
        private int points;
        private PlayerType type;

        public ActivePlayer(int points, PlayerType type)
        {
            Points = points;
            Type = type;
        }

        public ActivePlayer()
        {
        }

        public override string ToString()
        {
            return "Player: " + Id.IdPlayer + " Game: " + Id.IdGame + " " + Points + " " + Type;
        }

        public override bool Equals(object obj)
        {
            var player = obj as ActivePlayer;
            return player != null &&
                   Id.IdPlayer == player.Id.IdPlayer &&
                   Id.IdGame == player.Id.IdGame;
        }

        public override int GetHashCode()
        {
            var hashCode = 860376579;
            hashCode = hashCode * -1521134295 + points.GetHashCode();
            hashCode = hashCode * -1521134295 + type.GetHashCode();
            return hashCode;
        }

        public int Points { get => points; set => points = value; }
        public PlayerType Type { get => type; set => type = value; }
    }
}
