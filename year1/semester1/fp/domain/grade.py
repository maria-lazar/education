class Grade:

    def __init__(self, student, course, grade):
        self.__student = student
        self.__course = course
        self._grade = grade

    def get_student(self):
        return self.__student

    def get_course(self):
        return self.__course

    def get_grade(self):
        return self._grade

    def set_grade(self, new_grade):
        self._grade = new_grade

    def __str__(self):
        return "Student: " + str(self.__student) + " Course: " + str(self.__course) + " Grade: " + str(self._grade)


