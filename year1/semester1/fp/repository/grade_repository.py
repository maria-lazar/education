from domain.grade import Grade
from domain.gradeDTO import GradeDTO
from domain.validator import GradeValidator
from errors import GradeError

class GradeRepository:

    def __init__(self):
        self.grades = []

    def save(self, grade):
        '''
        save -> None
        Searches if the given grade exists in the list of Grades, if not adds the grade to the list
        If the grade already exists, raises GradeError
        :param grade: Grade
        '''
        index = self.find_index(grade)
        if index == -1:
            self.grades.append(grade)
        else:
            raise GradeError("Grade already exists")

    def update(self, grade):
        '''
        update -> None
        Searches if the given grade id's exists in the list of Grades, if they exist, replaces the existing grade with the given one
        If the id's don't exist, raises GradeError
        :param grade: Grade
        '''
        index = self.find_index(grade)
        if index != -1:
            self.grades[index] = grade
        else:
            raise GradeError("Grade not found")

    def delete(self, student_id, course_id):
        '''
        delete -> None
        Searches if the given id's exist in the list of Grades, if they exist, deletes the grade with the given id's
        If the id's don't exist, raises GradeError
        :param student_id: int
        :param course_id: int
        '''
        grade = self.find_by_id(student_id, course_id)
        if grade != -1:
            index = self.find_index(grade)
            del self.grades[index]
        else:
            raise GradeError("Grade not found")

    def find_all(self):
        '''
        find_all -> list of DTOGrades
        Returns the list of Grades
        '''
        dto_grades = []
        for i in range(0, len(self.grades)):
            grade = GradeDTO(self.grades[i].get_student().get_id(), self.grades[i].get_course().get_id(), self.grades[i].get_student().get_name(), self.grades[i].get_course().get_name(), self.grades[i].get_course().get_professor(), self.grades[i].get_grade())
            dto_grades.append(grade)
        return dto_grades

    def find_by_id(self, student_id, course_id):
        '''
        find_by_id -> Grade or int
        Returns the grade that has the same course id and student id as the one given,
        -1 if there isn't one
        :param student_id: int
        :param course_id: int
        '''
        for i in range(0, len(self.grades)):
            if student_id == self.grades[i].get_student().get_id() and course_id == self.grades[i].get_course().get_id():
                return self.grades[i]
        return -1

    def find_index(self, grade):
        '''
        find_index -> int
        Returns the list index corresponding to the grade that has the same course id and student id as the one given,
        -1 if there isn't one
        :param grade: Grade
        '''
        for i in range(0, len(self.grades)):
            if grade.get_course().get_id() == self.grades[i].get_course().get_id() and grade.get_student().get_id() == self.grades[i].get_student().get_id():
                return i
        return -1

    def find_by_student(self, id):
        '''
        find_by_student -> list of DTOGrades
        Returns the grades that have the same student id as the one given,
        :param id: int
        '''
        '''
        dto_grades = []
        for i in range(0,len(self.grades)):
            grade = self.grades[i]
            student = grade.get_student()
            if student.get_id() == id:
                course = grade.get_course()
                grade = GradeDTO(student.get_id(), course.get_id(),
                                 student.get_name(), course.get_name(),
                                 course.get_professor(), grade.get_grade())
                dto_grades.append(grade)
        return dto_grades
        '''
        return self.find_by_student_recursive(id, self.grades)

    def find_by_student_recursive(self, id, grades):
        if len(grades) == 0:
            return []
        else:
            grade = grades[0]
            student = grade.get_student()
            if student.get_id() == id:
                course = grade.get_course()
                grade = GradeDTO(student.get_id(), course.get_id(),
                                 student.get_name(), course.get_name(),
                                 course.get_professor(), grade.get_grade())
                return self.find_by_student_recursive(id, grades[1:]) + [grade]
            else:
                return self.find_by_student_recursive(id, grades[1:])

    def find_by_course(self, id):
        '''
        find_by_course -> list of Courses
        Returns the grades that have the same sourse id as the one given,
        :param id: int
        '''
        dto_grades = []
        for i in range(0,len(self.grades)):
            if self.grades[i].get_course().get_id() == id:
                grade = GradeDTO(self.grades[i].get_student().get_id(), self.grades[i].get_course().get_id(),
                                 self.grades[i].get_student().get_name(), self.grades[i].get_course().get_name(),
                                 self.grades[i].get_course().get_professor(), self.grades[i].get_grade())
                dto_grades.append(grade)
        return dto_grades

class GradeFileRepository(GradeRepository):

    def __init__(self, courses, students):
        self.courses = courses
        self.students = students
        GradeRepository.__init__(self)
        self.validator = GradeValidator()
        self.grades = self.read_from_file()

    def read_from_file(self):
        '''
        read_from_file -> list of Grades
        Reads information about grades from a file and puts it in a list,
        raises IOError if the file is corrupted
        '''
        grades = []
        try:
            f = open("data/grades", "r")
            line = f.readline()
            while line != "":
                object = line.split(",")
                student = None
                course = None
                for i in range(0, len(self.students)):
                    if self.students[i].get_id() == int(object[0]):
                        student = self.students[i]
                if student == None:
                    raise IOError
                for i in range(0, len(self.courses)):
                    if self.courses[i].get_id() == int(object[1]):
                        course = self.courses[i]
                if course == None:
                    raise IOError
                gr = float(object[2])
                grade = Grade(student, course, gr)
                self.validator.validate(grade)
                grades.append(grade)
                line = f.readline()
            f.close()
            return grades
        except IOError:
            raise ValueError("Cannot read grades")

    def save_to_file(self):
        '''
        save_to_file -> None
        Saves into a file information about the existing grades,
        raises IOError if the file is corrupted
        '''
        try:
            f = open("data/grades", "w")
            for i in range(0, len(self.grades)):
                s = str(self.grades[i].get_student().get_id())+ "," + str(self.grades[i].get_course().get_id()) +  "," + str(self.grades[i].get_grade()) + ",\n"
                f.write(s)
            f.close()
        except IOError:
            raise ValueError("Cannot save grades")

    def save(self, grade):
        GradeRepository.save(self, grade)
        self.save_to_file()

    def update(self, grade):
        GradeRepository.update(self, grade)
        self.save_to_file()

    def delete(self, student_id, course_id):
        GradeRepository.delete(self, student_id, course_id)
        self.save_to_file()
