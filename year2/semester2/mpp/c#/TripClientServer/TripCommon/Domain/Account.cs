using System;
using TripCommon.Domain;

namespace TripCommon.domain
{
    public class Account: Entity<int>
    {
        private String name; 
        private String password;

        public override string ToString()
        {
            return Id + " " + name + " " + password;
        }

        public Account()
        {
        }
        public Account(int id, string name, string password)
        {
            this.Id = id;
            this.Name = name;
            this.Password = password;
        }
        public Account(string name, string password)
        {
            this.Name = name;
            this.Password = password;
        }

        public string Name { get => name; set => name = value; }
        public string Password { get => password; set => password = value; }
    }
}
