from core.cli import Cli
from domain import validator
from domain.validator import validate_name
from errors import StudentError, CourseError
from service import generate


class UI:
    def __init__(self, course_controller, student_controller, grade_controller):
        self.course_controller = course_controller
        self.student_controller = student_controller
        self.grade_controller = grade_controller
        self.cli = Cli()
        course_menu = self.cli.add_menu("Courses")
        course_menu.add("add", "Add course", self.add_course)
        course_menu.add("print", "Print courses", self.print_courses)
        course_menu.add("remove", "Remove course", self.remove_course)
        course_menu.add("update", "Update course", self.update_course)
        course_menu.add("search-id", "Get the course with the given id", self.search_by_id_course)
        course_menu.add("generate", "Add the given number of courses, generated randomly", self.generate_courses)
        student_menu = self.cli.add_menu("Students")
        student_menu.add("add","Add student", self.add_student)
        student_menu.add("print","Print students", self.print_students)
        student_menu.add("remove", "Remove student", self.remove_student)
        student_menu.add("update", "Update student", self.update_student)
        student_menu.add("search-id", "Get the student with the given registration number", self.search_by_id_student)
        student_menu.add("search-name", "Get the students with the given name", self.search_by_name_student)
        student_menu.add("generate", "Add the given number of students, generated randomly", self.generate_students)
        grade_menu = self.cli.add_menu("Grades")
        grade_menu.add("add","Assign grade to a student at a course", self.add_grade)
        grade_menu.add("print", "Print the list of grades", self.print_grades)
        grade_menu.add("remove", "Remove a grade", self.remove_grade)
        grade_menu.add("update", "Update a grade", self.update_grade)
        grade_menu.add("grades-course", "Get the grades at the given course", self.course_grades)
        grade_menu.add("grades-student", "Get the grades of the given student", self.student_grades)
        grade_menu.add("best-students", "Get the students with the highest overall grades", self.best_grades)
        grade_menu.add("failed-per-professor", "Get the number of students that failed at each course", self.failed_st_per_professor)

###Courses

    def read_course(self):
        str_course = raw_input("New course(id, name, professor): ")
        str_course = str_course.split(",")
        if len(str_course) != 3:
            raise CourseError("Not a course")
        try:
            id = int(str_course[0])
        except ValueError:
            raise ValueError("Id must be an integer")
        name = str_course[1]
        professor = str_course[2]
        return id, name, professor

    def add_course(self):
        id, name, professor = self.read_course()
        self.course_controller.add(id, name, professor)

    def print_courses(self):
        courses = self.course_controller.get_all()
        if len(courses) > 0:
            for i in range(0,len(courses)):
                print(str(courses[i]))
        else:
            print("No courses found")

    def remove_course(self):
        print("Remove the course with the given id")
        id = raw_input("Course id: ")
        try:
            id = int(id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(id)
        self.grade_controller.remove_course(id)

    def update_course(self):
        print("The id must correspond with the course(id) you want to update_course")
        id, name, professor = self.read_course()
        self.grade_controller.update_course(id, name, professor)

    def search_by_id_course(self):
        id = raw_input("Course id: ")
        try:
            id = int(id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(id)
        course = self.course_controller.search_by_id(id)
        print(str(course))


    def generate_courses(self):
        number = raw_input("Number of courses: ")
        try:
            number = int(number)
        except ValueError:
            raise ValueError("Expected number")
        if number < 0:
            raise ValueError("Expected number bigger than zero")
        index = 1
        while index <= number:
            id = generate.generate_number()
            name = generate.generate_name()
            professor = generate.generate_name()
            course = self.course_controller.create(id, name, professor)
            if self.course_controller.search_for_generate(course) == -1:
                self.course_controller.add(course.get_id(), course.get_name(), course.get_professor())
                index = index + 1

###Students

    def read_student(self):
        str_student = raw_input("New student(registration number,name): ")
        str_student = str_student.split(",")
        if len(str_student) != 2:
            raise StudentError("Not a student")
        try:
            id = int(str_student[0])
        except ValueError:
            raise ValueError("Registration number must be an integer")
        name = str_student[1]
        return id, name

    def add_student(self):
        id, name = self.read_student()
        self.student_controller.add(id, name)

    def print_students(self):
        students = self.student_controller.get_all()
        if len(students) > 0:
            for i in range(0, len(students)):
                print(str(students[i]))
        else:
            print("No students found")

    def remove_student(self):
        print("Remove the student with the given registration number")
        id = raw_input("Student registration number: ")
        try:
            id = int(id)
        except ValueError:
            raise ValueError("Registration number must be an integer")
        validator.validate_id(id)
        self.grade_controller.remove_student(id)

    def update_student(self):
        print("The id must correspond with the student(registration number) you want to update_course")
        id, name = self.read_student()
        self.grade_controller.update_student(id, name)

    def search_by_id_student(self):
        id = raw_input("Student registration number: ")
        try:
            id = int(id)
        except ValueError:
            raise ValueError("Registration number must be an integer")
        validator.validate_id(id)
        student = self.student_controller.search_by_id(id)
        print(str(student))

    def search_by_name_student(self):
        name = raw_input("Student name: ")
        validate_name(name)
        students = self.student_controller.search_by_name(name)
        for i in range(0, len(students)):
            print(str(students[i]))

    def generate_students(self):
        number = raw_input("Number of students: ")
        try:
            number = int(number)
        except ValueError:
            raise ValueError("Expected number")
        if number < 0:
            raise ValueError("Expected number bigger than zero")
        index = 1
        while index <= number:
            id = generate.generate_number()
            name = generate.generate_name()
            student = self.student_controller.create(id, name)
            if self.student_controller.search_for_generate(student) == -1:
                self.student_controller.add(student.get_id(), student.get_name())
                index = index + 1

###grades

    def add_grade(self):
        student_id = raw_input("The registration number of the student: ")
        try:
            student_id = int(student_id)
        except ValueError:
            raise ValueError("Registration number must be an integer")
        validator.validate_id(student_id)
        course_id = raw_input("The course id: ")
        try:
            course_id = int(course_id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(course_id)
        grade = raw_input("Grade: ")
        try:
            grade = float(grade)
        except ValueError:
            raise ValueError("Grade must be a number")
        self.grade_controller.add(student_id, course_id, grade)

    def remove_grade(self):
        student_id = raw_input("The registration number of the student: ")
        try:
            student_id = int(student_id)
        except ValueError:
            raise ValueError("Registration number must be an integer")
        validator.validate_id(student_id)
        course_id = raw_input("The course id: ")
        try:
            course_id = int(course_id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(course_id)
        self.grade_controller.remove_grade(student_id, course_id)

    def update_grade(self):
        student_id = raw_input("The registration number of the student: ")
        try:
            student_id = int(student_id)
        except ValueError:
            raise ValueError("Registration number must be an integer")
        validator.validate_id(student_id)
        course_id = raw_input("The course id: ")
        try:
            course_id = int(course_id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(course_id)
        grade = raw_input("Grade: ")
        try:
            grade = float(grade)
        except ValueError:
            raise ValueError("Grade must be a number")
        self.grade_controller.update_grade(student_id, course_id, grade)

    def course_grades(self):
        course_id = raw_input("The course id: ")
        try:
            course_id = int(course_id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(course_id)
        grades = self.grade_controller.search_course_grades(course_id)
        if grades == []:
            print("No grades found")
        else:
            for i in range(0, len(grades)):
                print("Registration number: " + str(grades[i].get_id_student()) + " Name: " + grades[i].get_student_name() + " Grade: " + str(grades[i].get_grade()))

    def student_grades(self):
        student_id = raw_input("The student id: ")
        try:
            student_id = int(student_id)
        except ValueError:
            raise ValueError("Id must be an integer")
        validator.validate_id(student_id)
        grades = self.grade_controller.search_student_grades(student_id)
        if grades == []:
            print("No grades found")
        else:
            for i in range(0, len(grades)):
                print("Registration number: " + str(grades[i].get_id_student()) + " Name: " + grades[i].get_student_name() + " Grade: " + str(grades[i].get_grade()))


    def best_grades(self):
        students = self.grade_controller.search_highest_grades()
        if len(students) > 0:
            for i in range(0, len(students)):
                print((str(students[i][0].get_name())) + " Grade: " + str(students[i][1]))
        else:
            print("No grades found")

    def failed_st_per_professor(self):
        professors = self.grade_controller.failed_students()
        if len(professors) > 0:
            for i in range(0, len(professors)):
                s = "Professor: " + professors[i][0] + " Students failed: " +  str(professors[i][1])
                print(s)
        else:
            print("No professors found")

    def print_grades(self):
        grades = self.grade_controller.get_all()
        if len(grades) > 0:
            for i in range(0, len(grades)):
                print(str(grades[i]))
        else:
            print("No students found")

    def run(self):
        self.cli.run()
