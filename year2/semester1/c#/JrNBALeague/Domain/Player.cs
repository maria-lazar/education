using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static JrNBALeague.domain.Team;

namespace JrNBALeague.domain
{
    public enum SchoolName
    {
        ScoalaGimnazialaHorea,
        ScoalaGimnazialaOctavianGoga,
        LiceulTeoreticLucianBlaga,
        ScoalaGimnazialaIoanBob,
        ScoalaGimnazialaIonCreanga,
        ColegiulNationalPedagogicGheorgheLazar,
        ScoalaGimnazialaInternationalaSPECTRUM,
        ColegiulNationalEmilRacovita,
        ColegiulNationalGeorgeCosbuc,
        ScoalaGimnazialaIonAgarbiceanu,
        LiceulTeoreticAvramIancu,
        ScoalaGimnazialaConstantinBrancusi,
        LiceulTeoreticOnisiforGhibu,
        LiceulcuProgramSportivClujNapoca,
        LiceulTeoreticNicolaeBalcescu,
        LiceulTeoreticGheorgheSincai,
        ScoalaNicolaeTitulescu,
        ScoalaGimnazialaLiviuRebreanu,
        ScoalaGimnazialaIuliuHatieganu,
        LiceulTeoreticBathoryIstvan,
        ColegiulNationalGeorgeBaritiu,
        LiceulTeoreticApaczaiCsereJanos,
        SeminarulTeologicOrtodox,
        LiceuldeInformaticaTiberiuPopoviciu,
        ScoalaGimnazialaAlexandruVaidaVoevod,
        LiceulTeoreticELF,
        ScoalaGimnazialaGheorgheSincaiFloresti
    }
    class Player: Student
    {
        private int teamId;

        public Player(int teamId, string name, string school): base(name, school)
        {
            TeamId = teamId;
        }

        public Player()
        {
        }

        public override string ToString()
        {
            return base.ToString();
        }
        
        public int TeamId { get => teamId; set => teamId = value; }
    }
}
