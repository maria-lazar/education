from domain.student import Student
from domain.validator import StudentValidator
from errors import StudentError

class StudentRepository:
    def __init__(self):
        self.students = []

    def find_all(self):
        '''
        find_all -> list of Students
        Returns the list of Students
        '''
        return self.students

    def save(self, student):
        '''
        save -> None
        Searches if the given student id exists in the list of Students, if not adds the student to the list
        If the id already exists, raises StudentError
        :param student: Student
        '''
        index = self.find_index(student)
        if index == -1:
            self.students.append(student)
        else:
            raise StudentError("Duplicated id's")

    def update(self, student):
        '''
        update_course -> None
        Searches if the given student id exists in the list of Students, if it exists, replaces the existing student with the given student
        If the id doesn't exist, raises StudentError
        :param student: Student
        '''
        index = self.find_index(student)
        if index != -1:
            self.students[index] = student
        else:
            raise StudentError("Student not found")

    def delete(self, id):
        '''
        delete -> None
        Searches if the given id exists in the list of Students, if it exists, deletes the student with the given id
        If the id doesn't exist, raises StudentError
        :param id: int
        '''
        s = self.find_by_id(id)
        if s != -1:
            index = self.find_index(s)
            del self.students[index]
        else:
            raise StudentError("Student not found")

    def find(self, id):
        '''
        find -> Student
        Returns the Student from the list of students that has the same id as the student given, if it exists,
        if not, raises StudentError
        :param id: int
        '''
        s = self.find_by_id(id)
        if s != -1:
            return s
        else:
            raise StudentError("Student not found")

    def find_by_id(self, id):
        '''
        find_id -> student or -1
        Returns student from the list of students that has the student id = id,
        -1 if there isn't one
        :param id: int
        '''
        '''
        for i in range(0, len(self.students)):
            if id == self.students[i].get_id():
                return self.students[i]
        return -1
        '''
        return self.find_by_id_recursive(id,self.students)

    def find_by_id_recursive(self, id, students):
        if len(students) == 0:
            return -1
        else:
            if id == students[0].get_id():
                return students[0]
            else:
                return self.find_by_id_recursive(id, students[1:])

    def find_index(self, student):
        '''
        find_index ->  int
        Returns the position in the list of students of the given student,
        -1 if there isn't one
        :param student: Student
        '''
        for i in range(0, len(self.students)):
            if student.get_id() == self.students[i].get_id():
                return i
        return -1

    def find_by_name(self, name):
        '''
        find_by_name ->  list of Students
        Returns the list of students with the given name,
        raises StudentError if there isn't one
        :param name: string
        '''
        students = []
        for i in range(0, len(self.students)):
            if name == self.students[i].get_name():
                students.append(self.students[i])
        if students == []:
            raise StudentError("No student found")
        return students

class StudentFileRepository(StudentRepository):

    def __init__(self):
        StudentRepository.__init__(self)
        self.validator = StudentValidator()
        self.students = self.read_from_file()

    def read_from_file(self):
        '''
        read_from_file -> list of Students
        Reads information about students from a file and puts it in a list,
        raises IOError if the file is corrupted
        '''
        students = []
        try:
            f = open("data/students", "r")
            line = f.readline()
            while line != "":
                object = line.split(",")
                student = Student(int(object[0]), object[1])
                for i in range(0, len(students)):
                    if int(object[0]) == students[i].get_id():
                        raise IOError
                self.validator.validate(student)
                students.append(student)
                line = f.readline()
            f.close()
            return students
        except IOError:
            raise ValueError("Cannot read students")

    def save_to_file(self):
        '''
        save_to_file -> None
        Saves into a file information about the existing students,
        raises IOError if the file is corrupted
        '''
        try:
            f = open("data/students", "w")
            for i in range(0, len(self.students)):
                s = str(self.students[i].get_id()) + "," + self.students[i].get_name() + ",\n"
                f.write(s)
            f.close()
        except IOError:
            raise ValueError("Cannot save courses")

    def save(self, student):
        StudentRepository.save(self, student)
        self.save_to_file()

    def update(self, student):
        StudentRepository.update(self, student)
        self.save_to_file()

    def delete(self, id):
        StudentRepository.delete(self, id)
        self.save_to_file()