using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace JrNBALeague.domain
{
    class Team: Entity<int>
    {
        public enum TeamName
        {
            HoustonRockets,
            LosAngelesLakers,
            LAClippers,
            ChicagoBulls,
            ClevelandCavaliers,
            UtahJazz,
            BrooklynNets,
            NewOrleansPelicans,
            IndianaPacers,
            TorontoRaptors,
            CharlotteHornets,
            PhoenixSuns,
            PortlandTrailBlazers,
            GoldenStateWarriors,
            WashingtonWizards,
            SanAntonioSpurs,
            OrlandoMagic,
            DenverNuggets,
            DetroitPistons,
            AtlantaHawks,
            DallasMavericks,
            SacramentoKings,
            OklahomaCityThunder,
            BostonCeltics,
            NewYorkKnicks,
            MinnesotaTimberwolves,
            MiamiHeat,
            MilwaukeeBucks
        }

        private string name;

        internal string Name { get => name; set => name = value; }

        public Team(string name)
        {
            Name = name;
        }

        public Team()
        {
        }

        public override string ToString()
        {
            return Name;
        }
    }
}
