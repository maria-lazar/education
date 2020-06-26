
using System;

namespace TripCommon.Dto
{
    [Serializable]
    public class AccountDto
    {
        public int id;
        public string name;
        public string password;

        public AccountDto(int accountId)
        {
            this.id = accountId;
        }

        public AccountDto(string name, string password)
        {
            this.name = name;
            this.password = password;
        }

        public AccountDto(int id, string name, string password)
        {
            this.id = id;
            this.name = name;
            this.password = password;
        }

        public override string ToString()
        {
            return "AccountDto{ id=" + id + ", name=" + name + ", password=" + password + "}";
        }
    }
}
