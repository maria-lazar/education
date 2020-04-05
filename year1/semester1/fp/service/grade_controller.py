from core.merge_sort import merge_sort
from core.bingo_sort import bingo_sort
from domain.course import Course
from domain.grade import Grade
from domain.student import Student
from domain.validator import CourseValidator, StudentValidator, GradeValidator
from errors import GradeError


class GradeController:

    def __init__(self, course_repository, student_repository, grade_repository):
        self.course_repository = course_repository
        self.student_repository = student_repository
        self.grade_repository = grade_repository
        self.course_validator = CourseValidator()
        self.student_validator = StudentValidator()
        self.grade_validator = GradeValidator()

    def create(self, student, course, grade):
        '''
        create -> Grade
        Creates and returns a Grade that is assigned to the given student at the given course
        :param student: Student
        :param course: Course
        :param grade: int
        '''
        grade = Grade(student, course, grade)
        return grade

    def add(self, student_id, course_id, grade):
        '''
        add -> None
        Adds a grade to the list of grades,
        if the Student doesn't exist raises StudentError,
        if the Course doesn't exist raises CourseError
        :param student_id: int
        :param course_id: int
        :param grade: float
        '''
        student = self.student_repository.find_by_id(student_id)
        if student == -1:
            raise GradeError("Student not found")
        course = self.course_repository.find_by_id(course_id)
        if course == -1:
            raise GradeError("Course not found")
        grade = self.create(student, course, grade)
        self.grade_validator.validate(grade)
        self.grade_repository.save(grade)

    def get_all(self):
        '''
        get_all -> list of grades
        Returns the list of grades
        '''
        return self.grade_repository.find_all()

    def update_course(self, id, name, professor):
        '''
        update_course -> None
        Updates a Course from the list of courses
        If the course doesn't exist or there id a validation error, raises CourseError
        :param id: int
        :param name: string
        :param professor: string
        '''
        course = Course(id, name, professor)
        self.course_validator.validate(course)
        self.course_repository.update(course)
        grades_to_update = self.grade_repository.find_by_course(course.get_id())
        for i in range(0, len(grades_to_update)):
            student = self.student_repository.find_by_id(grades_to_update[i].get_id_student())
            grade = grades_to_update[i].get_grade()
            grade = self.create(student, course, grade)
            self.grade_repository.update(grade)


    def update_student(self, id, name):
        '''
        update_student -> None
        Updates a Student from the list of students
        If the Student doesn't exist or there is a validation error, raises StudentError
        :param id: int
        :param name: string
        '''
        student = Student(id, name)
        self.student_validator.validate(student)
        self.student_repository.update(student)
        grades_to_update = self.grade_repository.find_by_student(student.get_id())
        for i in range(0, len(grades_to_update)):
            course = self.course_repository.find_by_id(grades_to_update[i].get_id_course())
            grade = grades_to_update[i].get_grade()
            grade = self.create(student, course, grade)
            self.grade_repository.update(grade)

    def update_grade(self, student_id, course_id, grade):
        '''
        update_grade -> None
        Updates a Grade from the list of grades
        If the Grade doesn't exist, raises GradeError
        :param student_id: int
        :param course_id: int
        :param grade: float
        '''
        student = self.student_repository.find_by_id(student_id)
        if student == -1:
            raise GradeError("Student not found")
        course = self.course_repository.find_by_id(course_id)
        if course == -1:
            raise GradeError("Course not found")
        grade = self.create(student, course, grade)
        self.grade_validator.validate(grade)
        self.grade_repository.update(grade)

    def remove_course(self, id):
        '''
        remove_course -> None
        Removes the Course with th id=id from the list of courses
        If the course doesn't exist, raises CourseError
        :param id: int
        '''
        self.course_repository.delete(id)
        grades_to_delete = self.grade_repository.find_by_course(id)
        for i in range(0, len(grades_to_delete)):
            self.grade_repository.delete(grades_to_delete[i].get_id_student(), id)

    def remove_student(self, id):
        '''
        remove_course -> None
        Removes a Student from the list of students
        If the Student doesn't exist, raises StudentError
        :param id: int
        '''
        self.student_repository.delete(id)
        grades_to_delete = self.grade_repository.find_by_student(id)
        for i in range(0, len(grades_to_delete)):
            self.grade_repository.delete(id, grades_to_delete[i].get_id_course())

    def remove_grade(self, student_id, course_id):
        '''
        remove_grade -> None
        Removes a Grade from the list of grades
        If the Grade doesn't exist, raises GradeError
        :param student_id: int
        :param course_id: int
        '''
        self.grade_repository.delete(student_id, course_id)

    def search_course_grades(self, course_id):
        '''
        search_course_grades -> list of DTOGrades
        Returns the list of grades with the same course id as the one given, ordered by student name and grade value
        :param course_id: int
        '''
        dto_grades = self.grade_repository.find_by_course(course_id)
        if len(dto_grades) != 0:
            bingo_sort(dto_grades, key = lambda grade: grade.get_grade(), reverse = True)
            bingo_sort(dto_grades, key = lambda grade: grade.get_student_name())
        return dto_grades

    def search_student_grades(self, student_id):
        '''
        search_student_grades -> list of DTOGrades
        Returns the list of grades with the same student id as the one given
        :param student_id: int
        '''
        dto_grades = self.grade_repository.find_by_student(student_id)
        return dto_grades

    def search_highest_grades(self):
        '''
        search_highest_grade -> list of tuples containing a student and an int
        Returns the first 20% of the students that have the highest overall grades and their overall grade
        '''
        overall_grade_students = []
        students = self.student_repository.find_all()
        for i in range(0, len(students)):
            grades_student = self.search_student_grades(students[i].get_id())
            if len(grades_student) != 0:
                overall_grade = 0
                for grade in range(0, len(grades_student)):
                    overall_grade = overall_grade + grades_student[grade].get_grade()
                overall_grade = float(overall_grade) / len(grades_student)
                overall_grade_students.append((students[i], overall_grade))
        merge_sort(overall_grade_students, key = lambda student: student[1], reverse = True)
        if int(len(students)/5) == 0:
            number_of_students = 1
        else:
            number_of_students = int(len(students)/5)
        return overall_grade_students[:number_of_students]

    def failed_students(self):
        '''
        failed_students -> list of lists containing a string and an int
        Returns a list containing each professor and the number of failed students at their course
        '''
        courses = self.course_repository.find_all()
        failed_per_prof = []
        for i in range(0, len(courses)):
            grades_course = self.grade_repository.find_by_course(courses[i].get_id())
            failed = 0
            for j in range(0, len(grades_course)):
                if grades_course[j].get_grade() < 5:
                    failed = failed + 1
            found = False
            for j in range(0,len(failed_per_prof)):
                if failed_per_prof[j][0] == courses[i].get_professor():
                    failed_per_prof[j][1] = failed_per_prof[j][1] + failed
                    found = True
            if found == False:
                failed_per_prof.append([courses[i].get_professor(), failed])
        return failed_per_prof
