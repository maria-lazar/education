using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JrNBALeague.domain
{
    public enum School
    {
       
    }
    class Student: Entity<int>
    {
        private string name;
        private string school;

        public Student(string name, string school)
        {
            Name = name;
            School = school;
        }

        public Student()
        {
        }

        public override string ToString()
        {
            return "Name: " + name + " School: " + school;
        }

        public string Name { get => name; set => name = value; }
        public string School { get => school; set => school = value; }
    }
}
